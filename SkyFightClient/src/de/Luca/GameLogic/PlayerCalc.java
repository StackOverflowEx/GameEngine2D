package de.Luca.GameLogic;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Calculation.Camera;
import de.Luca.Entities.Player;
import de.Luca.Main.SkyFightClient;
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
	
	public static void calc() {
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
			if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
				if(p.isOnGround()) {
					upSpeed = 4.5f;
				}
			}
			if(!p.isOnGround() || upSpeed > 0) {
				upSpeed -= sec * GRAVITY;
			}else if(upSpeed < 0){
				upSpeed = 0;
			}
			addY += sec * upSpeed;
		}
		p.move(new Vector2f(addX, addY));
		
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
	
	public static void setOtherData(float xSpeedPerSec, float ySpeedPerSec, long finishedMove, Vector2f set) {
		xSpeedOther = xSpeedPerSec;
		ySpeedOther = ySpeedPerSec;
		finishedOtherMove = finishedMove;
		setPos = set;
	}
	
	public static void tpOtherToExact() {
		if(setPos == null) {
			return;
		}
		Vector2f move = new Vector2f(setPos.x - SkyFightClient.pother.getWorldPos().x, setPos.y - SkyFightClient.pother.getWorldPos().y);
		SkyFightClient.pother.move(move);
	}
	
	private static void calcOther(float sec) {
		if(finishedOtherMove > System.currentTimeMillis()) {
			return;
		}
		float addX = xSpeedOther * sec;
		float addY = ySpeedOther * sec;
		SkyFightClient.pother.move(new Vector2f(addX, addY));
	}
		
	public Player getPlayer() {
		return p;
	}

}
