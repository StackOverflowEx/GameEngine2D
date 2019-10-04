package de.Luca.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

import de.Luca.EventManager.EventManager;
import de.Luca.Packets.Packet;
import de.Luca.Security.Encryption;
import de.Luca.Security.RSAKeyPairGenerator;
import de.Luca.Security.RSAUtil;
import de.Luca.Window.Window;

public class Connection {
	
	public static final int HANDLE_SERVER_PORT = 33333;
//	public static final String HANDLE_SERVER_IP = "127.0.0.1";
	public static final String HANDLE_SERVER_IP = "167.86.87.105";
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private PrivateKey clientPrivateKey;
	private String clientPublicKey;
	private String serverPublicKey;
	private Thread th;
	private boolean connected;
	private String AESKey;
	
	private String ip; 
	private int port;
	
	public Connection(String ip, int port) {
		try {
			System.out.println("Connection to " + ip + ":" + port);
			this.ip = ip;
			this.port = port;
			socket = new Socket(ip, port);
			connected = true;
			init();
			listen();
			sendHandshake();
		} catch (IOException e) {
			e.printStackTrace();
			EventManager.fireEvent(new ConnectionErrorEvent(this));
			connected = false;
			return;
		}
	}
	
	public void retry() {
		long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) < 5000 && !Window.shouldClose()) {}
		if(Window.shouldClose()) {
			return;
		}
		try {
			socket = new Socket(ip, port);
			connected = true;
			init();
			listen();
			sendHandshake();
		} catch (IOException e) {
			e.printStackTrace();
			EventManager.fireEvent(new ConnectionErrorEvent(this));
			connected = false;
			return;
		}
	}
	
	public boolean finishedHandshaking() {
		return AESKey != null;
	}
	
	public String getIP() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean isConnected() {
		return connected && !socket.isClosed();
	}
	
	private void init() {
		
		AESKey = null;
		serverPublicKey = null;
		
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
			clientPrivateKey = keyGen.getPrivateKey();
			clientPublicKey = Base64.getEncoder().encodeToString(keyGen.getPublicKey().getEncoded());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			disconnect();
		}
		
		EventManager.fireEvent(new ConnectionConnectedEvent(this));
	}
	
	private void sendHandshake() {
		Packet packet = new Packet();
		packet.packetType = Packet.HANDSHAKE;
		packet.a = clientPublicKey;
		System.out.println("Handshaking with " + HANDLE_SERVER_IP + ":" + HANDLE_SERVER_PORT);
		sendUnencrypted(packet);
	}
	
	public void disconnect() {
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		EventManager.fireEvent(new ConnectionDisconnectedEvent(this));
		System.out.println("Disconnected from server.");
	}
	
	private void listen() {
		th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!socket.isClosed()) {
					try {
//						if (is.available() > 0) {
							byte[] data = getDataFromInputStream();
							if(data == null) {
								continue;
							}
							System.out.println(new String(data));
							String input = null;
							if(serverPublicKey == null) {
								input = new String(data);
							}else if(AESKey == null){
								input = RSAUtil.decrypt(data, clientPrivateKey);
							}else {
								input = Encryption.decrypt(data, AESKey);
							}
							Packet packet = new Packet(input);
							handlePacket(packet);
//						}
					}catch (Exception e) {
						e.printStackTrace();
						disconnect();
					}
				}
			}
		});
		th.start();
	}

	public void close() {
		try {
			is.close();
			os.close();
			socket.close();
		}catch (IOException e) {
			e.printStackTrace();
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
			handleHandshake(packet);
		} else if(packet.packetType == Packet.KEY) {
			handleKey(packet);
		}else {
			if (serverPublicKey == null) {
				Packet p = genErrorPacket(Packet.ERROR_MISSING_HANDSHAKE);
				sendUnencrypted(p);
				return;
			}
			EventManager.fireEvent(new ConnectionRecieveEvent(packet, this));
		}
	}
	
	private void handleKey(Packet packet) {
		AESKey = (String) packet.a;
		System.out.println("Key: " + AESKey);
	}
	
	private void handleHandshake(Packet packet) {
		if (packet.packetType == Packet.HANDSHAKE) {
			serverPublicKey = (String) packet.a;
			System.out.println("RSA/public key (Server): " + serverPublicKey);
			Packet p = new Packet();
			p.packetType = Packet.KEY;
			send(p);
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
			disconnect();
			e.printStackTrace();
		}
	}
	
	public void send(Packet packet) {
		try {
			String msg = packet.toJSONString();
			byte[] enMSG = null;
			if(AESKey == null) {
				enMSG = RSAUtil.encrypt(msg, serverPublicKey);
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
				enMSG = RSAUtil.encrypt(msg, serverPublicKey);
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
