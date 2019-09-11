package de.Luca.GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.Luca.Connection.ConnectionHandler;
import de.Luca.Main.Server;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;

public class GameLogic {
	
	private static final float MOVEMENT_THRESHOLD_PER_SECOND = 6;
	private static final int TPS = 30;
	
	private static Timer timer;
	private static HashMap<ConnectionHandler, ArrayList<Packet>> recieved = new HashMap<ConnectionHandler, ArrayList<Packet>>();
	
	public static void addRecievedPacket(ConnectionHandler ch, Packet p) {
		if(!recieved.containsKey(ch)) {
			ArrayList<Packet> tmp = new ArrayList<Packet>();
			recieved.put(ch, tmp);
		}
		recieved.get(ch).add(p);
	}
	
	public static void startTicking() {
		if(timer != null) {
			return;
		}
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				processPackets();
				if(Server.player1 != null) {
					if(Server.player1.finishedHandshaking() && Server.player1.isConnected()) {
						Server.player1.send(Server.player1.getSendPacket());
					}
				}
				if(Server.player2 != null) {
					if(Server.player2.finishedHandshaking() && Server.player2.isConnected()) {
						Server.player2.send(Server.player2.getSendPacket());
					}
				}
			}
		}, 1000, 1000/TPS);
	}
	
	private static void processPackets() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(ConnectionHandler con : recieved.keySet()) {
					for(Packet packet : recieved.get(con)) {
						processPacket(con, packet);
					}
				}
			}
		}).start();
	}
	
	public static void processPacket(ConnectionHandler con, Packet packet) {
		int packetType = packet.packetType;
		if(packetType == Packet.GAME_DATA) {
			
			ConnectionHandler other = Server.player1;
			if(Server.player1 == con) {
				other = Server.player2;
			}
			PlayerData pd = con.getPlayerData();
			
			GamePacket gp = (GamePacket) packet;
			Packet movePacket = gp.getMovementPacket();
			if(movePacket != null) {
				processMovepacket(movePacket, con, other, pd);
			}
		}
	}
	
	private static void processMovepacket(Packet packet, ConnectionHandler con, ConnectionHandler other, PlayerData pd) {
		float x = (float) packet.a;
		float y = (float) packet.b;
		boolean facingRight = (boolean) packet.c;
		
		long lastMove = pd.getLastMove();
		float delta = (System.currentTimeMillis() - lastMove) / 1000f;
		float allowed = MOVEMENT_THRESHOLD_PER_SECOND * delta;
		if(Math.abs(pd.getX() - x) > allowed) {
			telportToOldPosition(con, other);
			return;
		}			
		if(pd.getY() < y) {
			if(Math.abs(pd.getY() - y) > allowed) {
				telportToOldPosition(con, other);
				return;
			}		
		}
		if(Blocks.isCollidingWithBlock(x, y)) {
			telportToOldPosition(con, other);
			return;
		}
		pd.setX(x);
		pd.setY(y);
		pd.setFacingRight(facingRight);
		pd.setLastMove(System.currentTimeMillis());
		updatePosition(con, other);
	}
	
	//Movement packet for other is on b, own on a	
	public static void updatePosition(ConnectionHandler con, ConnectionHandler other) {
		Packet p = new Packet();
		p.packetType = Packet.GAME_POSITION_AND_FACING;
		p.a = false;
		p.b = con.getPlayerData().getX();
		p.c = con.getPlayerData().getY();
		p.d = con.getPlayerData().isFacingRight();
		other.getSendPacket().b = p.toJSONString();
	}
	
	public static void telportToOldPosition(ConnectionHandler con, ConnectionHandler other) {
		Packet p = new Packet();
		p.packetType = Packet.GAME_POSITION_AND_FACING;
		p.a = con.getPlayerData().getX();
		p.b = con.getPlayerData().getY();
		p.c = con.getPlayerData().isFacingRight();
		con.getSendPacket().a = p.toJSONString();
		other.getSendPacket().b = p.toJSONString();
	}
	
	public static void sendGlobalError(int errorCode) {
		Packet p = new Packet();
		p.packetType = Packet.ERROR;
		p.a = errorCode;
		Server.player1.send(p);
		Server.player2.send(p);
	}

}
