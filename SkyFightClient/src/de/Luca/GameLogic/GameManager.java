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
		
	private static void start() {
		listener = new GameListener();
		EventManager.registerEvent(listener);
		started = true;
		last = System.currentTimeMillis();
		SkyFightClient.mainGUI.setVisible(false);
	}
	
	public static void calcGameData() {
		if(SkyFightClient.gameState == GameState.RUNNING) {
			if(!started) {
				start();
			}
			
			float sec = (System.currentTimeMillis() - last) / 1000f;
			
			
			if(listener.getBreaking() != null) {
				listener.getBreaking().setBreakPercentage(listener.getBreaking().getBreakPercentage() + (sec / listener.getBreaking().getBlockData().getHardness()));
				
				if(listener.getBreaking().getBreakPercentage() >= 1) {
					BlockManager.removeBlock(listener.getBreaking());
					value += listener.getBreaking().getBlockData().getValue();
					System.out.println("BROKE");
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
