package de.Luca.Connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.Luca.Main.Main;
import de.Luca.Packets.Packet;

public class ServerGenerator {
	
	public static final String SERVER_FILE = "data/Server.jar";
	public static final int START_PORT = 33334;
	private static HashMap<String, Integer> ports = new HashMap<String, Integer>();
	private static HashMap<String, Long> gen = new HashMap<String, Long>();
	
	public static int getPort(String id) {
		if(ports.containsKey(id)) {
			return ports.get(id);
		}
		return -1;
	}
	
	public static int genServer(String mapPath, String id) {
		if(!new File(mapPath).exists()) {
			return Packet.ERROR_SERVER;
		}
		int port = -1;
		for(int i = START_PORT; i < Integer.MAX_VALUE; i++) {
			if(!ports.values().contains(i)) {
				port = i;
				break;
			}
		}
		if(port == -1) {
			return Packet.ERROR_SERVER;
		}
		ports.put(id, port);
		gen.put(id, System.currentTimeMillis());
		
		File server = new File(Main.root + "/" + SERVER_FILE);

		try {
			Runtime.getRuntime().exec("java -jar " + server.getPath() + " " + port + " " + mapPath + " " + id);
			System.out.println("java -jar " + server.getPath() + " " + port + " " + mapPath + " " + id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Generated server");
		return port;
	}
	
	
	public static void setStarted(String id) {
		gen.remove(id);
	}
	
	public static void stop(String id) {
		ports.remove(id);
	}
	
	public static void check() {
		for(String s : gen.keySet()) {
			if(System.currentTimeMillis() - gen.get(s) > 30*1000) {
				gen.remove(s);
				ports.remove(s);
				Packet p = new Packet();
				p.packetType = Packet.DEMON_ERROR;
				p.a = Packet.ERROR_COULD_NOT_CREATE_SERVER;
				Main.con.send(p);
			}
		}
	}

}
