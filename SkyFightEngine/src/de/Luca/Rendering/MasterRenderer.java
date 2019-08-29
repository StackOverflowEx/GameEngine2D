package de.Luca.Rendering;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.Luca.Calculation.Calc;
import de.Luca.Entities.Entity;
import de.Luca.Entities.Texture;
import de.Luca.GUI.NukManager;
import de.Luca.Loading.Frame;
import de.Luca.Loading.Loader;
import de.Luca.Shader.EntityShader;

public class MasterRenderer extends Thread{
	
	public static MasterRenderer masterRenderer;
	private EntityShader shader;
	private Frame frame;
	private Frame queuedFrame;
	private ArrayList<Texture> loadTextures;
	
	public MasterRenderer(RenderLoop loop) {
		super(loop);
		if(masterRenderer != null) {
			throw new IllegalStateException("MasterRenderer already created");
		}
		GLFW.glfwMakeContextCurrent(0);
		setName("Rendering Thread");
		masterRenderer = this;
		loadTextures = new ArrayList<Texture>();;
	}
	
	public void queueFrame(Frame frame) {
		if(queuedFrame != null) {
			synchronized (this.queuedFrame) {
				this.queuedFrame = frame;
			}
		}else {
			this.queuedFrame = frame;
		}
	}
	
	public void queueTexture(Texture texture) {
		loadTextures.add(texture);
	}
	
	private void loadTextures() {
		synchronized (loadTextures) {
			for(Texture texture : loadTextures) {
				System.out.println("Lading texture");
				int id = Loader.loadTexture(texture.getBuffer(), texture.getWidth(), texture.getHeight());
				texture.setTextureID(id);
			}
			loadTextures.clear();
		}
	}
	
	private void renderGUI() {
		if(NukManager.nukManager == null) {
			new NukManager();
		}
		NukManager.nukManager.render();
	}
	
	public synchronized void render() {
		loadTextures();
		swapFrames();
		if(shader == null) {
			shader = new EntityShader();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 1, 1, 1);
		
		renderGUI();
		
		if(frame != null) {
			shader.start();
			bindModel();
			
			Matrix4f viewMatrix = Calc.calcViewMatrix();
			Matrix4f projectionMatrix = Calc.getProjectionMatrix();
			shader.loadProjectionMatrix(projectionMatrix);
			shader.loadViewMatrix(viewMatrix);

			for(int i = 0; i < frame.getBufferedEntities(); i++) {
				Entity entity = frame.getEntities().get(i);
				if(entity.getModel().getTexture().getTextureID() != -1) {
					bindTexture(entity.getModel().getTexture().getTextureID());
					Matrix4f transformationMatrix = Calc.getTransformationMatrix(entity.getLocation(), new Vector2f(entity.getModel().getScale(), entity.getModel().getScale()), entity.getRoll());
					shader.loadTransformationMatrix(transformationMatrix);
					drawVAO();
				}
			}
			
			shader.stop();
			unbind();
		}
	}
	
	public void swapFrames() {
		if(queuedFrame != null) {
			frame = queuedFrame;
			queuedFrame = null;
		}
	}
	
	public void drawVAO() {
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 8);
	}
	
	public void bindModel() {
		GL30.glBindVertexArray(Loader.getQuadVAO());
		GL30.glEnableVertexAttribArray(0);
	}
	
	//GL13.GL_TEXTURE0, ...
	public void bindTexture(int id) {
		GL20.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public void unbind() {
		GL30.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
