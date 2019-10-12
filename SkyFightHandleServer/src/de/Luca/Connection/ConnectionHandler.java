package de.Luca.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

import javax.mail.MessagingException;

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
	private String username;

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
					System.out.println(Base64.getEncoder().encodeToString(data));
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
	
	public void disconnect() {
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {}
		Searching.removeSearching(this);
		ConnectionHandler p = Searching.getFoundPartner(this);
		if(p != null) {
			Searching.removeSearching(this);
			Searching.removeSearching(p);
			Searching.removeFound(this);
			Packet pa = new Packet();
			pa.packetType = Packet.MATCH_CANCELLED;
			pa.a = Packet.ERROR_PLAYER_QUIT;
			System.out.println("Matchmaking canceled");
			p.send(pa);
		}
		System.out.println("Client disconnected");
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
			}else if (packet.packetType == Packet.PASSWORD_RESET) {
				handlePasswordReset(packet);
			}else {
				if(username == null) {
					Packet send = genErrorPacket(Packet.ERROR_NOT_LOGGED_IN);
					send(send);
					System.out.println("UNAUTHORIZED >> " + packet.packetType);
					return;
				}
				if (packet.packetType == Packet.SEARCHING) {
					handleSearch(packet);
				}
			}
			
		}
	}
	
	private void handlePasswordReset(Packet packet) {
		String mail = (String) packet.a;
		if(mail.contains("@")) {
			try {
				String token = Encryption.genKey();
				PasswordReset.addToken(mail, token);
				if(DatabaseManager.doseEmailExist(mail.toLowerCase())) {
					try {
						PasswordReset.sendReset(mail, DatabaseManager.getUsername(mail), token);
						Packet p = new Packet();
						p.packetType = Packet.SUCCESS;
						p.a = Packet.PASSWORD_RESET;
						p.b = 0;
						send(p);
					} catch (UnsupportedEncodingException | MessagingException e) {
						send(genErrorPacket(Packet.ERROR_COULD_NOT_RESET_PASSWORD));
						e.printStackTrace();
					}
				}else {
					send(genErrorPacket(Packet.ERROR_COULD_NOT_RESET_PASSWORD));
				}
			} catch (NoSuchAlgorithmException e) {
				send(genErrorPacket(Packet.ERROR_COULD_NOT_RESET_PASSWORD));
				e.printStackTrace();
				return;
			}
		}else {
			String token = mail;
			String newPass = (String) packet.b;
			mail = PasswordReset.getEmailForToken(token);
			if(mail != null) {
				PasswordReset.removeTokcen(token);
				if(!DatabaseManager.setPassword(mail, newPass)) {
					send(genErrorPacket(Packet.ERROR_COULD_NOT_RESET_PASSWORD));
				}else {
					Packet p = new Packet();
					p.packetType = Packet.SUCCESS;
					p.a = Packet.PASSWORD_RESET;
					p.b = 1;
					send(p);
				}
			}else {
				send(genErrorPacket(Packet.ERROR_COULD_NOT_RESET_PASSWORD));
				return;
			}
		}
		
	}
	
	private void handleSearch(Packet packet) {
		if(DemonConnectionHandler.getHandler().size() == 0) {
			Packet p = genErrorPacket(Packet.ERROR_MATCHES_NOT_AVALIABLE);
			send(p);
			return;
		}
		
		Packet res = new Packet();
		res.packetType = Packet.SUCCESS;
		res.a = Packet.SEARCHING;
		
		if(Searching.getSearching().contains(this)) {
			res.b = 0;
			Searching.removeSearching(this);
		}else {
			res.b = 1;
			try {
				Searching.searching(this);
			}catch (Exception e) {
				System.out.println("ERROR");
				e.printStackTrace();
			}
		}
		
		
		if(Searching.getFoundPartner(this) == null) {
			send(res);
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
		
		if(isLoggedIn(username)) {
			send(genErrorPacket(Packet.ERROR_ACCOUNT_LOGGED_IN));
			return;
		}
		
		int status = DatabaseManager.login(username, password);
		if(status == 0) {
			send(genSuccess(packet.packetType));
			this.username = username.toLowerCase();
		}
		
		send(genErrorPacket(status));
	}
	
	private static boolean isLoggedIn(String username) {
		String search = username.toLowerCase();
		for(ConnectionHandler con : handler) {
			if(con.getUsername() != null && con.getUsername().equalsIgnoreCase(search)) {
				return true;
			}
		}
		return false;
	}
	
	public String getUsername() {
		return username;
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
			handler.remove(this);
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
