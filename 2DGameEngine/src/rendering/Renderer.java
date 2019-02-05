package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import models.Texture;

public abstract class Renderer {
	
	
	protected abstract void render();
	
	protected abstract void cleanUP();
	
	//dose not load uniforms
	public void bindModel(RawModel model, int[] enabledAttrib) {
		GL30.glBindVertexArray(model.getVaoID());
		for(int i : enabledAttrib) {
			GL30.glEnableVertexAttribArray(i);
		}
	}
	
	//GL13.GL_TEXTURE0, ...
	public void bindTexture(Texture tex, int unit) {
		GL20.glActiveTexture(unit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
	}
	
	public void bindTexture(int tex, int unit) {
		GL20.glActiveTexture(unit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
	}
	
	public void drawSTRIP(RawModel model) {
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getVertexCount());
	}
	
	public void draw(RawModel model) {
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	public void drawArrays(RawModel model) {
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
	}
	
	public void unbind(int[] enabledAttrib) {
		for(int i : enabledAttrib) {
			GL30.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
	}

}
