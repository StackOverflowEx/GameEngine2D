package rendering;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import tools.Camera;

public class MasterRenderer {

	private static Matrix4f projectionMatrix;
	private ArrayList<Renderer> renderers;

	public MasterRenderer() {
		renderers = new ArrayList<Renderer>();
	}

	public void addRenderer(Renderer renderer) {
		renderers.add(renderer);
	}

	public void render() {
		prepare();
		for (Renderer renderer : renderers) {
			renderer.render();
		}
		DisplayManager.setResized(false);
	}

	private void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 0, 0, 1);
		
		if(DisplayManager.isResized()) {
			createProjectionMatrix();
		}
		Camera.calcViewMatrix();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void enableAlpha() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void disableAlpha() {
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public static Matrix4f getProjectionMatrix() {
		if (projectionMatrix == null) {
			createProjectionMatrix();
		}
		return projectionMatrix;
	}
	
	public void cleanUP() {
		for(Renderer r : renderers) {
			r.cleanUP();
		}
	}

	public static void createProjectionMatrix() {

		Vector2f windowSize = DisplayManager.getWindowSize();

		float aspectRatio = windowSize.x / windowSize.y;
		float halfWidth = 1.0f;
		float halfHeight = halfWidth / aspectRatio;
		
		float left = -halfWidth;
		float right = halfWidth;
		float bottom = -halfHeight;
		float top = halfHeight;
		float far = -1f;
		float near = 1f;

		Matrix4f matrix = new Matrix4f();

		matrix.setIdentity();

		matrix.m00 = 2f / (right - left);
		matrix.m11 = 2f / (top - bottom);
		matrix.m22 = -2f / (far - near);
		matrix.m32 = (far + near) / (far - near);
		matrix.m30 = (right + left) / (right - left);
		matrix.m31 = (top + bottom) / (top - bottom);

		projectionMatrix = matrix;
	}

}
