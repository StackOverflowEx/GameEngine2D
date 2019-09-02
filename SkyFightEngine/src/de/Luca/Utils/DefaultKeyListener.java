package de.Luca.Utils;

import org.lwjgl.glfw.GLFW;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.KeyEvent;
import de.Luca.Window.Window;

public class DefaultKeyListener implements Listener{
	
	
	@EventHandler
	public void onKeyPress(KeyEvent e) {
		if(e.getAction() == GLFW.GLFW_PRESS) {
			if(e.getKey() == GLFW.GLFW_KEY_F12) {
				Window.toggleFullscreen();
			}
			if(e.getKey() == GLFW.GLFW_KEY_F3) {
				
			}
		}
	}	

}
