package de.Luca.Utils;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.glfw.GLFW;

import de.Luca.Calculation.Calc;
import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.KeyEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Events.ScrollEvent;
import de.Luca.Window.Window;

public class DefaultKeyListener implements Listener {

	//default KeyListener
	//Wird für die Gundlegenden Funktionen der Engine benötigt.
	
	//enthält alle gedrückten Tasten
	private static CopyOnWriteArrayList<String> keysPressed = new CopyOnWriteArrayList<String>();
	private static float enterZoom = 1.0f;

	@EventHandler
	public void onKeyPress(KeyEvent e) {

		if (e.getAction() == GLFW.GLFW_PRESS) {
			if (e.isCancelled()) {
				return;
			}
			keysPressed.add(e.getKey() + "");
			if (e.getKey() == GLFW.GLFW_KEY_F12) {
				Window.toggleFullscreen();
			}
			if (e.getKey() == GLFW.GLFW_KEY_V) {
				enterZoom = Calc.getZoom();
				Calc.setZoom(3f);
			}
		} else if (e.getAction() == GLFW.GLFW_RELEASE) {
			keysPressed.remove(e.getKey() + "");
			if (e.getKey() == GLFW.GLFW_KEY_V) {
				Calc.setZoom(enterZoom);
			}
		}
	}

	@EventHandler
	public void onMouse(MouseButtonEvent e) {
		if (e.getAction() == GLFW.GLFW_PRESS) {
			if (e.isCancelled()) {
				return;
			}
			keysPressed.add(e.getButton() + "");
		} else if (e.getAction() == GLFW.GLFW_RELEASE) {
			keysPressed.remove(e.getButton() + "");
		}
	}

	//Berechnet den Zoom
	@EventHandler
	public void onScroll(ScrollEvent e) {
		if (!e.isCancelled()) {
			float zoom = (float) (Calc.getZoom() + e.getyOffset() * -0.05f);
			if (zoom > 2)
				zoom = 2;
			if (zoom < 0.4)
				zoom = 0.4f;
			Calc.setZoom(zoom);
		}
	}

	public static boolean isKeyPressed(int key) {
		return keysPressed.contains(key + "");
	}

}
