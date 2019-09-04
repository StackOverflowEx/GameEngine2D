package de.Luca.Rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import de.Luca.Models.Texture;
import de.Luca.Shader.BackgroundShader;

public class Background {
	
	private Texture tex;
	private Texture afterTex;
	private float blendFactor;
	private long initiate, duration;
	private BackgroundShader shader;
	private Texture defaultTexture;
	
	public Background(Texture texture) {
		this.tex = texture;
		this.defaultTexture = texture;
		this.afterTex = texture;
		this.shader = new BackgroundShader();
	}
	
	public Texture getDefaultTexture() {
		return defaultTexture;
	}
	
	public BackgroundShader getShader() {
		return shader;
	}
	
	public void bindTexture() {
		
		if(tex != afterTex) {
			long s = System.currentTimeMillis() - initiate;
			blendFactor = s / (float)duration;
			
			if(blendFactor >= 1) {
				blendFactor = 1;
				tex = afterTex;
			}
		}
		
		GL20.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
		if(afterTex.getTextureID() != -1) {
			GL20.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, afterTex.getTextureID());
		}
		shader.loadAfterTex(1);
		shader.loadBlendFactor(blendFactor);
		
	}
	
	public void switchTexture(Texture tex, long durationMillis) {
		initiate = System.currentTimeMillis();
		duration = durationMillis;
		afterTex = tex;
	}

}
