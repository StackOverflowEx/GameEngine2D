package de.Luca.Networking;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.joml.Vector2f;
import org.json.JSONArray;
import org.json.JSONObject;

import de.Luca.Blocks.BlockData;
import de.Luca.Connection.Connection;
import de.Luca.Effects.Effect;
import de.Luca.GameLogic.GameManager;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;

public class ServerTicker {
	
	
	//Ticks pro Sekunde
	private static int TPS;
	private static Timer timer;
	//Connection zum GameServer
	private static Connection con;
	//cointer
	private static int counter;
	//boolean, ob der Spieler geschlagen hat
	private static boolean hit;
	
	//wie viele Packets Friedensphase
	public static final int gracePeriode = 30;

	private static float dmgDelt, dmgTaken;
	private static ArrayList<JSONObject> blockChanges = new ArrayList<JSONObject>();
	private static ArrayList<JSONObject> arrowChanges = new ArrayList<JSONObject>();

	//Es wird gespeichert, ob dem Gegner Schaden zugefügt wurde
	public static void addDmgDelt(float add) {
		if(counter < gracePeriode) {
			return;
		}
		//ein Bluteffekt wird abgespielt
		Effect e = new Effect(SkyFightClient.gettingHit, new Vector2f(SkyFightClient.pother.getWorldPos()).add(0.5f, 1f), new Vector2f(BlockData.BLOCK_SCALE * 2.5f, BlockData.BLOCK_SCALE * 2.5f));
		e.setWorldPos(new Vector2f(SkyFightClient.pother.getWorldPos()).add(-0.5f, -1f));
		e.play();
		dmgDelt += add;
	}
	
	public static void addHit() {
		hit = true;
	}
	
	//es wird gespeichert, ob der Spieler schaden erhalten hat
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
	
	public static void addArrowChange(float x, float y, float xVel, float yVel, UUID uuid, boolean added, boolean isPlayerHit) {
		JSONObject blockData = new JSONObject();
		blockData.put("x", x);
		blockData.put("y", y);
		blockData.put("xVel", xVel);
		blockData.put("yVel", yVel);
		blockData.put("add", added);
		blockData.put("uuid", uuid.toString());
		blockData.put("player", isPlayerHit);
		arrowChanges.add(blockData);
	}

	//der Ticker wird gestartet und es wird TPS-mal in der Sekunde ein Packet an den Server gesendet
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

	//der Ticker wird beendet
	public static void stopTicking() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	//es wird ein Gamepacket erstellt und an den Server gesendet.
	private static void executeTick() {
		
		GamePacket send = new GamePacket();
		send.packetType = Packet.GAME_DATA;
		send.setGamePacketType(GamePacket.POSITION);
		send.b = SkyFightClient.p.getWorldPos().x;
		send.c = SkyFightClient.p.getWorldPos().y + "/" + hit;
		send.d = SkyFightClient.p.isFacingRight() + "/" + SkyFightClient.ingameOverlay.getSelectedSlot().toString();

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
		hit = false;
		dmgDelt = 0;
		dmgTaken = 0;

		if (con != null && con.isConnected() && con.finishedHandshaking()) {
			con.send(send);
		} else {
			stopTicking();
		}
	}

}
