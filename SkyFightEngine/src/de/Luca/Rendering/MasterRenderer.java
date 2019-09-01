package de.Luca.Rendering;

import java.util.concurrent.CopyOnWriteArrayList;

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
import de.Luca.Loading.Frame;
import de.Luca.Loading.Loader;
import de.Luca.Shader.EntityShader;
import de.Luca.Text.TextManager;

public class MasterRenderer extends Thread {

	public static MasterRenderer masterRenderer;
	private EntityShader shader;
	private Frame frame;
	private Frame queuedFrame;
	private CopyOnWriteArrayList<Texture> loadTextures;
	private Matrix4f projection, view;

	public MasterRenderer(RenderLoop loop) {
		super(loop);
		if (masterRenderer != null) {
			throw new IllegalStateException("MasterRenderer already created");
		}
		GLFW.glfwMakeContextCurrent(0);
		setName("Rendering Thread");
		masterRenderer = this;
		loadTextures = new CopyOnWriteArrayList<Texture>();
	}

	public void queueFrame(Frame frame) {
//		if (queuedFrame != null) {
//			synchronized (this.queuedFrame) {
//				this.queuedFrame = frame;
//			}
//		} else {
//			this.queuedFrame = frame;
//		}
		this.queuedFrame = frame;
	}

	public void queueTexture(Texture texture) {
		loadTextures.add(texture);
	}

	private void loadTextures() {
//		synchronized (loadTextures) {
			for (Texture texture : loadTextures) {
				System.out.println("Loading texture");
				int id = Loader.loadTexture(texture.getBuffer(), texture.getWidth(), texture.getHeight());
				texture.setTextureID(id);
			}
			loadTextures.clear();
//		}
	}
	
	private void renderGUI() {

		TextManager.render();
		
	}
	
	private void processMatricies() {
		
		boolean loadProjection = true;
		boolean loadView = true;
		
		if(projection == null) {
			projection = Calc.getProjectionMatrix();
		}else {
			if(projection == Calc.getProjectionMatrix()) {
				loadProjection = false;
			}else {
				projection = Calc.getProjectionMatrix();
			}
		}
		
		if(view == null) {
			view = Calc.getViewMatrix();
		}else {
			if(view == Calc.getViewMatrix()) {
				loadView = false;
			}else {
				view = Calc.getViewMatrix();
			}
		}
		
		if(loadProjection) {
			shader.loadProjectionMatrix(projection);
		}
		if(loadView) {
			shader.loadViewMatrix(view);
		}
	}

	public void render() {
		loadTextures();
		swapFrames();
		if (shader == null) {
			shader = new EntityShader();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 0.5f, 0, 1);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);

		if (frame != null) {
			bindModel();
			shader.start();

			processMatricies();

			for (int i = 0; i < frame.getBufferedEntities(); i++) {
				Entity entity = frame.getEntities().get(i);
				if (entity.getModel().getTexture().getTextureID() != -1) {
					bindTexture(entity.getModel().getTexture().getTextureID());
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
		if(frame.getVaoID() == -1) {
			frame.setVaoID(Loader.loadToVAO(frame.getVerticies()));
		}
		GL30.glBindVertexArray(frame.getVaoID());
		GL30.glEnableVertexAttribArray(0);
//		GL30.glEnableVertexAttribArray(1);
	}

	public void bindTexture(int id) {
		GL20.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public void unbind() {
		GL30.glDisableVertexAttribArray(0);
//		GL30.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
