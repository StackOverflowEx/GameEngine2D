package de.Luca.Rendering;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.Luca.Blocks.BlockManager;
import de.Luca.Calculation.Calc;
import de.Luca.Effects.EffectManager;
import de.Luca.Entities.EntityManager;
import de.Luca.GUI.GUIManager;
import de.Luca.Loading.Frame;
import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;
import de.Luca.Shader.BlockShader;
import de.Luca.Text.TextManager;
import de.Luca.Window.Window;

public class MasterRenderer extends Thread {

	private static MasterRenderer masterRenderer;
	private BlockShader shader;
	private Frame frame;
	private Frame queuedFrame;
	private CopyOnWriteArrayList<Texture> loadTextures;
	private Matrix4f projection, view, zoomProjection;
	private Background background;
	private Texture backgroundTex;
	private boolean ProjectionChanged;
	private boolean viewChanged;
	private boolean projectionZoomChanged;
	
	private File screenShot;
	
	public MasterRenderer(RenderLoop loop) {
		super(loop);
		if (masterRenderer != null) {
			throw new IllegalStateException("MasterRenderer already created");
		}
		this.ProjectionChanged = true;
		this.viewChanged = true;
		masterRenderer = this;
		loadTextures = new CopyOnWriteArrayList<Texture>();
				
		GLFW.glfwMakeContextCurrent(0);
		setName("Rendering Thread");
	}
	
	public static void queueScreenshot(File save) {
		masterRenderer.screenShot = save;
	}
	
	public static void setBackground(Texture tex) {
		masterRenderer.backgroundTex = tex;
	}

	public static Texture getDefaultBackgroundTexture() {
		return MasterRenderer.masterRenderer.background.getDefaultTexture();
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

	public static void cleanUP(){
		MasterRenderer.masterRenderer.shader.cleanUP();
	}
	
	private static void loadTextures() {
		for (Texture texture : masterRenderer.loadTextures) {
			System.out.println("Loading texture...");
			int id = Loader.loadTexture(texture, texture.getTextureType());
			System.out.println("Loaded texture: " + id);
			texture.setTextureID(id);
			masterRenderer.loadTextures.remove(texture);
		}
	}
	
	public static boolean hasProjectionChanged() {
		return masterRenderer.ProjectionChanged;
	}
	
	public static boolean hasViewChanged() {
		return masterRenderer.viewChanged;
	}
	
	public static boolean hasZoomProjectionChanged() {
		return masterRenderer.projectionZoomChanged;
	}
	
	public static Matrix4f getProjection() {
		return masterRenderer.projection;
	}
	
	public static Matrix4f getView() {
		return masterRenderer.view;
	}
	
	public static Matrix4f getZoomProjection() {
		return masterRenderer.zoomProjection;
	}

	private void processMatricies() {

		boolean loadProjection = true;
		boolean loadView = true;
		boolean loadZommProjection = true;
		Matrix4f calcZommProjeciton = Calc.getZommProjectionMatrix();
		Matrix4f calcProjection = Calc.getProjectionMatrix();
		Matrix4f calcView = Calc.getViewMatrix();

		if (projection == null) {
			projection = calcProjection;
		} else {
			if (projection == calcProjection) {
				loadProjection = false;
			} else {
				projection = calcProjection;
			}
		}

		if (view == null) {
			view = calcView;
		} else {
			if (view == calcView) {
				loadView = false;
			} else {
				view = calcView;
			}
		}
		
		if (zoomProjection == null) {
			zoomProjection = calcZommProjeciton;
		} else {
			if (zoomProjection == calcZommProjeciton) {
				loadZommProjection = false;
			} else {
				zoomProjection = calcZommProjeciton;
			}
		}
		
		ProjectionChanged = loadProjection;
		viewChanged = loadView;
		projectionZoomChanged = loadZommProjection;
		
	}

	public static void render() {		
		masterRenderer.draw();
	}

	public void draw() {
		loadTextures();
		swapFrames();
		if (shader == null) {
			shader = new BlockShader();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		processMatricies();
		
		if (frame != null) {
			bindModel();

			drawBackground();
//			drawEntities();
			BlockManager.render();
			EntityManager.render();
			EffectManager.render();
			
			if(masterRenderer.screenShot != null) {
				Window.takeScreenshot(masterRenderer.screenShot);
				masterRenderer.screenShot = null;
			}
			
			GUIManager.render();
			TextManager.render();

			unbind();
		}

	}
	
	public static void switchBackground(Texture switchTex, long durationMillis) {
		MasterRenderer.masterRenderer.background.switchTexture(switchTex, durationMillis);
	}
	
	private void drawBackground() {
		if(background == null) {
			if(backgroundTex == null) {
				return;
			}
			background = new Background(backgroundTex);
		}
		background.getShader().start();
		
		if(MasterRenderer.hasProjectionChanged()) {
			background.getShader().loadProjectionMatrix(MasterRenderer.getProjection());
		}
		
		background.bindTexture();
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4, 4);
		
		background.getShader().stop();
	}

//	private void drawEntities() {
//		shader.start();
//		processMatricies();
//
//		for (int i = 0; i < frame.getBufferedEntities(); i++) {
//			RenderModel entity = frame.getEntities().get(i);
//			if (entity.getModel().getTexture().getTextureID() != -1) {
//				bindTexture(entity.getModel().getTexture().getTextureID());
//				Matrix4f transformationMatrix = Calc.getTransformationMatrix(entity.getLocation(),
//						entity.getModel().getScale(), entity.getRoll());
//				shader.loadTransformationMatrix(transformationMatrix);
//				drawVAO();
//			}
//		}
//		shader.stop();
//	}

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

//	private void drawVAO() {
//		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4, 4);
//	}

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
