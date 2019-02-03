package events;

import gui.GUIElement;

public interface InputListener {
		
	public void KeyEvent(int key, int action, int mods);
	
	public void MouseEvent(int key, int action, int mods);
	
	public void GUIEvent(GUIElement guiElement, GUIAction action, int MOUSE_BUTTON);

}
