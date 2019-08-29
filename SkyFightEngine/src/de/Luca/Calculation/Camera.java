package de.Luca.Calculation;

import org.joml.Vector2f;

public class Camera {
	
	public static Vector2f position = new Vector2f(0, 0);
	public static float roll = 0;
	
	public static void move(float dx, float dy) {
		position.x += dx;
		position.y += dy;
	}
	
	public static void roll(float droll) {
		roll += droll;
	}

}
