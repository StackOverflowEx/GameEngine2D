package de.Luca.Rendering;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.Luca.Calculation.Calc;
import de.Luca.Entities.RenderModel;
import de.Luca.Entities.Texture;
import de.Luca.GUI.GUIManager;
import de.Luca.Loading.Frame;
import de.Luca.Loading.Loader;
import de.Luca.Shader.EntityShader;
import de.Luca.Text.TextManager;

public class MasterRenderer extends Thread {

	private static MasterRenderer masterRenderer;
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

	public static void queueFrame(Frame frame) {
		masterRenderer.queuedFrame = frame;
	}

	public static void queueTexture(Texture texture) {
		masterRenderer.loadTextures.add(texture);
	}

	public static void begin() {
		masterRenderer.start();
	}

	private static void loadTextures() {
		for (Texture texture : masterRenderer.loadTextures) {
			System.out.println("Loading texture");
			int id = Loader.loadTexture(texture.getBuffer(), texture.getWidth(), texture.getHeight());
			texture.setTextureID(id);
		}
		masterRenderer.loadTextures.clear();
	}

	private void processMatricies() {

		boolean loadProjection = true;
		boolean loadView = true;

		if (projection == null) {
			projection = Calc.getProjectionMatrix();
		} else {
			if (projection == Calc.getProjectionMatrix()) {
				loadProjection = false;
			} else {
				projection = Calc.getProjectionMatrix();
			}
		}

		if (view == null) {
			view = Calc.getViewMatrix();
		} else {
			if (view == Calc.getViewMatrix()) {
				loadView = false;
			} else {
				view = Calc.getViewMatrix();
			}
		}

		if (loadProjection) {
			shader.loadProjectionMatrix(projection);
		}
		if (loadView) {
			shader.loadViewMatrix(view);
		}
	}

	public static void render() {
		masterRenderer.draw();
	}

	public void draw() {
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

			long nano = System.nanoTime();
			drawEntities();
			GUIManager.render();
			TextManager.render();
			float ms = ((System.nanoTime() - nano) / 1000000f);
//			System.out.println("Rendered: " + ms + "ms");
			if(ms > 0.5) {
				System.out.println("Stutter detected");
			}

			unbind();
		}

	}

	private void drawEntities() {
		shader.start();
		processMatricies();

		for (int i = 0; i < frame.getBufferedEntities(); i++) {
			RenderModel entity = frame.getEntities().get(i);
			if (entity.getModel().getTexture().getTextureID() != -1) {
				bindTexture(entity.getModel().getTexture().getTextureID());
				Matrix4f transformationMatrix = Calc.getTransformationMatrix(entity.getLocation(),
						entity.getModel().getScale(), entity.getRoll());
				shader.loadTransformationMatrix(transformationMatrix);
				drawVAO();
			}
		}
		shader.stop();
	}

	public static void swapFrames() {
		masterRenderer.nswapFrames();
	}

	public void nswapFrames() {
		if (queuedFrame != null) {
			if (frame != queuedFrame) {
				if (frame != null) {
					if (frame.getVaoID() != -1) {
						Loader.cleanVAO(frame.getVaoID());
					}
				}
			}
			frame = queuedFrame;
			queuedFrame = null;
		}
	}

	private void drawVAO() {
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	private void bindModel() {
		if (frame.getVaoID() == -1) {
			frame.setVaoID(Loader.loadToVAO(frame.getVerticies(), frame.getTextureCoords()));
		}
		GL30.glBindVertexArray(frame.getVaoID());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
	}

	public static void bindTexture(int id) {
		GL20.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	private void unbind() {
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
