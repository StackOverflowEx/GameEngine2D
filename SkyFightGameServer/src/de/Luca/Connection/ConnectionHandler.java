package de.Luca.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

import de.Luca.GameLogic.GameLogic;
import de.Luca.GameLogic.PlayerData;
import de.Luca.Main.Server;
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
	private PlayerData playerData;
	private Packet sendPacket;

	public ConnectionHandler(Socket socket) {
		this.socket = socket;
		addHandler(this);
		init();
		if(Server.player1 == null) {
			Server.player1 = this;
		}else if(Server.player2 == null) {
			Server.player2 = this;
		}else {
			disconnect();
		}
		
		System.out.println("Connection established: " + socket.getInetAddress() + ":" + socket.getPort());
		
	}
	
	public boolean finishedHandshaking() {
		return AESKey != null;
	}
	
	public PlayerData getPlayerData() {
		return playerData;
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
	
	public Packet getSendPacket(){
		return sendPacket;
	}
	
	public boolean isConnected() {
		return socket.isConnected();
	}
	
	public void sendSendPacket() {
		sendPacket.packetType = Packet.GAME_DATA;
		send(sendPacket);
		sendPacket = new Packet();
	}

	private void init() {
		playerData = new PlayerData();
		sendPacket = new Packet();
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
			disconnect();
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
				e.printStackTrace();
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
				System.out.println("Key requested");
				handleKey(packet);
			}else {
				GameLogic.addRecievedPacket(this, packet);
			}
			
		}
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
		System.out.println("Requested key: " + AESKey);
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
		Packet p = new Packet();
		p.packetType = Packet.ERROR_PLAYER_QUIT;
		if(Server.player1 == this && Server.player2 != null && Server.player2.finishedHandshaking()) {
			Server.player2.send(p);
		}else if(Server.player1 != null && Server.player1.finishedHandshaking()){
			Server.player1.send(p);
		}
				
		System.out.println("Player disconnected");
		System.exit(0);
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
			e.printStackTrace();
			System.exit(-1);
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
			e.printStackTrace();
		}
	}

}
