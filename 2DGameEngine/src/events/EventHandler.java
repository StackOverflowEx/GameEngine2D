package events;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;

public class EventHandler {
	
	private static ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	private static GLFWKeyCallback keyboard = new GLFWKeyCallback() {
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			for(Listener l : listeners) {
				l.ButtonEvent(window, key, scancode, action, mods);
			}
		}
	};
	
	public static GLFWKeyCallback getKeyBoard() {
		return keyboard;
	}
	
	public static void registerListener(Listener l) {
		listeners.add(l);
	}
	
	public static void removeListener(Listener l) {
		listeners.remove(l);
	}
	
	
	
}
