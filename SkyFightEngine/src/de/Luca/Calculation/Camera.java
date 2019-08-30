package de.Luca.Calculation;

import org.joml.Vector2f;

public class Camera {
	
	private static Vector2f position = new Vector2f(0, 0);
	private static float roll = 0;
	private static boolean moved = false;;
	
	public static void move(float dx, float dy) {
		position.x += dx;
		position.y += dy;
		Calc.calcViewMatrix();
		moved = true;
	}
	
	public static void roll(float droll) {
		roll += droll;
		Calc.calcViewMatrix();
		moved = true;
	}
	
	public static float getRoll() {
		return roll;
	}
	
	public static Vector2f getPosition() {
		return position;
	}
	
	public static boolean hasMoved() {
		return moved;
	}

}
