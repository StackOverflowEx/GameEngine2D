package de.Luca.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

import de.Luca.MySQL.DatabaseManager;
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
				if (is.available() > 0) {
					byte[] data = getDataFromInputStream();
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
				}
			}catch (Exception e) {
				disconnect();
				e.printStackTrace();
			}

		}
	}
	
	public void disconnect() {
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {}
		ConnectionHandler p = Searching.getFoundPartner(this);
		if(p != null) {
			Packet pa = new Packet();
			pa.packetType = Packet.MATCH_CANCELLED;
			pa.a = Packet.ERROR_PLAYER_QUIT;
			p.send(pa);
		}
		System.out.println("Demon disconnected");
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
			
			if(packet.packetType == Packet.LOGIN) {
				handleLogin(packet);
			}else if(packet.packetType == Packet.REGISTRATION) {
				handleRegistration(packet);
			}else if(packet.packetType == Packet.PING) {
				handlePing(packet);
			}else if(packet.packetType == Packet.ERROR) {
				handleError(packet);
			}else if(packet.packetType == Packet.SUCCESS) {
				handleSuccess(packet);
			}else if (packet.packetType == Packet.KEY) {
				handleKey(packet);
			}else if (packet.packetType == Packet.SEARCHING) {
				handleSearch(packet);
			}
			
		}
	}
	
	private void handleSearch(Packet packet) {
		if(DemonConnectionHandler.getHandler().size() == 0) {
			Packet p = genErrorPacket(Packet.ERROR_MATCHES_NOT_AVALIABLE);
			send(p);
			return;
		}
		Searching.searching(this);
	}
	
	private void handleKey(Packet packet) {
		String key = "";
		try {
			key = Encryption.genKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Packet p = new Packet();
		p.packetType = Packet.KEY;
		p.a = key;
		send(p);
		AESKey = key;
	}
	
	private void handlePing(Packet packet) {
		Packet p = new Packet();
		p.packetType = Packet.PING;
		p.a = (long)packet.a;
		send(p);
	}
	
	private void handleRegistration(Packet packet) {
		String username = (String) packet.a;
		String password = (String) packet.b;
		String email = (String) packet.c;
		int status = DatabaseManager.register(username, email, password);
		if(status == 0)
			send(genSuccess(packet.packetType));
		
		send(genErrorPacket(status));
	}
	
	private void handleLogin(Packet packet) {
		String username = (String) packet.a;
		String password = (String) packet.b;
		int status = DatabaseManager.login(username, password);
		if(status == 0)
			send(genSuccess(packet.packetType));
		
		send(genErrorPacket(status));
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
