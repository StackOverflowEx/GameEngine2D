package rendering;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowPosCallback;
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
	private static boolean fullscreen = false;
	private static String title;
	private static Vector2f pos;
	
	public static void createDisplay(int width, int height, String title, long monitor) {
		
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Could not initalize GLFW");
		}
		
		DisplayManager.title = title;
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		WINDOW = GLFW.glfwCreateWindow(width, height, title, monitor, 0);
		if(WINDOW == NULL) {
			throw new IllegalStateException("Could not create Window");
		}
		
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		pos = new Vector2f((vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
		GLFW.glfwSetWindowPos(WINDOW, (int)pos.x, (int)pos.y);
		
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
		
		GLFW.glfwSetWindowPosCallback(WINDOW, new GLFWWindowPosCallback() {
			
			@Override
			public void invoke(long window, int x, int y) {
				pos.x = x;
				pos.y = y;
			}
		});
		
		GLFW.glfwSwapInterval(0);
		
		time = System.currentTimeMillis();
		
	}
	
	public static String getTitle() {
		return title;
	}
	
	private static void updateViewPort() {
	    IntBuffer pWidth = BufferUtils.createIntBuffer(1);
	    IntBuffer pHeight = BufferUtils.createIntBuffer(1);

	    GLFW.glfwGetFramebufferSize(WINDOW, pWidth, pHeight);
	    GL11.glViewport(0, 0, pWidth.get(0), pHeight.get(0)); 
	}
	
	public static boolean isResized() {
		return resized;
	}
	
	public static void setResized(boolean b) {
		resized = b;
	}
	
//	public static void toggleFullscreen() {
//		if(!fullscreen) {
//			fullscreen = true;
//			Vector2f size = getWindowSize();
//			GLFW.glfwSetWindowMonitor(WINDOW, GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), 60);
//		}else {
//			fullscreen = false;
//			Vector2f size = getWindowSize();
//			GLFW.glfwSetWindowMonitor(WINDOW, 0, 0, 0, (int)size.x, (int)size.y, 1);
//		}
//	}
	
	public static void updateDisplay() {
		updateViewPort();
		GLFW.glfwSwapBuffers(WINDOW);
		if((System.currentTimeMillis() - time) < 1000) {
			counter++;
		}else {
			time = System.currentTimeMillis();
			System.out.println("FPS: " + counter);
			counter = 0;
		}
	}
	
	public static void pollEvents() {
		GLFW.glfwPollEvents();
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
