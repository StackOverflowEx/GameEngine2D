package de.Luca.GameLogic;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockManager;
import de.Luca.EventManager.EventManager;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.HandelServerPacketHandler;
import de.Luca.Networking.ServerTicker;

public class GameManager {
	
	private static GameListener listener = null;
	private static long last;
	private static boolean started = false;
	
	private static float health;
	private static float value;
		
	private static void start() {
		value = 0;
		health = 100;
		listener = new GameListener();
		EventManager.registerEvent(listener);
		started = true;
		last = System.currentTimeMillis();
		SkyFightClient.mainGUI.setVisible(false);
		SkyFightClient.ingameOverlay.setUsername(HandelServerPacketHandler.username);
		SkyFightClient.ingameOverlay.setHealth(health);
		SkyFightClient.ingameOverlay.setCoins(value);
		SkyFightClient.ingameOverlay.setUsername(HandelServerPacketHandler.username);
		SkyFightClient.ingameOverlay.setVisible(true);
	}
	
	public enum HOTBARSLOT{
		SWORD,
		PICKAXE,
		BLOCK,
		BOW
	}
	
	public static boolean isShooting(){
		if(listener != null) {
			return listener.isShooting();
		}
		return false;
	}
	
	public static void setHealth(float health) {
		GameManager.health = health;
		SkyFightClient.ingameOverlay.setHealth(GameManager.health);
	}
	
	public static float getHealth() {
		return health;
	}
	
	public static void calcGameData() {
		if(SkyFightClient.gameState == GameState.RUNNING) {
			if(!started) {
				start();
			}
			
			float sec = (System.currentTimeMillis() - last) / 1000f;
			
			
			if(listener.getBreaking() != null) {
				float multiplyer = 1;
				if(SkyFightClient.ingameOverlay.getSelectedSlot() != HOTBARSLOT.PICKAXE) {
					multiplyer = 0.5f;
				}
				listener.getBreaking().setBreakPercentage(listener.getBreaking().getBreakPercentage() + multiplyer * (sec / listener.getBreaking().getBlockData().getHardness()));
				
				if(listener.getBreaking().getBreakPercentage() >= 1) {
					BlockManager.removeBlock(listener.getBreaking());
					setValue(getValue() + listener.getBreaking().getBlockData().getValue());
					Block b = listener.getBreaking();
					listener.reset();
					ServerTicker.addBlockChange((int)b.getWorldPos().x, (int)b.getWorldPos().y, b.getBlockData().getName(), 1f);
				}
			}
			
			
			last = System.currentTimeMillis();
		}else {
			reset();
		}
	}
	
	public static float getValue() {
		return value;
	}
	
	public static void setValue(float value) {
		GameManager.value = value;
		SkyFightClient.ingameOverlay.setCoins(value);
	}
	
	public static void resetListener() {
		listener.reset();
	}
	
	public static Block getBreaking() {
		if(listener != null) {
			return listener.getBreaking();
		}
		return null;
	}
	
	public static void reset() {
		if(listener != null) {
			listener.reset();
			EventManager.removeListener(listener);
			listener = null;
		}
		started = false;
		value = 0;
		
	}

}
