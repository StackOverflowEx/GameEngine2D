package de.Luca.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

import de.Luca.Packets.Packet;
import de.Luca.Security.Encryption;
import de.Luca.Security.RSAKeyPairGenerator;
import de.Luca.Security.RSAUtil;

public class DemonConnectionHandler implements Runnable {

	private static ArrayList<DemonConnectionHandler> handler = new ArrayList<DemonConnectionHandler>();

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private PrivateKey serverPrivateKey;
	private String serverPublicKey;
	private String clientPublicKey;
	private long lastInfo;
	private DemonInfo info;
	private String AESKey;

	public DemonConnectionHandler(Socket socket) {
		this.socket = socket;
		lastInfo = 0;
		addHandler(this);
		init();

		System.out.println("Demon connection established: " + socket.getInetAddress() + ":" + socket.getPort());

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<DemonConnectionHandler> getHandler() {
		return (ArrayList<DemonConnectionHandler>) DemonConnectionHandler.handler.clone();
	}

	public static void addHandler(DemonConnectionHandler handler) {
		DemonConnectionHandler.handler.add(handler);
	}

	public static void removeHandler(DemonConnectionHandler handler) {
		DemonConnectionHandler.handler.remove(handler);
	}

	private void init() {
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
			serverPrivateKey = keyGen.getPrivateKey();
			serverPublicKey = Base64.getEncoder().encodeToString(keyGen.getPublicKey().getEncoded());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void run() {
		while (!socket.isClosed()) {
			requestDemonInfo();
			try {
				if (is.available() > 0) {
					byte[] data = getDataFromInputStream();
					String input = null;
					if (clientPublicKey == null) {
						input = new String(data);
					} else if(AESKey == null){
						input = RSAUtil.decrypt(data, serverPrivateKey);
					}else {
						input = Encryption.decrypt(data, AESKey);
					}
					Packet packet = new Packet(input);
					handlePacket(packet);
				}
			} catch (Exception e) {
				e.printStackTrace();
				disconnect();
			}

		}
		return;
	}

	private void requestDemonInfo() {
		if (AESKey != null && clientPublicKey != null && (System.currentTimeMillis() - lastInfo) > 10000) {
			System.out.println("Requesting DemonInfo");
			Packet p = new Packet();
			p.packetType = Packet.DEMON_INFO;
			p.a = System.currentTimeMillis();
			send(p);
			lastInfo = System.currentTimeMillis();
		}
	}

	private byte[] getDataFromInputStream() throws IOException {
		byte[] ret = new byte[is.available()];
		is.read(ret);
		return ret;
	}

	private void handlePacket(Packet packet) {
		if (packet.packetType == Packet.HANDSHAKE) {
			System.out.println("Handshake width demon " + socket.getInetAddress() + ":" + socket.getPort());
			handleHandshake(packet);
		} else {
			if (clientPublicKey == null) {
				Packet p = genErrorPacket(Packet.ERROR_MISSING_HANDSHAKE);
				sendUnencrypted(p);
				return;
			}

			if (packet.packetType == Packet.DEMON_ERROR) {
				handleError(packet);
			} else if (packet.packetType == Packet.DEMON_SUCCESS) {
				handleSuccess(packet);
			} else if (packet.packetType == Packet.DEMON_INFO) {
				handleDeamonInfo(packet);
			} else if (packet.packetType == Packet.DEMON_KEY) {
				handleDemonKey(packet);
			}

		}
	}
	
	private void handleDemonKey(Packet packet) {
		String key = "";
		try {
			key = Encryption.genKey();
		} catch (NoSuchAlgorithmException e) {
			disconnect();
			e.printStackTrace();
		}
		Packet p = new Packet();
		p.packetType = Packet.DEMON_KEY;
		p.a = key;
		send(p);
		AESKey = key;
	}

	private void handleDeamonInfo(Packet packet) {
		int ping = (int) (System.currentTimeMillis() - (long) packet.a);
		double l = (double) packet.b;
		float load = (float) l;
		int freeRamMB = (int) packet.c;
		System.out.println(ping + " | " + l + " | " + freeRamMB);
		if (info == null) {
			info = new DemonInfo(ping, freeRamMB, load);
		} else {
			info.setPing(ping);
			info.setLoad(load);
			info.setFreeRamMB(freeRamMB);
		}
	}

	public DemonInfo getDeamonInfo() {
		return info;
	}

	private void handleSuccess(Packet packet) {
		System.out.println("Error/Demon (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")"
				+ (String) packet.a);
	}

	private void handleError(Packet packet) {
		System.out.println("Info/Demon (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")"
				+ (String) packet.a);
	}

	private void handleHandshake(Packet packet) {
		if (packet.packetType == Packet.HANDSHAKE) {
			clientPublicKey = (String) packet.a;
			Packet p = new Packet();
			p.packetType = Packet.HANDSHAKE;
			p.a = serverPublicKey;
			sendUnencrypted(p);
		}
	}

	public Packet genErrorPacket(int error) {
		Packet p = new Packet();
		p.packetType = Packet.ERROR;
		p.a = error;
		return p;
	}

	public Packet genSuccess(int recievedType) {
		Packet p = new Packet();
		p.packetType = Packet.SUCCESS;
		p.a = recievedType;
		return p;
	}

	public void sendUnencrypted(Packet packet) {
		try {
			String msg = packet.toJSONString();
			byte[] bMSG = msg.getBytes();
			os.write(bMSG);
			os.flush();
		} catch (IOException e) {
			disconnect();
		}
	}

	public void disconnect() {
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {}
		System.out.println("Demon disconnected");
	}

	public void send(Packet packet) {
		try {
			String msg = packet.toJSONString();
			byte[] enMSG = null;
			if(AESKey == null) {
				enMSG = RSAUtil.encrypt(msg, clientPublicKey);
			}else {
				enMSG = Encryption.encrypt(msg, AESKey);
			}
			os.write(enMSG);
			os.flush();
		} catch (Exception e) {
			disconnect();
		}
	}

	public void send(String msg) {
		try {
			byte[] enMSG = null;
			if(AESKey == null) {
				enMSG = RSAUtil.encrypt(msg, clientPublicKey);
			}else {
				enMSG = Encryption.encrypt(msg, AESKey);
			}
			os.write(enMSG);
			os.flush();
		} catch (Exception e) {
			disconnect();
		}
	}

}
