package de.Luca.GameLogic;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockManager;
import de.Luca.EventManager.EventManager;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.ServerTicker;

public class GameManager {
	
	private static GameListener listener = null;
	private static long last;
	private static boolean started = false;
	
	private static float value;
	
	public static void calcGameData() {
		if(SkyFightClient.gameState == GameState.RUNNING) {
			if(!started) {
				listener = new GameListener();
				EventManager.registerEvent(listener);
				started = true;
				last = System.currentTimeMillis();
			}
			
			float sec = (System.currentTimeMillis() - last) / 1000f;
			
			
			if(listener.getBreaking() != null) {
				listener.getBreaking().setBreakPercentage(listener.getBreaking().getBreakPercentage() + (sec / listener.getBreaking().getBlockData().getHardness()));
				
				if(listener.getBreaking().getBreakPercentage() >= 1) {
					BlockManager.removeBlock(listener.getBreaking());
					value += listener.getBreaking().getBlockData().getValue();
					ServerTicker.addBlockChange((int)listener.getBreaking().getWorldPos().x, (int)listener.getBreaking().getWorldPos().y, listener.getBreaking().getBlockData().getName(), 1f);
					listener.reset();
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
