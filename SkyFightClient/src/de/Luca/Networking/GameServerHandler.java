package de.Luca.Networking;

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
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;
import de.Luca.World.WorldLoader;

public class GameServerHandler {
	
	public static int TPS;
	private static Connection con;
	private static int highest;
	
	public static void setConnection(Connection con) {
		GameServerHandler.con = con;
		highest = -1;
		SkyFightClient.mainGUI.setIsSearching(false, false);
	}
	
	public static void handlePacket(Packet packet) {
		
		if(packet.i != null && (int) packet.i < highest) {
			return;
		}
		highest = (int) packet.i;
		
		GamePacket gp = new GamePacket(packet.toJSONString());
		
		if(gp.getGamePacketType() == GamePacket.INFO) {
			
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
			
		}else if(gp.getGamePacketType() == GamePacket.POSITION){
			if(SkyFightClient.gameState == GameState.RUNNING) {
				float x = Float.parseFloat(gp.b.toString()); //x
				float y = Float.parseFloat(gp.c.toString()); //x
				boolean facingRight = Boolean.parseBoolean(gp.d.toString()); //x
				
				float health = Float.parseFloat(gp.f.toString()); //health
				GameManager.setHealth(health);
				
				if(gp.e != null) {
					processBlockChanges(gp.e.toString());
				}
				if(gp.h != null) {
					processArrowChanges(gp.h.toString());
				}
				
				SkyFightClient.pother.setPosition(PlayerCalc.getSetPosOther());
								
				float xSpeed = ((x - SkyFightClient.pother.getWorldPos().x) * 1000f / (float)(1000 / TPS)) * 1;
				float ySpeed = ((y - SkyFightClient.pother.getWorldPos().y) * 1000f / (float)(1000 / TPS)) * 1;
				
				SkyFightClient.pother.setFacingRight(facingRight);
				long toFinish = System.currentTimeMillis() + (1000 / TPS);
				PlayerCalc.setOtherData(xSpeed, ySpeed, toFinish, new Vector2f(x, y));
			}
		}
	}
	
	private static void processBlockChanges(String blocks) {
		JSONArray changes = new JSONArray(blocks);
		for(int i = 0; i < changes.length(); i++) {
			JSONObject blockData = changes.getJSONObject(i);
			int x = blockData.getInt("x");
			int y = blockData.getInt("y");
			float breakPercent = blockData.getFloat("breakPercent");
			System.out.println("BREAK: " + breakPercent);
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
			b.setBreakPercentage(breakPercent);
			if(breakPercent >= 1) {
				BlockManager.removeBlock(b);
			}
		}
	}
	
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
			
			if(added) {
				Arrow a = new Arrow(new Vector2f(x, y), Loader.loadTexture("D:/Test.png", "arrow"), yVel, xVel, SkyFightClient.pother);
				EntityManager.addEntity(a);
			}else {
				for(Entity e : EntityManager.getEntities()) {
					if(e instanceof Arrow) {
						Arrow a = (Arrow) e;
						if(a.getUUID().toString().equals(uuid)) {
							a.setVisible(false);
							EntityManager.removeEntity(a);
						}
					}
				}
			}
		}
	}

}
