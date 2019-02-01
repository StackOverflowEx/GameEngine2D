package tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {

	public static Matrix4f getTransformationMatrix(Vector2f pos, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		Matrix4f.translate(pos, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	
	
}
