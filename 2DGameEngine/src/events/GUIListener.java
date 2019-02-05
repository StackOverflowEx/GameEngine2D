package events;


import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import gui.GUIElement;
import tools.Camera;

class GUIListener implements InputListener{
	
	private ArrayList<GUIElement> hovering = new ArrayList<GUIElement>();

	GUIListener() {
		EventHandler.registerListener(this);
	}
	
	@Override
	public void KeyEvent(int key, int action, int mods) {
		if(key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_REPEAT) {
			Camera.move(new Vector2f(-0.01f, 0));
		}else if(key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_REPEAT) {
			Camera.move(new Vector2f(+0.01f, 0));

		}else if(key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_REPEAT) {
			Camera.move(new Vector2f(0, 0.01f));

		}else if(key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_REPEAT) {
			Camera.move(new Vector2f(0, -0.01f));

		}
	}

	@Override
	public void MouseEvent(int key, int action, int mods) {
		if(key == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
			for(GUIElement ge : EventHandler.getCurrentlyHovering()) {
				EventHandler.fireGuiEvent(ge, GUIAction.RIGHT_CLICK, key);
			}
		}else if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
			for(GUIElement ge : EventHandler.getCurrentlyHovering()) {
				EventHandler.fireGuiEvent(ge, GUIAction.LEFT_CLICK, key);
			}
		}else {
			for(GUIElement ge : EventHandler.getCurrentlyHovering()) {
				EventHandler.fireGuiEvent(ge, GUIAction.CLICK, key);
			}
		}
	}

	@Override
	public void GUIEvent(GUIElement guiElement, GUIAction action, int MOUSE_BUTTON) {}
	
	public boolean isHovering(GUIElement ge) {
		return hovering.contains(ge);
	}
	
	public void setHovering(GUIElement ge) {
		hovering.add(ge);
	}
	
	public void stopHovering(ArrayList<GUIElement> hovering) {
		ArrayList<GUIElement> stop = new ArrayList<GUIElement>();
		for(GUIElement l : this.hovering) {
			boolean found = false;
			for(GUIElement ge : hovering) {
				if(l == ge) {
					found = true;
					break;
				}
			}
			if(!found) {
				stop.add(l);
			}
		}
		for(GUIElement rem : stop) {
			EventHandler.fireGuiEvent(rem, GUIAction.STOP_HOVER, -1);
			this.hovering.remove(rem);
		}
	}
	

}
