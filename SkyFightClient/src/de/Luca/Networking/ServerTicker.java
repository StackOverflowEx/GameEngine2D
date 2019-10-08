package de.Luca.Networking;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import de.Luca.Connection.Connection;
import de.Luca.GameLogic.GameManager;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;

public class ServerTicker {

	private static int TPS;
	private static Timer timer;
	private static Connection con;
	private static int counter;
	
	public static final int gracePeriode = 30;

	private static float dmgDelt, dmgTaken;
	private static ArrayList<JSONObject> blockChanges = new ArrayList<JSONObject>();
	private static ArrayList<JSONObject> arrowChanges = new ArrayList<JSONObject>();

	public static void addDmgDelt(float add) {
		if(counter < gracePeriode) {
			return;
		}
		dmgDelt += add;
	}
	
	public static void addDmgTaken(float add) {
		if(counter < gracePeriode) {
			return;
		}
		dmgTaken += add;
	}
	
	public static void addBlockChange(int x, int y, String name, float breakPercent) {
		JSONObject blockData = new JSONObject();
		blockData.put("x", x);
		blockData.put("y", y);
		blockData.put("breakPercent", breakPercent);
		blockData.put("name", name);
		blockChanges.add(blockData);
	}
	
	public static void addArrowChange(float x, float y, float xVel, float yVel, UUID uuid, boolean added) {
		JSONObject blockData = new JSONObject();
		blockData.put("x", x);
		blockData.put("y", y);
		blockData.put("xVel", xVel);
		blockData.put("yVel", yVel);
		blockData.put("add", added);
		blockData.put("uuid", uuid.toString());
		arrowChanges.add(blockData);
	}

	public static void startTicking(int tps, Connection connection) {
		System.out.println("Running with " + tps + " ticks per second");
		TPS = tps;
		con = connection;
		counter = 0;
		blockChanges.clear();
		dmgDelt = 0;
		dmgTaken = 0;
		if (timer == null) {
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
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private static void executeTick() {

		GamePacket send = new GamePacket();
		send.packetType = Packet.GAME_DATA;
		send.setGamePacketType(GamePacket.POSITION);
		send.b = SkyFightClient.p.getWorldPos().x;
		send.c = SkyFightClient.p.getWorldPos().y;
		send.d = SkyFightClient.p.isFacingRight();

		JSONArray changes = new JSONArray();
		if (GameManager.getBreaking() != null) {
			addBlockChange((int) GameManager.getBreaking().getWorldPos().x,
					(int) GameManager.getBreaking().getWorldPos().y, GameManager.getBreaking().getBlockData().getName(),
					GameManager.getBreaking().getBreakPercentage());
		}
		for (JSONObject blockData : blockChanges) {
			changes.put(blockData);
		}
		send.e = changes.toString();
		
		JSONArray arrowChange = new JSONArray();
		for (JSONObject arrowData : arrowChanges) {
			arrowChange.put(arrowData);
		}
		send.h = arrowChange.toString();
		
		send.f = dmgDelt;
		send.g = dmgTaken;
		send.i = counter;
		counter++;
		blockChanges.clear();
		arrowChanges.clear();
		dmgDelt = 0;
		dmgTaken = 0;

		if (con != null && con.isConnected() && con.finishedHandshaking()) {
			con.send(send);
		} else {
			stopTicking();
		}
	}

}
