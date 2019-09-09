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

public class Connection {
	
	public static final int HANDLE_SERVER_PORT = 33331;
	public static final String HANDLE_SERVER_IP = "127.0.0.1";
	public static final String endOfStream = "END"; //69 78 68
	
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
			this.ip = ip;
			this.port = port;
			socket = new Socket(ip, port);
			connected = true;
			init();
			sendHandshake();
			listen();
		} catch (IOException e) {
			e.printStackTrace();
			connected = false;
			return;
		}
	}
	
	public void retry() {
		long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) < 5000) {}
		try {
			socket = new Socket(ip, port);
			connected = true;
			init();
			sendHandshake();
			listen();
		} catch (IOException e) {
			e.printStackTrace();
			connected = false;
			return;
		}
	}
	
	public String getIP() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean isConnected() {
		return connected;
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
			clientPrivateKey = keyGen.getPrivateKey();
			clientPublicKey = Base64.getEncoder().encodeToString(keyGen.getPublicKey().getEncoded());
		} catch (NoSuchAlgorithmException e) {
			disconnect();
			e.printStackTrace();
		}
	}
	
	private void sendHandshake() {
		Packet packet = new Packet();
		packet.packetType = Packet.DEMON_HANDSHAKE;
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
		System.out.println("Disconnected from deamon");
		System.exit(0);
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
						disconnect();
						e.printStackTrace();
					}
				}
			}
		});
		th.start();
	}
	
	private int nullCount = 0;
	private ArrayList<Byte> bytes = new ArrayList<Byte>();
	private byte[] getDataFromInputStream() throws IOException {
		bytes.add((byte) is.read());
		if(bytes.get(bytes.size() - 1) == -1) {
			nullCount++;
			bytes.remove(bytes.size() - 1);
			if(nullCount == 10) {
				disconnect();
			}
			return null;
		}
		nullCount = 0;
		if(bytes.size() >= 3) {
			byte[] end = new byte[] {bytes.get(bytes.size()-3), bytes.get(bytes.size()-2), bytes.get(bytes.size()-1)};
			if(new String(end).equals(endOfStream)) {
				byte[] ret = new byte[bytes.size() - 3];
				for(int i = 0; i < ret.length; i++) {
					ret[i] = bytes.get(i);
				}
				bytes.clear();
				return ret;
			}
		}
		return getDataFromInputStream();
	}

	private void handlePacket(Packet packet) {
		if (packet.packetType == Packet.HANDSHAKE) {
			handleHandshake(packet);
		} else {
			if (serverPublicKey == null) {
				Packet p = genErrorPacket(Packet.ERROR_MISSING_HANDSHAKE);
				sendUnencrypted(p);
				return;
			}
			
			if(packet.packetType == Packet.KEY) {
				handleKey(packet);
			}
		}
	}
	
	
	
	private void handleKey(Packet packet) {
		AESKey = (String) packet.a;
		
		Packet p = new Packet();
		p.packetType = Packet.ID;
		p.a = Main.id;
		send(p);
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
	
	private byte[] getWithEnd(byte[] message) {
		byte[] ret = new byte[message.length + 3];
		for(int i = 0; i < message.length; i++) {
			ret[i] = message[i];
		}
		ret[ret.length - 3] = 69;
		ret[ret.length - 2] = 78;
		ret[ret.length - 1] = 68;
		return ret;
	}
	
	public void sendUnencrypted(Packet packet) {
		try {
			String msg = packet.toJSONString();
			byte[] bMSG = msg.getBytes();
			bMSG = getWithEnd(bMSG);
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
			enMSG = getWithEnd(enMSG);
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
			enMSG = getWithEnd(enMSG);
			os.write(enMSG);
			os.flush();
		} catch (Exception e) {
			disconnect();
			e.printStackTrace();
		}
	}

}
