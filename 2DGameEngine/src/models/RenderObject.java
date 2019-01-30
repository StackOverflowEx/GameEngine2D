package models;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class RenderObject {
	
	private TexturedModel model;
	private Vector2f position;
	private float scale;
	
	public RenderObject(TexturedModel model, Vector2f position, float scale) {
		this.model = model;
		this.position = position;
		this.scale = scale;
	}

	public TexturedModel getModel() {
		return model;
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public Matrix4f getTransformationMatrix() {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(position, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, 1f), matrix, matrix);
		return matrix;
	}
	
	public Matrix4f getFakeTransformationMatrix() {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(new Vector3f(position.x, position.y, -0.1f), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, 1f), matrix, matrix);
		return matrix;
	}

}
