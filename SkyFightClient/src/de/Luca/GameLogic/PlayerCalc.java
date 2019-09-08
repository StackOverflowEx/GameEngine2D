package de.Luca.GameLogic;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Calculation.Camera;
import de.Luca.Entities.Player;
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
		Camera.setPos(p.getModels()[1].getLocation().x, p.getModels()[1].getLocation().y);
		lastCalc = System.currentTimeMillis();
	}
		
	public Player getPlayer() {
		return p;
	}

}
