package de.Luca.Shader;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import de.Luca.Main.SkyFightEngine;


public class GUIShader extends ShaderProgramm{
	
	//Shader f�r die GUIs

	private int location_projectionMatrix;
	private int location_transformationMatrix;
	private int location_color;
	
	public GUIShader() {
		super(SkyFightEngine.class.getResourceAsStream("/de/Luca/Shader/vertexGUI.glsl"), SkyFightEngine.class.getResourceAsStream("/de/Luca/Shader/fragmentGUI.glsl"));
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
		location_color = getUniformLocation("color");	
	}

	public void loadProjectionMatrix(Matrix4f mat) {
		loadMatrix(location_projectionMatrix, mat);
	}
	
	public void loadTransformationMatrix(Matrix4f mat) {
		loadMatrix(location_transformationMatrix, mat);
	}
	
	public void loadColor(Vector4f color) {
		load4DVector(location_color, color);
	}
	
}
