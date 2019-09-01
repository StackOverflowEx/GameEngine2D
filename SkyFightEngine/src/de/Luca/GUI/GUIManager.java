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
			int xOffset = gui.getX();
			int yOffset = gui.getY();
			
			Vector2f offset = WorldPosition.toOpenGLCoords(new Vector2f(xOffset + windowSize.x / 2f, yOffset + windowSize.y / 2f));
			
			for(GUIComponent component : gui.getComponents()) {
				
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
		}
		shader.stop();
		
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
