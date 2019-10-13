package de.Luca.Utils;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Blocks.BlockData;
import de.Luca.Calculation.Calc;
import de.Luca.Calculation.Camera;
import de.Luca.GUI.GUIListener;
import de.Luca.Window.Window;

public class WorldPosition {

	//Gibt die Position des Cursors zurück (in Pixel)
	public static Vector2f getAbsCursorPos() {
		if(GUIListener.getMousePosition() == null) {
			double[] xb = new double[1];
			double[] yb = new double[1];
			GLFW.glfwGetCursorPos(Window.getWindowID(), xb, yb);
			return new Vector2f((float)xb[0], (float)yb[0]);
		}else {
			return GUIListener.getMousePosition();
		}
	}
	
	//Gibt die Weltposition im Blockgrid des Cursors zurück (gerundet)
	public static Vector2f getMouseWorldPos() {
		Vector2f openGL = toOpenGLCoords(getAbsCursorPos());
		openGL = openGL.mul(Calc.getZoom());
		Vector2f worldFloat = new Vector2f(openGL.x / (BlockData.BLOCK_SCALE) - BlockData.BLOCK_SCALE / 2f, openGL.y / (BlockData.BLOCK_SCALE) - BlockData.BLOCK_SCALE / 2f);
		Vector2f add = getExactWorldPos(Camera.getPosition());
		worldFloat.x = worldFloat.x + add.x;
		worldFloat.y = worldFloat.y + add.y;
		return new Vector2f((float)Math.round(worldFloat.x), (float)Math.round(worldFloat.y));
	}
	
	//Gibt die genaue Weltposition des cursors zurück
	public static Vector2f getMouseExactWorldPos() {
		Vector2f openGL = toOpenGLCoords(getAbsCursorPos());
		openGL = openGL.mul(Calc.getZoom());
		Vector2f worldFloat = new Vector2f(openGL.x / (BlockData.BLOCK_SCALE) - BlockData.BLOCK_SCALE / 2f, openGL.y / (BlockData.BLOCK_SCALE) - BlockData.BLOCK_SCALE / 2f);
		Vector2f add = getExactWorldPos(Camera.getPosition());
		worldFloat.x = worldFloat.x + add.x;
		worldFloat.y = worldFloat.y + add.y;
		return worldFloat;
	}
	
	//Gibt die exakte Weltposition einer openGL-Position zurück
	public static Vector2f getExactWorldPos(Vector2f openGlPos) {
		Vector2f worldFloat = new Vector2f(openGlPos.x / (BlockData.BLOCK_SCALE) - BlockData.BLOCK_SCALE / 2f, openGlPos.y / (BlockData.BLOCK_SCALE) - BlockData.BLOCK_SCALE / 2f);
		return new Vector2f(worldFloat.x, worldFloat.y);
	}
	
	//Konvertiert Pixelkoordinaten in OpenGL-Koordinaten
	public static Vector2f toOpenGLCoords(Vector2f pixel) {
		Vector2f windowSize = Window.getWindowSize();
		float x = pixel.x;
		float y = windowSize.y - pixel.y;
		x = (2.0f * x) / windowSize.x - 1f;
		y = (2.0f * y) / windowSize.y -1f;
		float aspectRation = windowSize.x / windowSize.y;
		Vector2f ret = new Vector2f(x, y / aspectRation);
		return ret;
	}

	
}
