package de.Luca.GUI;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.Calc;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Shader.GUIShader;
import de.Luca.Sound.SoundData;
import de.Luca.Utils.WorldPosition;
import de.Luca.Window.Window;

public class GUIManager {
	
	private static CopyOnWriteArrayList<GUI> guis = new CopyOnWriteArrayList<GUI>();
	private static GUIShader shader;
	private static Matrix4f projection;
	
	private static SoundData clickSound;
	
	public static void init() {
		shader = new GUIShader();
	}
	
	public static void addGUI(GUI gui) {
		guis.add(gui);
	}
	
	public static CopyOnWriteArrayList<GUI> getGUIS(){
		return guis;
	}
	
	public static void removeGUI(GUI gui) {
		guis.remove(gui);
	}
	
	public static void cleanUP() {
		shader.cleanUP();
	}
	
	public static SoundData getClickSound() {
		return clickSound;
	}
	
	public static void render() {
		Vector2f windowSize = Window.getWindowSize();
		shader.start();
		boolean b = processMatrix();
		for(GUI gui : guis) {
			if(!gui.isVisible()) {
				continue;
			}
			renderGUI(gui, windowSize, b);
		}
		shader.stop();
		
	}
	
	public static void setClickSound(SoundData sd) {
		clickSound = sd;
	}
	
	private static void renderGUI(GUI gui, Vector2f windowSize, boolean b) {
		int xOffset = gui.getX();
		int yOffset = gui.getY();
		
		Vector2f offset = WorldPosition.toOpenGLCoords(new Vector2f(xOffset + windowSize.x / 2f, yOffset + windowSize.y / 2f));
		Vector2f pixelOffset = new Vector2f(xOffset, yOffset);
		
		for(GUIComponent c : gui.getComponents()) {				
			renderComponents(c, b, offset, pixelOffset);
		}
	}
	
	private static void renderComponent(GUIComponent component, boolean b, Vector2f offset, Vector2f pixelOffset) {
		
		if(!component.isVisible()) {
			return;
		}
		
		if(component.getWidth() == 0 || component.getHeight() == 0) {
			return;
		}
				
		if(b) {
			component.setRenderModel();
		}
		

		RenderModel model = component.getRenderModel();
		Texture tex = model.getModel().getTexture();
		if(tex != null) {
			shader.loadColor(new Vector4f(0, 0, 0, -1));
			MasterRenderer.bindTexture(tex.getTextureID());
		}else {
			shader.loadColor(component.getCurrentColor());
		}
		Vector2f loc = model.getLocation();
		Vector2f realLoc = new Vector2f(loc.x + offset.x, loc.y + offset.y);
		Matrix4f transformation = Calc.getTransformationMatrix(realLoc, model.getModel().getScale(), 0);
		shader.loadTransformationMatrix(transformation);
		
		//RENDER
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 8, 4);
		
		MasterRenderer.bindTexture(0);
	}
	
	private static void renderComponents(GUIComponent c, boolean b, Vector2f offset, Vector2f pixelOffset) {
		if(c.getComponents().length == 1 && c.getComponents()[0] == c) {
			renderComponent(c, b, offset, pixelOffset);
			return;
		}
		
		if(c instanceof GSlider) {
			GSlider slider = (GSlider) c;
			if(slider.isSelected()) {
				if(!GUIListener.isMouseDown()) {
					slider.setSelected(false);
				}else {
					Vector2f mouse = WorldPosition.getAbsCursorPos();
					slider.setValue(slider.getValue((int)(mouse.x - pixelOffset.x), (int)(mouse.y - pixelOffset.y))); 
				}
			}
		}
		
		for(GUIComponent component : c.getComponents()) {
			renderComponents(component, b, offset, pixelOffset);
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
