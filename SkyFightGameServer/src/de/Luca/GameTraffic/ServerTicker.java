package de.Luca.GameTraffic;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import de.Luca.Connection.ConnectionHandler;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;

public class ServerTicker{
	
	private static ConnectionHandler con1, con2;
	private static GamePacket in1, in2;
	private static int counter = 0;
	
	private static Timer timer;
	private static int TPS;
	
	private static ArrayList<JSONObject> blockChanges1 = new ArrayList<JSONObject>();
	private static ArrayList<JSONObject> blockChanges2 = new ArrayList<JSONObject>();
	
	private static boolean started = false;
	
	private static void executeTick() {
		
		if(con1 != null && con1.finishedHandshaking() && con1.isConnected() && con2 != null && con2.finishedHandshaking() && con2.isConnected()) {
			if(!started) {
				started = true;
				sendInfo();
				return;
			}
			con1.send(craftPacketForPlayer(con1));
			con2.send(craftPacketForPlayer(con2));
			counter++;
			blockChanges1.clear();
			blockChanges2.clear();
		}
		
	}
	
	public static void connect(ConnectionHandler con) {
		if(con1 == null) {
			con1 = con;
		}else {
			con2 = con;
		}
	}
	
	public static void recievePacket(GamePacket p, ConnectionHandler con) {
		if(con == con1) {
			if(in1 != null && (int)p.i < (int)in1.i) {
				return;
			}
			in1 = p;
		}else {
			if(in2 != null && (int)p.i < (int)in2.i) {
				return;
			}
			in2 = p;
		}
		processBlockChanges(p, con);
	}
	
	
	/*
	 * "e": [
	 * 	{"x": 1, "y": 2, "breakPercent": 0.45, "name": "Dirt"},
	 * 	{"x": 4, "y": 3, "breakPercent": 1.0, "name": "Dirt"}
	 * ]
	 */
	private static void processBlockChanges(GamePacket p, ConnectionHandler con) {
		if(p.e != null) {
			String blocks = p.e.toString();
			JSONArray changes = new JSONArray(blocks);
			for(int i = 0; i < changes.length(); i++) {
				JSONObject blockData = changes.getJSONObject(i);
				if(con == con1) {
					blockChanges1.add(blockData);
				}else {
					blockChanges2.add(blockData);
				}
			}
		}
	}
	
	private static JSONArray getBlockChangesFor(ConnectionHandler con) {
		JSONArray ret = new JSONArray();
		if(con == con1) {
			for(JSONObject blockData : blockChanges2) {
				ret.put(blockData);
			}
		}else {
			for(JSONObject blockData : blockChanges1) {
				ret.put(blockData);
			}
		}
		return ret;
	}
	
	public static void sendInfo() {
		
		System.out.println("Starting game...");
		
		GamePacket info = new GamePacket();
		info.packetType = Packet.GAME_DATA;
		info.setGamePacketType(GamePacket.INFO);
		
		info.b = WorldLoader.spawn1.x;
		info.c = WorldLoader.spawn1.y;
		info.d = true;
		info.e = WorldLoader.spawn2.x;
		info.f = WorldLoader.spawn2.y;
		info.g = true;
		info.h = TPS;	
		info.i = counter;
		counter++;
		con1.send(info);
		in1 = info;
		
		
		info.b = WorldLoader.spawn2.x;
		info.c = WorldLoader.spawn2.y;
		info.e = WorldLoader.spawn1.x;
		info.f = WorldLoader.spawn1.y;
		con2.send(info);
		in2 = info;
	}
	
	private static Packet craftPacketForPlayer(ConnectionHandler con) {
		
		GamePacket p = new GamePacket();
		p.packetType = Packet.GAME_DATA;
		p.setGamePacketType(GamePacket.POSITION);
		
		if(con == con1) {
			p.b = in2.b; //x
			p.c = in2.c; //y
			p.d = in2.d; //facing Right
		}else {
			p.b = in1.b; //x
			p.c = in1.c; //y
			p.d = in1.d; //facing Right
		}
		
		p.i = counter;
		p.e = getBlockChangesFor(con).toString();
		
		return p;
	}
	
	public static void startTicking(int tps) {
		
		
		TPS = tps;
		if(timer == null) {
			System.out.println("Starting ticking... Waiting for clients...");
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				
				@Override
				public void run() {
					executeTick();
				}
			}, 0, (1000 / TPS));
		}
	}
	
	public static void stopTicking() {
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	

}
