package de.Luca.Shader;

import org.joml.Matrix4f;

import de.Luca.Main.Main;

public class EntityShader extends ShaderProgramm{

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_transformationMatrix;
	
	public EntityShader() {
		super(Main.class.getResource("/de/Luca/Shader/vertex.glsl").getFile(), Main.class.getResource("/de/Luca/Shader/fragment.glsl").getFile());
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
	}

	public void loadProjectionMatrix(Matrix4f mat) {
		loadMatrix(location_projectionMatrix, mat);
	}
	
	public void loadTransformationMatrix(Matrix4f mat) {
		loadMatrix(location_transformationMatrix, mat);
	}
	
	public void loadViewMatrix(Matrix4f mat) {
		loadMatrix(location_viewMatrix, mat);
	}
	
}
