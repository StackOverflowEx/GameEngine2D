package de.Luca.Networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.joml.Vector2f;
import org.json.JSONArray;
import org.json.JSONObject;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.Connection.Connection;
import de.Luca.Entities.Arrow;
import de.Luca.Entities.Entity;
import de.Luca.Entities.EntityManager;
import de.Luca.GameLogic.GameManager;
import de.Luca.GameLogic.GameState;
import de.Luca.GameLogic.PlayerCalc;
import de.Luca.GameLogic.GameManager.HOTBARSLOT;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;
import de.Luca.World.WorldLoader;

public class GameServerHandler {
	
	public static int TPS;
	private static Connection con;
	private static int highest;
	
	//setzt die Connection zum Gameserver
	public static void setConnection(Connection con) {
		GameServerHandler.con = con;
		highest = -1;
		SkyFightClient.mainGUI.setIsSearching(false, false);
	}
	
	
	//Verarbeitet Gamepackets
	public static void handlePacket(Packet packet) {
		
		//es werden immer nur die neuesten Packete akzeptiert
		
		GamePacket gp = new GamePacket(packet.toJSONString());
		System.out.println("Gamepacket RECIEVED");
		
		//Läuft das Spiel und ein Info-Pakcet wird erhalten ist dies das End-Packet
		if(gp.getGamePacketType() == GamePacket.INFO) {
			System.out.println("INFO RECIEVED");
			
			if(SkyFightClient.gameState == GameState.RUNNING) {
				//END INFO
				boolean win = Boolean.parseBoolean(gp.b.toString());
				boolean bothWin = false;
				if(gp.c != null) {
					Boolean.parseBoolean(gp.c.toString());
				}
				SkyFightClient.blockSelect.setVisible(false);
				SkyFightClient.ingameOverlay.setVisible(false);
				SkyFightClient.endGUI.setVisible(true);
				if(bothWin) {
					SkyFightClient.endGUI.showWin();
				}else {
					if(win) {
						SkyFightClient.endGUI.showWin();
					}else {
						SkyFightClient.endGUI.showLoos();
					}
				}
				SkyFightClient.gameState = GameState.MENUE;
				return;
			}
			
			//Verarbeitung des Start-Packets
			
			float x = Float.parseFloat(gp.b.toString()); //x
			float y = Float.parseFloat(gp.c.toString()); //x
			boolean facingRight = Boolean.parseBoolean(gp.d.toString()); //x
			
			SkyFightClient.p.setPosition(new Vector2f(x, y));
			SkyFightClient.p.setFacingRight(facingRight);
			
			
			float xO = Float.parseFloat(gp.e.toString()); //x
			float yO = Float.parseFloat(gp.f.toString()); //x
			boolean facingRightO = Boolean.parseBoolean(gp.g.toString()); //x
			
			SkyFightClient.pother.setPosition(new Vector2f(xO, yO));
			SkyFightClient.pother.setFacingRight(facingRightO);
			
			int tps = Integer.parseInt(gp.h.toString()); //x	
			TPS = tps;
			PlayerCalc.setOtherData(0, 0, System.currentTimeMillis(), new Vector2f(0, 0));
			
			SkyFightClient.p.setVisible(true);
			SkyFightClient.p.setFlying(false);
			SkyFightClient.p.setCollisionWithBlocks(true);
			SkyFightClient.pother.setVisible(true);
			SkyFightClient.pother.setFlying(false);
			SkyFightClient.pother.setCollisionWithBlocks(true);
			
			SkyFightClient.gameState = GameState.RUNNING;
			ServerTicker.startTicking(TPS, con);
			
		//Verarbeitung eines Positon-Packets (enthält position des Gegners, eigene Leben, Blockupdates, Pfeilupdates, Hotbarslot des Gegners)
		}else if(gp.getGamePacketType() == GamePacket.POSITION){
						
			
			if(gp.e != null) {
				processBlockChanges(gp.e.toString());
			}
			if(gp.h != null) {
				processArrowChanges(gp.h.toString());
			}
			
			if(packet.i != null && (int) packet.i < highest) {
				return;
			}
			highest = (int) packet.i;
			
			if(SkyFightClient.gameState == GameState.RUNNING) {
				float x = Float.parseFloat(gp.b.toString()); //x
				float y = Float.parseFloat(gp.c.toString().split("/")[0]); //x
				boolean facingRight = Boolean.parseBoolean(gp.d.toString().split("/")[0]);
				boolean dmg = false;
				try {
					HOTBARSLOT slot = HOTBARSLOT.valueOf(gp.d.toString().split("/")[1]);
					SkyFightClient.pother.setSelected(slot);
				}catch (ArrayIndexOutOfBoundsException e) {}
				
				
				float health = Float.parseFloat(gp.f.toString()); //health
				if(health != GameManager.getHealth()) {
					if((GameManager.getHealth() - health) == 5) {
						dmg = true;
						SkyFightClient.p.playSound(SkyFightClient.hit, 50, false);
					}
				}
				if(GameManager.getHealth() != health) {
					GameManager.setHealth(health);
				}
				
				try {
					boolean hit = Boolean.parseBoolean(gp.c.toString().split("/")[1]);
					if(hit && !dmg) {
						SkyFightClient.pother.playSound(SkyFightClient.missHit, 50, false);
					}
					if(hit) {
						SkyFightClient.pother.startAnimation(0, SkyFightClient.punchDown);
						SkyFightClient.pother.startAnimation(1, SkyFightClient.punchUp);
					}
				}catch (ArrayIndexOutOfBoundsException e) {}
				
				SkyFightClient.pother.setPosition(PlayerCalc.getSetPosOther());
								
				float xSpeed = ((x - SkyFightClient.pother.getWorldPos().x) * 1000f / (float)(1000 / TPS)) * 1;
				float ySpeed = ((y - SkyFightClient.pother.getWorldPos().y) * 1000f / (float)(1000 / TPS)) * 1;
				
				SkyFightClient.pother.setFacingRight(facingRight);
				long toFinish = System.currentTimeMillis() + (1000 / TPS);
				PlayerCalc.setOtherData(xSpeed, ySpeed, toFinish, new Vector2f(x, y));
			}
		}
		System.out.println("PACKET PROCESSED");
	}
	
