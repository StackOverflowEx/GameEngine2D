package de.Luca.Utils;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Text.Text;
import de.Luca.Text.TextManager;

public class DefaultKeyListener implements Listener{
	
	@EventHandler
	public void onMouseClick(MouseButtonEvent e) {
		if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(e.getAction() == GLFW.GLFW_RELEASE) {
				TextManager.manager.addText(new Text(TextManager.manager.getFont("Arial"), 100, 100, "Hello, World!", new Vector4f(0, 0, 0, 1)));
//				MainBeat.addEntity(new Entity(new Vector2f(0, 0), new Model(Loader.loadTexture("D:\\Icons\\Icon2.png"), 0.05f), 0));
			}
		}
	}

}
