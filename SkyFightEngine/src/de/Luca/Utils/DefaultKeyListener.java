package de.Luca.Utils;

import org.lwjgl.glfw.GLFW;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Main.Main;

public class DefaultKeyListener implements Listener{
	
	@EventHandler
	public void onMouseClick(MouseButtonEvent e) {
		if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(e.getAction() == GLFW.GLFW_RELEASE) {
				Main.paragraph.setVisible(!Main.paragraph.isVisible());
			}
		}
	}

}
