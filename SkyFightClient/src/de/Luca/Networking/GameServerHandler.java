package de.Luca.Networking;

import org.joml.Vector2f;
import org.json.JSONArray;
import org.json.JSONObject;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.Connection.Connection;
import de.Luca.GameLogic.GameState;
import de.Luca.GameLogic.PlayerCalc;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;
import de.Luca.World.WorldLoader;

public class GameServerHandler {
	
	public static int TPS;
	private static Connection con;
	
	public static void setConnection(Connection con) {
		GameServerHandler.con = con;
	}
	
	public static void handlePacket(Packet packet) {
		GamePacket gp = new GamePacket(packet.toJSONString());
		
		if(gp.getGamePacketType() == GamePacket.INFO) {
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
			
			SkyFightClient.gameState = GameState.RUNNING;
			ServerTicker.startTicking(TPS, con);
		}else if(gp.getGamePacketType() == GamePacket.POSITION){
			if(SkyFightClient.gameState == GameState.RUNNING) {
				float x = Float.parseFloat(gp.b.toString()); //x
				float y = Float.parseFloat(gp.c.toString()); //x
				boolean facingRight = Boolean.parseBoolean(gp.d.toString()); //x
				
//				Vector2f move = new Vector2f(x - SkyFightClient.pother.getWorldPos().x, y - SkyFightClient.pother.getWorldPos().y);
//				SkyFightClient.pother.move(move);
//				PlayerCalc.tpOtherToExact();
				if(gp.e != null) {
					processBlockChanges(gp.e.toString());
				}
				
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
			String name = blockData.getString("name");
			Block b = BlockManager.getBlock(new Vector2f(x, y));
			if(b == null || !name.equals(b.getBlockData().getName())) {
				BlockData bd = WorldLoader.getBlockData().get(name);
				if(b != null) {
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

}
