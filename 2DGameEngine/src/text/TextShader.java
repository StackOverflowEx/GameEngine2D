package text;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import rendering.ShaderProgramm;

public class TextShader extends ShaderProgramm{
	
	private int location_projectionMatrix;
	private int location_color;
	private int location_transformationMatrix;

	public TextShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_color = super.getUniformLocation("color");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f m) {
		super.loadMatrix(location_transformationMatrix, m);
	}
	
	public void loadProjectionMatrix(Matrix4f m) {
		super.loadMatrix(location_projectionMatrix, m);
	}

	public void loadColor(Vector3f color) {
		super.loadVector(location_color, color);
	}
	
}
