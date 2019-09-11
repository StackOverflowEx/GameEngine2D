package de.Luca.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import de.Luca.Packets.Packet;

public class Searching {
	
	private static final String[] presetMaps = new String[]{"Test"};
	
	private static ArrayList<ConnectionHandler> searching = new ArrayList<ConnectionHandler>();
	private static HashMap<String, ConnectionHandler[]> found = new HashMap<String, ConnectionHandler[]>();
	
	public static void searching(ConnectionHandler handler) {
		searching.add(handler);
		process();
	}
	
	public static ConnectionHandler[] getFound(String id) {
		return found.get(id);
	}
	
	public static ConnectionHandler getFoundPartner(ConnectionHandler ch) {
		for(String s : found.keySet()) {
			ConnectionHandler[] t = found.get(s);
			if(t[0] == ch)
				return t[1];
			else if(t[1] == ch)
				return t[0];
			else 
				continue;
		}
		return null;
	}
	
	public static void cancleMatch(String id, int error) {
		if(found.containsKey(id)) {
			Packet packet = new Packet();
			packet.packetType = Packet.MATCH_CANCELLED;
			packet.a = error;
			for(ConnectionHandler ch : found.get(id)) {
				ch.send(packet);
			}
		}
		found.remove(id);
	}

	private static void process() {
		System.out.println("Checking qeueu");
		if(searching.size() >= 2) {
			DemonConnectionHandler best = getBestDemon();
			if(best != null) {
				
				Random r = new Random();
				String map = presetMaps[r.nextInt(presetMaps.length)];
				
				Packet p = new Packet();
				p.packetType = Packet.MATCH_FOUND;
				p.a = map;
				searching.get(0).send(p);
				searching.get(1).send(p);
				String id = UUID.randomUUID().toString();
				found.put(id, new ConnectionHandler[] {searching.get(0), searching.get(1)});
				searching.clear();
			
				p = new Packet();
				p.packetType = Packet.DEMON_CREATE_SERVER;
				p.a = id;
				p.b = true;
				p.c = map;
				best.send(p);
				
			}
		}
	}
	
	private static DemonConnectionHandler getBestDemon() {
		int bestScore = Integer.MAX_VALUE;
		DemonConnectionHandler d = null;
		for(DemonConnectionHandler dch : DemonConnectionHandler.getHandler()) {
			int score = 0;
			DemonInfo info = dch.getDeamonInfo();
			score += info.getPing();
			score += info.getLoad() * 100;
			if(info.getFreeRamMB() < 1000) {
				score += 1000 - info.getFreeRamMB();
			}
			if(score < bestScore) {
				bestScore = score;
				d = dch;
			}
		}
		return d;
	}
	
}
