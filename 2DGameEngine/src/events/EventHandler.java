package events;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class EventHandler {
	
	private static ArrayList<InputListener> listeners = new ArrayList<InputListener>();
	
	private static GLFWKeyCallback keyboard = new GLFWKeyCallback() {
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			for(InputListener l : listeners) {
				l.KeyEvent(key, action, mods);
			}
		}
	};
	
	private static GLFWMouseButtonCallback mouse = new GLFWMouseButtonCallback() {
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			for(InputListener l : listeners) {
				l.MouseEvent(button, action, mods);
			}
		}
	};
	
	public static GLFWMouseButtonCallback getMouse() {
		return mouse;
	}
	
	public static GLFWKeyCallback getKeyBoard() {
		return keyboard;
	}
	
	public static void registerListener(InputListener l) {
		listeners.add(l);
	}
	
	public static void removeListener(InputListener l) {
		listeners.remove(l);
	}
	
	
	
}
