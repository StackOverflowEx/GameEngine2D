package de.Luca.GameLogic;

import java.util.Random;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Calculation.Camera;
import de.Luca.Entities.Player;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.ServerTicker;
import de.Luca.Utils.DefaultKeyListener;

public class PlayerCalc {
	
	private static Player p;
	private static float upSpeed;
	private static final float GRAVITY = 9.81f;
	private static long lastCalc;
		
	public static void init(Player p) {
		PlayerCalc.p = p;
		upSpeed = 0;
		lastCalc = -1;
	}
	
	//berechnet die Position des Spielers und des Gegners, falls vorhanden
	public static void calc() {
		
		if(SkyFightClient.gameState != GameState.RUNNING && SkyFightClient.gameState != GameState.WORLDEDITOR) {
			lastCalc = System.currentTimeMillis();
			return;
		}
		
		float addX = 0, addY = 0;
		float sec = (System.currentTimeMillis() - lastCalc) / 1000f;
		if(lastCalc == -1) {
			lastCalc = System.currentTimeMillis();
			return;
		}
		if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
			addX -= sec * 5;
		}
		if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
			addX += sec * 5;
		}
				
		if(p.isFlying()) {
			upSpeed = 0;
			if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
				addY += sec * 5;
			}
			if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				addY -= sec * 5;
			}
		}else {
			if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE) && !GameManager.isShooting()) {
				if(p.isOnGround()) {
					upSpeed = 4.5f;
				}
			}
			if(!p.isOnGround() || upSpeed > 0) {
				upSpeed -= sec * GRAVITY;
			}else if(upSpeed < 0){
				//Der Spieler erhält Fallschaden, wenn die y-Geschwindigkeit mehr als 10 Blöcke nach Unten pro Sekunde beträgt.
				if(upSpeed < -10) {
					float fallDmg = Math.abs(upSpeed) * 3;
					fallDmg = Math.round(fallDmg * 10f) / 10f;
					GameManager.setHealth(GameManager.getHealth() - fallDmg);
					ServerTicker.addDmgTaken(fallDmg);
				}
				upSpeed = 0;
			}
			addY += sec * upSpeed;
		}
		//schießt der Spieler, kann er sich nur halbso schnell bewegen
		if(GameManager.isShooting()) {
			addX = addX/2f;
		}
		
		//Bewegt sich der Spieler, wird ein Sound abgespielt und eine Laufanimation abgespielt
		if(addX != 0) {
			SkyFightClient.walking.setPosition(SkyFightClient.p.getWorldPos());
			if(!SkyFightClient.walking.isPlaying() && p.isOnGround() && !p.isFlying()) {
				Random r = new Random();
				float ran = r.nextFloat() - 0.5f;
				SkyFightClient.walking.setPitch(1-ran);
				SkyFightClient.walking.playSound(SkyFightClient.footstep);
			}
			
			if(!p.isOnGround() && !p.isFlying()) {
				try {
					if(SkyFightClient.p.getAnimationTitle(0).equals("run")) {
						SkyFightClient.p.stopAnimation(0);
						SkyFightClient.p.stopAnimation(1);
					}
				}catch (NullPointerException e) {}
			}else {
				if(!SkyFightClient.p.isAnimationRunning(0)) {
					SkyFightClient.p.startAnimation(0, SkyFightClient.downRun);
				}
				if(!SkyFightClient.p.isAnimationRunning(1)) {
					SkyFightClient.p.startAnimation(1, SkyFightClient.upRun);
				}
			}
		}else {
			//Steht der Spieler, wird die Laufanimation abgebrochen
			try {
				if(sec > 0 && SkyFightClient.p.getAnimationTitle(0).equals("run")) {
					SkyFightClient.p.stopAnimation(0);
					SkyFightClient.p.stopAnimation(1);
				}
			}catch (NullPointerException e) {}
		}
		
		p.move(new Vector2f(addX, addY));
		
		//ist der Spieler unter der y-Koordinate -100 stirbt dieser sofort
		if(p.getWorldPos().y < -100) {
			GameManager.setHealth(0);
			ServerTicker.addDmgTaken(100);		
		}
		
		if(addX > 0) {
			p.setFacingRight(true);
		}else if(addX < 0){
			p.setFacingRight(false);
		}
		
		calcOther(sec);
		
		Camera.setPos(p.getModels()[1].getLocation().x, p.getModels()[1].getLocation().y);
		lastCalc = System.currentTimeMillis();
	}
	
	private static float xSpeedOther, ySpeedOther;
	private static long finishedOtherMove;
	private static Vector2f setPos;
	
	public static Vector2f getSetPosOther() {
		return setPos;
	}
	
	//Daten, wie der andere Spieler bewegt werden soll und wo hin er soll
	public static void setOtherData(float xSpeedPerSec, float ySpeedPerSec, long finishedMove, Vector2f set) {
		xSpeedOther = xSpeedPerSec;
		ySpeedOther = ySpeedPerSec;
		finishedOtherMove = finishedMove;
		setPos = set;
	}
	
	//teleportiert den Gegner zu der exakten Position
	public static void tpOtherToExact() {
		if(setPos == null) {
			return;
		}
		Vector2f move = new Vector2f(setPos.x - SkyFightClient.pother.getWorldPos().x, setPos.y - SkyFightClient.pother.getWorldPos().y);
		SkyFightClient.pother.move(move);
	}
	
	//Die Position für den Gegner wird berechnet
	private static void calcOther(float sec) {
		if(finishedOtherMove > System.currentTimeMillis()) {
			return;
		}
		float addX = xSpeedOther * sec;
		float addY = ySpeedOther * sec;
		
		//siehe oben (Abspielen einer Animation/Sound beim Bewegen)
		if(addX != 0) {
			SkyFightClient.walkingOther.setPosition(SkyFightClient.pother.getWorldPos());
			if(!SkyFightClient.walkingOther.isPlaying() && SkyFightClient.pother.isOnGround()) {
				Random r = new Random();
				float ran = r.nextFloat() - 0.5f;
				SkyFightClient.walkingOther.setPitch(1-ran);
				SkyFightClient.walkingOther.playSound(SkyFightClient.footstep);
			}
			
			if(!SkyFightClient.pother.isOnGround()) {
				try {
					if(SkyFightClient.pother.getAnimationTitle(0).equals("run")) {
						SkyFightClient.pother.stopAnimation(0);
						SkyFightClient.pother.stopAnimation(1);
					}
				}catch (NullPointerException e) {}
			}else {
				if(!SkyFightClient.pother.isAnimationRunning(0)) {
					SkyFightClient.pother.startAnimation(0, SkyFightClient.downRun);
				}
				if(!SkyFightClient.pother.isAnimationRunning(1)) {
					SkyFightClient.pother.startAnimation(1, SkyFightClient.upRun);
				}
			}			
		}
		
		SkyFightClient.pother.move(new Vector2f(addX, addY));
	}
		
	public Player getPlayer() {
		return p;
	}

}
