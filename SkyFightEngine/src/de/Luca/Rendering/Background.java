package de.Luca.Rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;
import de.Luca.Shader.BackgroundShader;

public class Background {
	
	//Der Hintergrund der Spielewelt
	
	//aktuelle Textur
	private Texture tex;
	//Textur, die angezeigt werden soll
	private Texture afterTex;
	//Factor, der benötigt wird, um die beiden Texturen im Shader zu mischen
	private float blendFactor;
	//Wann ein wechsel in Auftrag gegeben wurde und wie lange der Übergang dauern soll
	private long initiate, duration;
	//Der Shader für das Mischen der Texturen
	private BackgroundShader shader;
	private Texture defaultTexture;
	
	public Background(Texture texture) {
		this.tex = texture;
		this.defaultTexture = texture;
		this.afterTex = texture;
		this.shader = new BackgroundShader();
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjection());
		shader.stop();
	}
	
	public Texture getDefaultTexture() {
		return defaultTexture;
	}
	
	public BackgroundShader getShader() {
		return shader;
	}
	
	//Die richtige Textur wird gebunden und der Blendfactor in den Shader geladen
	public void bindTexture() {
		
		if(tex != afterTex) {
			long s = System.currentTimeMillis() - initiate;
			blendFactor = s / (float)duration;
			
			if(blendFactor >= 1) {
				blendFactor = 1;
				if(tex.getTextureID() != defaultTexture.getTextureID()) {
					Loader.destroyTexture(tex);
				}
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
	
	//ein Wechsel wird in Auftrag gegeben
	public void switchTexture(Texture tex, long durationMillis) {
		initiate = System.currentTimeMillis();
		duration = durationMillis;
		afterTex = tex;
	}

}
