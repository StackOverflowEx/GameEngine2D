package tools;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import rendering.DisplayManager;
import rendering.MasterRenderer;

public class Mouse {

	public static Vector2f getAbsCursorPos() {
		DoubleBuffer xb = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yb = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(DisplayManager.getWINDOW(), xb, yb);
		return new Vector2f((float)xb.get(), (float)yb.get());
	}
	
	public static Vector2f getWorldPos() {
		return getWorldPos(getAbsCursorPos());
	}
	
	public static Vector2f getWorldPosWithoutCamera() {
		return getWorldPosWithoutCamera(getAbsCursorPos());
	}
	
	public static Vector2f getWorldPos(Vector2f pos) {
		Vector2f absPos = pos;
		Vector2f windowSize = DisplayManager.getWindowSize();
		
		float x = (float) absPos.x;
		float y = windowSize.y - (float) absPos.y;
		
		//normalise
		x = (2.0f * x) / windowSize.x - 1f;
		y = (2.0f * y) / windowSize.y -1f;
				
		//clipspace
		Vector4f clipCoords = new Vector4f(x, y, 0f, 1f);
		
		//eyespace
		Matrix4f invertedProjection = Matrix4f.invert(MasterRenderer.getProjectionMatrix(), null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		
		Matrix4f invertedView = Matrix4f.invert(Camera.getViewMatrix(), null);
		Vector4f world = Matrix4f.transform(invertedView, eyeCoords, null);
				
		Vector2f worldPos = new Vector2f(world.x, world.y);
		return worldPos;
	}
	
	public static Vector2f getWorldPosWithoutCamera(Vector2f pos) {
		Vector2f absPos = pos;
		Vector2f windowSize = DisplayManager.getWindowSize();
		
		float x = (float) absPos.x;
		float y = windowSize.y - (float) absPos.y;
		
		//normalise
		x = (2.0f * x) / windowSize.x - 1f;
		y = (2.0f * y) / windowSize.y -1f;
				
		//clipspace
		Vector4f clipCoords = new Vector4f(x, y, 0f, 1f);
		
		//eyespace
		Matrix4f invertedProjection = Matrix4f.invert(MasterRenderer.getProjectionMatrix(), null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
				
		Vector2f worldPos = new Vector2f(eyeCoords.x, eyeCoords.y);
		return worldPos;
	}
	
	public static Vector2f getGridPosMouse(float GRID_SCALE) {
		Vector2f worldPos = Mouse.getWorldPos();
		Vector2f gridPos = new Vector2f(worldPos.x / GRID_SCALE, worldPos.y / GRID_SCALE);
				
		gridPos.x = (int)(gridPos.x);
		gridPos.y = (int)(gridPos.y);
				
		return gridPos;
	}

	
}
