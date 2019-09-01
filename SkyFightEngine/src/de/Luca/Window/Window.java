package de.Luca.Window;

import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import de.Luca.Calculation.Calc;
import de.Luca.EventManager.EventManager;
import de.Luca.Events.CharInputEvent;
import de.Luca.Events.CursorPositionEvent;
import de.Luca.Events.KeyEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Events.ScrollEvent;

public class Window {
	
	public static Window window;
	private boolean resized;
	private long frameCountTime;
	private long frameCount;
	private boolean fullscreen = false;
	private int lastX, lastY, lastWidth, lastHeight;
	
	private int frametimePointer;
	private float[] frametimes;
	private long last;
	
	private long WINDOW_ID;
	
	//Wird einmal aufgerufen, um das Spielefenster zu erstellen	
	public Window(int width, int height, String title) {
		if(Window.window == null) {
			resized = false;
			
			last = -1;
			frametimes = new float[100];
			frametimes[0] = 0;
			frametimes[frametimes.length-1] = 0;
			frametimePointer = 0;
			frameCountTime = 0;
			frameCount = 0;
			
			init(width, height, title);
			Window.window = this;
			System.out.println(window);
		}else {
			throw new IllegalStateException("Window already created");
		}
	}
	
	//Diese Methode ist f�r das eigentliche erstellen des Spielefensters zust�ndig (GLFW)
	private void init(int width, int height, String title) {
		
		//GLFW wird initalisiert
		if(!GLFW.glfwInit()) {
			throw new IllegalAccessError("Could not initalize GLFW");
		}
		
		//Spielfenster wird erstellt und bleibt zun�chst unsichtbar.
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		WINDOW_ID = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if(WINDOW_ID == MemoryUtil.NULL) {
			throw new IllegalStateException("Could not create window");
		}
		
		//Spielefenster wird mittig auf den Bildschrim gesetzt
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		float x = (vidMode.width() - width) / 2.0f;
		float y = (vidMode.height() - height) / 2.0f;
		GLFW.glfwSetWindowPos(WINDOW_ID, (int) x, (int) y);
		
		//OPENGL-Debug
//		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
		
		//Der GLFW Kontext wird an den Thread gebunden, damit sp�ter Buffer mit OpenGL gerendert werden k�nnen
		GLFW.glfwMakeContextCurrent(WINDOW_ID);
		GL.createCapabilities();
//		Callback debugProc = GLUtil.setupDebugMessageCallback();
		
		//Das Fenster wird angezeigt und V-Sync deaktiviert
		GLFW.glfwShowWindow(WINDOW_ID);
		GLFW.glfwSwapInterval(0);
		
		setupCallbacks();
	}
	
	public boolean hasResized() {
		return resized;
	}
	
	private void setupCallbacks() {
		GLFW.glfwSetKeyCallback(WINDOW_ID, new GLFWKeyCallbackI() {
			
			@Override
			public void invoke(long window, int key, int scanCode, int action, int mods) {
				EventManager.eventMangaer.fireEvent(new KeyEvent(key, scanCode, action, mods));
			}
		});
		GLFW.glfwSetScrollCallback(WINDOW_ID, new GLFWScrollCallbackI() {
			
			@Override
			public void invoke(long window, double xOffset, double yOffset) {
				EventManager.eventMangaer.fireEvent(new ScrollEvent(xOffset, yOffset));
			}
		});
		GLFW.glfwSetCharCallback(WINDOW_ID, new GLFWCharCallbackI() {
			
			@Override
			public void invoke(long window, int codepoint) {
				EventManager.eventMangaer.fireEvent(new CharInputEvent(codepoint));
			}
		});
		GLFW.glfwSetCursorPosCallback(WINDOW_ID, new GLFWCursorPosCallbackI() {
			
			@Override
			public void invoke(long window, double xpos, double ypos) {
				EventManager.eventMangaer.fireEvent(new CursorPositionEvent(xpos, ypos));
			}
		});
		GLFW.glfwSetMouseButtonCallback(WINDOW_ID, new GLFWMouseButtonCallbackI() {
			
			@Override
			public void invoke(long window, int button, int action, int mods) {
				EventManager.eventMangaer.fireEvent(new MouseButtonEvent(button, action, mods));
			}
		});
		GLFW.glfwSetWindowSizeCallback(WINDOW_ID, new GLFWWindowSizeCallbackI() {
			
			@Override
			public void invoke(long window, int width, int height) {
				resized = true;
				Calc.calcProjectionMatrix();
			}
		});
	}
	
	public Vector2f getWindowSize() {
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(WINDOW_ID, x, y);
		return new Vector2f(x.get(), y.get());
	}
	
	public void toggleFullscreen() {
		if (!fullscreen) {
			fullscreen = true;
			IntBuffer x = BufferUtils.createIntBuffer(1);
			IntBuffer y = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowPos(WINDOW_ID, x, y);
			lastX = x.get();
			lastY = y.get();
			GLFW.glfwGetWindowSize(WINDOW_ID, x, y);
			lastWidth = x.get();
			lastHeight = y.get();
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowMonitor(WINDOW_ID, GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(),
					60);
		} else {
			fullscreen = false;
			GLFW.glfwSetWindowMonitor(WINDOW_ID, 0, (int) lastX + 1, (int) lastY, (int) lastWidth, (int) lastHeight, 1);
		}
	}
	
	public void updateWindow() {
		//Der Viewport wird aktualisiert.
		updateViewPort();
		//Ein neuer Frame wird im Fenster angezeigt.
		GLFW.glfwSwapBuffers(WINDOW_ID);
		//Ein Frame wird registriert und verrechnet.
		countFrame();
	}
	
	public float getAvgFrameTime() {
		float sum = 0;
		for(float f : frametimes) {
			sum += f;
		}
		return sum / 100f;
	}
	
	//Diese Methode gibt jede Sekunde aus, mit wie vielen frames per second (FPS) das spiel l�uft.
	private void countFrame() {
		
		long nano = System.nanoTime();
		float frametime = nano - last;
		if(last == -1) {
			frametime = 0.5f;
		}
		frametimes[frametimePointer] = frametime;
		frametimePointer++;
		if(frametimePointer == frametimes.length) {
			frametimePointer = 0;
		}
		last = nano;
		
		if((System.currentTimeMillis() - frameCountTime) > 1000) {
			System.out.println("FPS: " + frameCount);
			frameCount = 0;
			frameCountTime = System.currentTimeMillis();
		}
		frameCount++;
	}
	
	//Der Viewport wird aktualisiert. Das ist n�tig, da sich die Fenstergr��e �ndern kann und somit auch der Viewport
	private void updateViewPort() {
		IntBuffer pWidth = BufferUtils.createIntBuffer(1);
		IntBuffer pHeight = BufferUtils.createIntBuffer(1);

		GLFW.glfwGetFramebufferSize(WINDOW_ID, pWidth, pHeight);
		GL11.glViewport(0, 0, pWidth.get(0), pHeight.get(0));
	}
	
	//Schlie�t das Fenster und beendet GLFW
	public void closeWindow() {
		GLFW.glfwDestroyWindow(WINDOW_ID);
		GLFW.glfwTerminate();
	}
	
	public long getWindowID() {
		return WINDOW_ID;
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(WINDOW_ID);
	}

}
