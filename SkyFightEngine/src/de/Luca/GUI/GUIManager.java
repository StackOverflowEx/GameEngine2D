package de.Luca.GUI;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.Calc;
import de.Luca.Entities.RenderModel;
import de.Luca.Entities.Texture;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Shader.GUIShader;
import de.Luca.Utils.WorldPosition;
import de.Luca.Window.Window;

public class GUIManager {
	
	private static CopyOnWriteArrayList<GUI> guis = new CopyOnWriteArrayList<GUI>();
	private static GUIShader shader;
	private static Matrix4f projection;
	
	public static void init() {
		shader = new GUIShader();
	}
	
	public static void addGUI(GUI gui) {
		guis.add(gui);
	}
	
	public static void removeGUI(GUI gui) {
		guis.remove(gui);
	}
	
	public static void cleanUP() {
		shader.cleanUP();
	}
	
	public static void render() {
		Vector2f windowSize = Window.getWindowSize();
		shader.start();
		boolean b = processMatrix();
		for(GUI gui : guis) {
			renderGUI(gui, windowSize, b);
		}
		shader.stop();
		
	}
	
	private static void renderGUI(GUI gui, Vector2f windowSize, boolean b) {
		int xOffset = gui.getX();
		int yOffset = gui.getY();
		
		Vector2f offset = WorldPosition.toOpenGLCoords(new Vector2f(xOffset + windowSize.x / 2f, yOffset + windowSize.y / 2f));
		
		for(GUIComponent c : gui.getComponents()) {				
			renderComponents(c, b, offset);
		}
	}
	
	private static void renderComponent(GUIComponent component, boolean b, Vector2f offset) {
		
		if(!component.isVisible()) {
			return;
		}
		
		if(b) {
			component.setRenderModel();
		}
		
		RenderModel model = component.getRenderModel();
		Texture tex = model.getModel().getTexture();
		if(tex != null) {
			MasterRenderer.bindTexture(tex.getTextureID());
		}else {
			shader.loadColor(component.getCurrentColor());
		}
		Vector2f loc = model.getLocation();
		Vector2f realLoc = new Vector2f(loc.x + offset.x, loc.y + offset.y);
		Matrix4f transformation = Calc.getTransformationMatrix(realLoc, model.getModel().getScale(), 0);
		shader.loadTransformationMatrix(transformation);
		
		//RENDER
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		
		MasterRenderer.bindTexture(0);
	}
	
	private static void renderComponents(GUIComponent c, boolean b, Vector2f offset) {
		if(c.getComponents().length == 1 && c.getComponents()[0] == c) {
			renderComponent(c, b, offset);
			return;
		}
		for(GUIComponent component : c.getComponents()) {
			renderComponents(component, b, offset);
		}
	}
	
	private static boolean processMatrix() {

		boolean loadProjection = true;

		if (projection == null) {
			projection = Calc.getProjectionMatrix();
		} else {
			if (projection == Calc.getProjectionMatrix()) {
				loadProjection = false;
			} else {
				projection = Calc.getProjectionMatrix();
			}
		}

		if (loadProjection) {
			shader.loadProjectionMatrix(projection);
		}
		return loadProjection;
	}

}
