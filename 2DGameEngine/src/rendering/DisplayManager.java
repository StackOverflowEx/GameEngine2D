package rendering;

import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class DisplayManager {
	
	private static long WINDOW;
	
	public static void createDisplay(int width, int height, String title) {
		
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Could not initalize GLFW");
		}
		
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		WINDOW = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if(WINDOW == NULL) {
			throw new IllegalStateException("Could not create Window");
		}
		
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(WINDOW, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
		
		GLFW.glfwMakeContextCurrent(WINDOW);
		GL.createCapabilities();
		
		GLFW.glfwShowWindow(WINDOW);
		
	}
	
	public static void updateDisplay() {
		GLFW.glfwSwapBuffers(WINDOW);
		GLFW.glfwPollEvents();
	}
	
	public static void closeDisplay() {
		GLFW.glfwDestroyWindow(WINDOW);
		GLFW.glfwTerminate();
	}
	
	public static long getWINDOW() {
		return WINDOW;
	}

}
