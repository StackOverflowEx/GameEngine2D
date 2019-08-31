package de.Luca.Rendering;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.Luca.Calculation.Calc;
import de.Luca.Entities.Entity;
import de.Luca.Entities.Texture;
import de.Luca.Loading.Frame;
import de.Luca.Loading.Loader;
import de.Luca.Shader.EntityShader;
import de.Luca.Text.Text;
import de.Luca.Text.TextManager;
import de.Luca.Window.Window;

public class MasterRenderer extends Thread {

	public static MasterRenderer masterRenderer;
	private EntityShader shader;
	private Frame frame;
	private Frame queuedFrame;
	private ArrayList<Texture> loadTextures;

	public MasterRenderer(RenderLoop loop) {
		super(loop);
		if (masterRenderer != null) {
			throw new IllegalStateException("MasterRenderer already created");
		}
		GLFW.glfwMakeContextCurrent(0);
		setName("Rendering Thread");
		masterRenderer = this;
		loadTextures = new ArrayList<Texture>();
		;
	}

	public void queueFrame(Frame frame) {
		if (queuedFrame != null) {
			synchronized (this.queuedFrame) {
				this.queuedFrame = frame;
			}
		} else {
			this.queuedFrame = frame;
		}
	}

	public void queueTexture(Texture texture) {
		loadTextures.add(texture);
	}

	private void loadTextures() {
		synchronized (loadTextures) {
			for (Texture texture : loadTextures) {
				System.out.println("Loading texture");
				int id = Loader.loadTexture(texture.getBuffer(), texture.getWidth(), texture.getHeight());
				texture.setTextureID(id);
			}
			loadTextures.clear();
		}
	}

//	private void renderGUI() {
//		if (NukManager.nukManager == null) {
//			new NukManager();
//		}
//		NukManager.nukManager.render();
//	}
	
	private void renderGUI() {

		if (TextManager.manager == null) {
			new TextManager();
			TextManager.manager.generateFont("C:\\Windows\\Fonts\\Arial.ttf", 40f, "Arial");
			TextManager.manager.addText(new Text(TextManager.manager.getFont("Arial"), (int)(Window.window.getWindowSize().x / 2f), (int)(Window.window.getWindowSize().y / 2f), "Hello, World!", new Vector4f(0, 0, 0, 1)));
		}
		TextManager.manager.render();
//		NukManager.nukManager.render();
		
	}

	public synchronized void render() {
		loadTextures();
		swapFrames();
		if (shader == null) {
			shader = new EntityShader();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 0.5f, 0, 1);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable( GL11.GL_BLEND );

		if (frame != null) {
			bindModel();
			shader.start();

			Matrix4f viewMatrix = Calc.getViewMatrix();
			shader.loadViewMatrix(viewMatrix);
			Matrix4f projectionMatrix = Calc.getProjectionMatrix();
			shader.loadProjectionMatrix(projectionMatrix);

			for (int i = 0; i < frame.getBufferedEntities(); i++) {
				Entity entity = frame.getEntities().get(i);
				if (entity.getModel().getTexture().getTextureID() != -1) {
					bindTexture(entity.getModel().getTexture().getTextureID());
//					if(TextManager.manager != null && TextManager.manager.getTexture().getTextureID() != -1) {
//						bindTexture(TextManager.manager.getTexture().getTextureID());
//					}
					Matrix4f transformationMatrix = Calc.getTransformationMatrix(entity.getLocation(),
							new Vector2f(entity.getModel().getScale(), entity.getModel().getScale()), entity.getRoll());
					shader.loadTransformationMatrix(transformationMatrix);
					drawVAO();
				}
			}

			shader.stop();
			renderGUI();
		}

		unbind();
	}

	public void swapFrames() {
		if (queuedFrame != null) {
			if(frame != queuedFrame) {
				if(frame != null) {
					if(frame.getVaoID() != -1) {
						Loader.cleanVAO(frame.getVaoID());
					}
				}
			}
			frame = queuedFrame;
			queuedFrame = null;
		}
	}

	public void drawVAO() {
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void bindModel() {
//		GL30.glBindVertexArray(Loader.getQuadVAO());
		if(frame.getVaoID() == -1) {
			frame.setVaoID(Loader.loadToVAO(frame.getVerticies(), frame.getTextureCoords()));
		}
		GL30.glBindVertexArray(frame.getVaoID());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
	}

	// GL13.GL_TEXTURE0, ...
	public void bindTexture(int id) {
		GL20.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public void unbind() {
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
