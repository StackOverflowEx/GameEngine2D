package de.Luca.Shader;

import org.joml.Matrix4f;

import de.Luca.Main.SkyFightEngine;

public class BackgroundShader extends ShaderProgramm{

	private int location_projectionMatrix;
	private int location_blendFactor;
	private int location_afterTex;
	
	public BackgroundShader() {
		super(SkyFightEngine.class.getResourceAsStream("/de/Luca/Shader/vertexBackground.glsl"), SkyFightEngine.class.getResourceAsStream("/de/Luca/Shader/fragmentBackground.glsl"));
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_blendFactor = getUniformLocation("blendFactor");	
		location_afterTex = getUniformLocation("afterTex");
	}

	public void loadProjectionMatrix(Matrix4f mat) {
		loadMatrix(location_projectionMatrix, mat);
	}
	
	public void loadBlendFactor(float factor) {
		loadFloat(location_blendFactor, factor);
	}
	
	public void loadAfterTex(int texUnit) {
		loadInt(location_afterTex, texUnit);
	}
	
}
