package de.Luca.GameLogic;

import java.util.Random;

import org.joml.Vector2f;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.Effects.Effect;
import de.Luca.EventManager.EventManager;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.HandelServerPacketHandler;
import de.Luca.Networking.ServerTicker;

public class GameManager {
	
	private static GameListener listener = null;
	private static long last;
	private static boolean started = false;
	private static long startTime;
	
	private static float health;
	private static float value;
			
	private static void start() {
		startTime = System.currentTimeMillis();
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
		if(SkyFightClient.gameState != GameState.RUNNING) {
			return;
		}
		if(GameManager.health != health) {
			Effect e = new Effect(SkyFightClient.gettingHit, new Vector2f(SkyFightClient.p.getWorldPos()).add(0.5f, 1f), new Vector2f(BlockData.BLOCK_SCALE * 2.5f, BlockData.BLOCK_SCALE * 2.5f));
			e.setWorldPos(new Vector2f(SkyFightClient.p.getWorldPos()).add(-0.5f, -1f));
			e.play();
		}
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
			
			String time = "";
			float delta = System.currentTimeMillis() - startTime;
			delta = 1000*60*15 - delta;
			int seconds = (int) (delta / 1000);
			int min = seconds / 60;
			seconds = seconds - min * 60;
			
			time = min + ":" + seconds;
			if(min < 10) {
				time = "0" + time;
			}
			if(seconds < 10) {
				time = time.replace(":", ":0");
			}			
			if(delta < 0) {
				time = "00:00";
			}
			SkyFightClient.ingameOverlay.setTime(time);
			
			if((System.currentTimeMillis() - startTime) > 1000 * 60 * 15) {
				float dmg = last / 10;
				ServerTicker.addDmgTaken(dmg);
			}
			
			float sec = (System.currentTimeMillis() - last) / 1000f;
			
			
			if(listener.getBreaking() != null) {
				float multiplyer = 1;
				if(SkyFightClient.ingameOverlay.getSelectedSlot() != HOTBARSLOT.PICKAXE) {
					multiplyer = 0.5f;
				}
				listener.getBreaking().setBreakPercentage(listener.getBreaking().getBreakPercentage() + multiplyer * (sec / listener.getBreaking().getBlockData().getHardness()));
				if(!SkyFightClient.p.isAnimationRunning(0) && !SkyFightClient.p.getAnimationTitle(0).equals("hit")) {
					SkyFightClient.p.startAnimation(0, SkyFightClient.punchDown);
					SkyFightClient.p.startAnimation(1, SkyFightClient.punchUp);
				}
				
				Random r = new Random();
				float ran = r.nextFloat() - 0.5f;
				if(listener.getBreaking().getBlockData().getBreakSound() != null) {
					listener.getBreaking().playSound(listener.getBreaking().getBlockData().getBreakSound(), 1 + ran, 1);
				}else {
					listener.getBreaking().playSound(SkyFightClient.breakingSound, 1 + ran, 1);
				}
				
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