	private static HashMap<Integer, ArrayList<String>> set = new HashMap<Integer, ArrayList<String>>();
	
	//verarbeitet Blockudpates
	private static void processBlockChanges(String blocks) {
		JSONArray changes = new JSONArray(blocks);
		set.clear();
		for(int i = 0; i < changes.length(); i++) {
			JSONObject blockData = changes.getJSONObject(i);
			int x = blockData.getInt("x");
			int y = blockData.getInt("y");
			float breakPercent = blockData.getFloat("breakPercent");
			
			if(set.containsKey(x) && breakPercent != 0 && breakPercent != 1) {
				if(set.get(x).contains(y + "")) {
					continue;
				}
			}
			
			if(!set.containsKey(x)) {
				ArrayList<String> newA = new ArrayList<String>();
				set.put(x, newA);
			}
			set.get(x).add(y + "");
			
			String name = blockData.getString("name");
			Block b = BlockManager.getBlock(new Vector2f(x, y));
			if(b == null || !name.equals(b.getBlockData().getName())) {
				BlockData bd = WorldLoader.getBlockData().get(name);
				if(b != null) {
					if(GameManager.getBreaking() == b) {
						GameManager.resetListener();
					}
					BlockManager.removeBlock(b);
				}
				b = new Block(bd, new Vector2f(x, y));
				BlockManager.addBlock(b);
			}
			if(breakPercent != 0) {
				if(!SkyFightClient.pother.isAnimationRunning(0) && !SkyFightClient.pother.getAnimationTitle(0).equals("hit")) {
					SkyFightClient.pother.startAnimation(0, SkyFightClient.punchDown);
					SkyFightClient.pother.startAnimation(1, SkyFightClient.punchUp);
				}
			}
			b.setBreakPercentage(breakPercent);
			if(breakPercent >= 1) {
				BlockManager.removeBlock(b);
			}
			if(b.getBlockData().getBreakSound() != null) {
				Random r = new Random();
				float ran = r.nextFloat() - 0.5f;
				b.playSound(b.getBlockData().getBreakSound(), 1 + ran, 1);
			}else {
				Random r = new Random();
				float ran = r.nextFloat() - 0.5f;
				b.playSound(SkyFightClient.breakingSound, 1 + ran, 1);
			}
		}
	}
	
	//verarbeitet Pfeilupdates
	private static void processArrowChanges(String arrows) {
		JSONArray changes = new JSONArray(arrows);
		for(int i = 0; i < changes.length(); i++) {
			JSONObject arrowData = changes.getJSONObject(i);
			float x = arrowData.getFloat("x");
			float y = arrowData.getFloat("y");
			float xVel = arrowData.getFloat("xVel");
			float yVel = arrowData.getFloat("yVel");
			boolean added = arrowData.getBoolean("add");
			String uuid = arrowData.getString("uuid");
			boolean playerHit = arrowData.getBoolean("player");
			
			System.out.println(added);
			if(added) {
				Arrow a = new Arrow(new Vector2f(x, y), SkyFightClient.arrow, yVel, xVel, SkyFightClient.pother);
				a.setUUID(uuid);
				EntityManager.addEntity(a);
			}else {
				for(Entity e : EntityManager.getEntities()) {
					if(e instanceof Arrow) {
						Arrow a = (Arrow) e;
						if(a.getUUID().toString().equals(uuid)) {
							a.setVisible(false);
							EntityManager.removeEntity(a);
							if(playerHit) {
								a.playSound(SkyFightClient.arrowHit, 50, false);
							}else {
								a.playSound(SkyFightClient.arrowHit, 50, false);
							}
						}
					}
				}
			}
		}
	}

}
