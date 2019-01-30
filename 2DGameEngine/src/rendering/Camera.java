package rendering;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private static Vector2f position = new Vector2f(0, 0);
	private static float roll = 0;
	private static float zoom = 1.0f;

	public static Vector2f getPosition() {
		return position;
	}

	public static float getRoll() {
		return roll;
	}

	public static float getZoom() {
		return zoom;
	}

	public static void move(Vector2f add) {
		position.x += add.x;
		position.y += add.y;
	}

	public static void roll(float deg) {
		roll = deg;
	}

	public static void addRoll(float add) {
		roll += add;
	}

	public static void setZoom(float z) {
		zoom = z;
	}

	public static void changeZoom(float add) {
		zoom += add;
	}

	public static Matrix4f getViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector2f negativeCameraPos = new Vector2f(-position.x, -position.y);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
}
