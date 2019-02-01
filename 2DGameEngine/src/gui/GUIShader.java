package gui;

import org.lwjgl.util.vector.Matrix4f;

import rendering.ShaderProgramm;

public class GUIShader extends ShaderProgramm{
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;

	public GUIShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f m) {
		super.loadMatrix(location_transformationMatrix, m);
	}
	
	public void loadProjectionMatrix(Matrix4f m) {
		super.loadMatrix(location_projectionMatrix, m);
	}

}
