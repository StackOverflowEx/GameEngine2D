package rendering;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import events.EventHandler;

public class DisplayManager {
	
	private static long WINDOW;
	private static long time;
	private static int counter;
	private static boolean resized;
	
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
		
		GLFW.glfwSetKeyCallback(WINDOW, EventHandler.getKeyBoard());
		
		GLFW.glfwSetWindowSizeCallback(WINDOW, new GLFWWindowSizeCallback() {
			
			@Override
			public void invoke(long arg0, int arg1, int arg2) {
				resized = true;
			}
		});
		
		GLFW.glfwSwapInterval(0);
		
		time = System.currentTimeMillis();
		
	}
	
	public static boolean isResized() {
		return resized;
	}
	
	public static void setResized(boolean b) {
		resized = b;
	}
	
	public static void updateDisplay() {
		GLFW.glfwSwapBuffers(WINDOW);
		GLFW.glfwPollEvents();
		if((System.currentTimeMillis() - time) < 1000) {
			counter++;
		}else {
			time = System.currentTimeMillis();
			System.out.println("FPS: " + counter);
			counter = 0;
		}
	}
	
	public static Vector2f getWindowSize() {
		IntBuffer wb = BufferUtils.createIntBuffer(1);
		IntBuffer hb = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(WINDOW, wb, hb);
		return new Vector2f(wb.get(), hb.get());
	}
	
	public static void closeDisplay() {
		GLFW.glfwDestroyWindow(WINDOW);
		GLFW.glfwTerminate();
	}
	
	public static long getWINDOW() {
		return WINDOW;
	}

}
