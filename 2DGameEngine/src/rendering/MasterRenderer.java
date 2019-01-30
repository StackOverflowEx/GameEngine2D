package rendering;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

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
	}

	private void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 0, 0, 1);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
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

	private static void createProjectionMatrix() {

		IntBuffer widthb = BufferUtils.createIntBuffer(1);
		IntBuffer heightb = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(DisplayManager.getWINDOW(), widthb, heightb);

		float aspectRatio = (float) widthb.get() / (float) heightb.get();
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

		matrix.m00 = 2 / (right - left);
		matrix.m11 = 2 / (top - bottom);
		matrix.m22 = -2 / (far - near);
		matrix.m32 = (far + near) / (far - near);
		matrix.m30 = (right + left) / (right - left);
		matrix.m31 = (top + bottom) / (top - bottom);

		projectionMatrix = matrix;
	}

}
