package de.Luca.Utils;

import org.lwjgl.glfw.GLFW;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.KeyEvent;
import de.Luca.Loading.Loader;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Window.Window;

public class DefaultKeyListener implements Listener{
	
	private boolean b = false;
	
	@EventHandler
	public void onKeyPress(KeyEvent e) {
		if(e.getAction() == GLFW.GLFW_PRESS) {
			if(e.getKey() == GLFW.GLFW_KEY_F12) {
				Window.toggleFullscreen();
			}
			if(e.getKey() == GLFW.GLFW_KEY_F3) {
				if(!b) {
					MasterRenderer.switchBackground(Loader.loadTexture("D:\\Downloads\\preview_164.png"), 5000);
					b = true;
				}else {
					b = false;
					MasterRenderer.switchBackground(MasterRenderer.getDefaultBackgroundTexture(), 5000);
				}
			}
		}
	}	

}
