package events;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import gui.GUIElement;
import gui.GUIHandler;

public class EventHandler {
	
	private static ArrayList<InputListener> listeners = new ArrayList<InputListener>();
	private static ArrayList<GUIElement> currentlyHovering = new ArrayList<GUIElement>();
	private static GUIListener l = new GUIListener();
	
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
	
	public static ArrayList<GUIElement> getCurrentlyHovering(){
		return currentlyHovering;
	}
	
	public static void fireGuiEvent(GUIElement ge, GUIAction action, int MOUSE_BUTTON) {
		for(InputListener l : listeners) {
			l.GUIEvent(ge, action, MOUSE_BUTTON);
		}
	}
	
	public static void pollEvents() {
		//Hoverevents
		currentlyHovering.clear();
		currentlyHovering.addAll(GUIHandler.getElementsHovering());
		for(GUIElement ge : currentlyHovering) {
			//start hover
			if(!l.isHovering(ge)) {
				fireGuiEvent(ge, GUIAction.START_HOVER, -1);
				l.setHovering(ge);
			}
			//hover
			fireGuiEvent(ge, GUIAction.HOVER, -1);
		}
		//stop hover
		l.stopHovering(currentlyHovering);
		
		//Clickevents
	}
	
	
	
}
