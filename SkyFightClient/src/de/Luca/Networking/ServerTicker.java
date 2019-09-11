package de.Luca.Networking;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.joml.Vector2f;

import de.Luca.Connection.Connection;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;

public class ServerTicker {
	
	private static final int TPS = 30;
	
	public static boolean finishedHandshaking = false;
	
	private static Timer timer;
	private static ArrayList<Packet> recieved = new ArrayList<Packet>();
	private static Packet send;
	
	public static void addRecievedPacket(Packet p) {
		recieved.add(p);
	}
	
	public static void startTicking(Connection con) {
		if(timer != null) {
			return;
		}
		send = null;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if(!con.finishedHandshaking()) {
					return;
				}
				SkyFightClient.pother.setVisible(true);
				processIncomingPackets();
				craftPacket();
				con.send(send);
			}
		}, 0, 1000/TPS);
	}
	
	public static void stopTicking() {
		timer.cancel();
		timer = null;
	}
	
	private static void processIncomingPackets() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(Packet packet : recieved) {
					processPacket(packet);
				}
			}
		}).start();
	}
	
	public static void processPacket(Packet packet) {
		int packetType = packet.packetType;
		if(packetType == Packet.GAME_DATA) {
			
			
			
			GamePacket gp = (GamePacket) packet;
			Packet movePacket = gp.getOwnMovementPacket();
			if(movePacket != null) {
				processMovepacket(movePacket);
			}
		}
	}
	
	private static void processMovepacket(Packet packet) {
		float x = (float) packet.a;
		float y = (float) packet.b;
		boolean facingRight = (boolean) packet.c;
		
		SkyFightClient.p.setPosition(new Vector2f(x, y));
//		pd.setFacingRight(facingRight);
	}
	
	private static void craftPacket() {
		send = new Packet();
		send.packetType = Packet.GAME_DATA;
		
		Packet p = new Packet();
		p.packetType = Packet.GAME_POSITION_AND_FACING;
		p.a = SkyFightClient.p.getWorldPos().x;
		p.b = SkyFightClient.p.getWorldPos().y;
		p.c = false;
		
		send.a = p.toJSONString();
	}

}
