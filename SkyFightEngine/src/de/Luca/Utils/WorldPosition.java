package de.Luca.Utils;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Window.Window;

public class WorldPosition {

	public static Vector2f getAbsCursorPos() {
		double[] xb = new double[1];
		double[] yb = new double[1];
		GLFW.glfwGetCursorPos(Window.getWindowID(), xb, yb);
		return new Vector2f((float)xb[0], (float)yb[0]);
	}
	
	public static Vector2f toOpenGLCoords(Vector2f pixel) {
		Vector2f windowSize = Window.getWindowSize();
		float x = pixel.x;
		float y = windowSize.y - pixel.y;
		x = (2.0f * x) / windowSize.x - 1f;
		y = (2.0f * y) / windowSize.y -1f;
		float aspectRation = windowSize.x / windowSize.y;
		return new Vector2f(x, y / aspectRation);
	}
	
//	public static Vector2f getWorldPos() {
//		return getWorldPos(getAbsCursorPos());
//	}
//	
//	public static Vector2f getWorldPosWithoutCamera() {
//		return getWorldPosWithoutCamera(getAbsCursorPos());
//	}
//	
//	public static Vector2f getWorldPos(Vector2f pos) {
//		Vector2f absPos = pos;
//		Vector2f windowSize = Window.window.getWindowSize();
//		
//		float x = (float) absPos.x;
//		float y = windowSize.y - (float) absPos.y;
//		
//		//normalise
//		x = (2.0f * x) / windowSize.x - 1f;
//		y = (2.0f * y) / windowSize.y -1f;
//				
//		//clipspace
//		Vector4f clipCoords = new Vector4f(x, y, 0f, 1f);
//		
//		//eyespace
//		Matrix4f invertedProjection = Calc.getProjectionMatrix();
//		invertedProjection = invertedProjection.invert();
//		Vector4f eyeCoords = invertedProjection.transform(clipCoords);
//		
//		Matrix4f invertedView = Calc.getViewMatrix();
//		invertedView = invertedView.invert();
//		Vector4f world = invertedView.transform(eyeCoords);
//				
//		Vector2f worldPos = new Vector2f(world.x, world.y);
//		return worldPos;
//	}
//	
//	public static Vector2f getWorldPosWithoutCamera(Vector2f pos) {
//		Vector2f absPos = pos;
//		Vector2f windowSize = Window.window.getWindowSize();
//		
//		float x = (float) absPos.x;
//		float y = windowSize.y - (float) absPos.y;
//		
//		//normalise
//		x = (2.0f * x) / windowSize.x - 1f;
//		y = (2.0f * y) / windowSize.y -1f;
//				
//		//clipspace
//		Vector4f clipCoords = new Vector4f(x, y, 0f, 1f);
//		
//		//eyespace
//		Matrix4f invertedProjection = Calc.getProjectionMatrix();
//		invertedProjection = invertedProjection.invert();
//		Vector4f eyeCoords = invertedProjection.transform(clipCoords);
//				
//		Vector2f worldPos = new Vector2f(eyeCoords.x, eyeCoords.y);
//		return worldPos;
//	}
//	
//	public static Vector2f getGridPosMouse(float GRID_SCALE) {
//		Vector2f worldPos = WorldPosition.getWorldPos();
////		System.out.println(worldPos);
//		Vector2f gridPos = new Vector2f(worldPos.x / GRID_SCALE, worldPos.y / GRID_SCALE);
//				
//		gridPos.x = (int)(gridPos.x);
//		gridPos.y = (int)(gridPos.y);
//				
//		return gridPos;
//	}

	
}
