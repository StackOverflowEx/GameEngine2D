package de.Luca.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

import de.Luca.Main.Main;
import de.Luca.Packets.Packet;
import de.Luca.Security.Encryption;
import de.Luca.Security.RSAKeyPairGenerator;
import de.Luca.Security.RSAUtil;

public class ConnectionHandler implements Runnable {

	private static ArrayList<ConnectionHandler> handler = new ArrayList<ConnectionHandler>();

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private PrivateKey serverPrivateKey;
	private String serverPublicKey;
	private String clientPublicKey;
	private String AESKey;
	private String id;

	public ConnectionHandler(Socket socket) {
		this.socket = socket;
		addHandler(this);
		init();
		
		System.out.println("Connection established: " + socket.getInetAddress() + ":" + socket.getPort());
		
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<ConnectionHandler> getHandler() {
		return (ArrayList<ConnectionHandler>) ConnectionHandler.handler.clone();
	}

	public static void addHandler(ConnectionHandler handler) {
		ConnectionHandler.handler.add(handler);
	}

	public static void removeHandler(ConnectionHandler handler) {
		ConnectionHandler.handler.remove(handler);
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
			disconnect();
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!socket.isClosed()) {
			try {
//				if (is.available() > 0) {
					byte[] data = getDataFromInputStream();
					if(data == null) {
						continue;
					}
					String input = null;
					if(clientPublicKey == null) {
						input = new String(data);
					} else if(AESKey == null){
						input = RSAUtil.decrypt(data, serverPrivateKey);
					}else {
						input = Encryption.decrypt(data, AESKey);
					}
					Packet packet = new Packet(input);
					handlePacket(packet);
//				}
			}catch (Exception e) {
				disconnect();
			}

		}
	}

	private int nullCount = 0;
	private byte[] getDataFromInputStream() throws IOException {
		byte[] buffer = new byte[1024];
		int t = is.read(buffer);
		if(t == -1) {
			nullCount++;
			if(nullCount == 10) {
				disconnect();
			}
			return null;
		}
		if(t == 0) {
			System.out.println(0);
			return null;
		}
		nullCount = 0;
		byte[] ret = new byte[t];
		for(int i = 0; i < t; i++) {
			ret[i] = buffer[i];
		}
		return ret;
	}

	private void handlePacket(Packet packet) {
		if (packet.packetType == Packet.HANDSHAKE) {
			System.out.println("Handshake width " + socket.getInetAddress() + ":" + socket.getPort());
			handleHandshake(packet);
		} else {
			if (clientPublicKey == null) {
				Packet p = genErrorPacket(Packet.ERROR_MISSING_HANDSHAKE);
				sendUnencrypted(p);
				return;
			}
			
			if(packet.packetType == Packet.ERROR) {
				handleError(packet);
			}else if(packet.packetType == Packet.SUCCESS) {
				handleSuccess(packet);
			}else if (packet.packetType == Packet.KEY) {
				handleKey(packet);
			}else if (packet.packetType == Packet.ID) {
				handleID(packet);
			}
			
		}
	}
	
	private void handleID(Packet packet) {
		id = (String) packet.a;
		ServerGenerator.setStarted(id);
		Packet p = new Packet();
		p.packetType = Packet.DEMON_SERVER_CREATED;
		p.a = id;
		p.b = ServerGenerator.getPort(id);
		Main.con.send(p);
	}
	
	private void handleKey(Packet packet) {
		String key = "";
		try {
			key = Encryption.genKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Packet p = new Packet();
		p.packetType = Packet.DEMON_KEY;
		p.a = key;
		send(p);
		AESKey = key;
	}
	
	
	private void handleSuccess(Packet packet) {
		System.out.println("Error/Client (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")" + (String)packet.a);
	}
	
	private void handleError(Packet packet) {
		System.out.println("Info/Client (" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + ")" + (String)packet.a);
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
	
	public void disconnect() {
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {}
		ServerGenerator.stop(id);
		handler.remove(this);
		System.out.println("Gameserver disconnected");
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
			e.printStackTrace();
		}
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

}
