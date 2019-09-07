package de.Luca.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.Luca.Packets.Packet;
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
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void run() {
		while (is != null) {
			try {
				if (is.available() > 0) {
					byte[] data = getDataFromInputStream();
					String input = null;
					if(clientPublicKey == null) {
						input = new String(data);
					}else {
						input = RSAUtil.decrypt(data, serverPrivateKey);
					}
					Packet packet = new Packet(input);
					handlePacket(packet);
				}
			}catch (IOException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException e) {
				e.printStackTrace();
			}

		}
	}

	private byte[] getDataFromInputStream() throws IOException {
		byte[] ret = new byte[is.available()];
		is.read(ret);
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
		}
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

	public void sendUnencrypted(Packet packet) {
		try {
			String msg = packet.toJSONString();
			byte[] bMSG = msg.getBytes();
			os.write(bMSG);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Packet packet) {
		try {
			String msg = packet.toJSONString();
			byte[] enMSG = RSAUtil.encrypt(msg, clientPublicKey);
			os.write(enMSG);
			os.flush();
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			byte[] enMSG = RSAUtil.encrypt(msg, clientPublicKey);
			os.write(enMSG);
			os.flush();
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}

}
