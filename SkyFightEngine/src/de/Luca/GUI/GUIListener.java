package de.Luca.GUI;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.EventPriority;
import de.Luca.EventManager.Listener;
import de.Luca.Events.CharInputEvent;
import de.Luca.Events.CursorPositionEvent;
import de.Luca.Events.KeyEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Events.ScrollEvent;
import de.Luca.GUI.GTextBox.INPUT_MODE;
import de.Luca.Utils.WorldPosition;

public class GUIListener implements Listener {

	private static CopyOnWriteArrayList<GUIComponent> components = new CopyOnWriteArrayList<GUIComponent>();
	private static Vector2f mousePosition;

	public static void addComponent(GUIComponent component) {
		components.add(component);
	}

	public static void removeComponent(GUIComponent component) {
		components.remove(component);
	}

	public static Vector2f getMousePosition() {
		return mousePosition;
	}

	private static boolean mouseDown = false;

	@EventHandler(priority = EventPriority.HIGH)
	public void onWindowResize(WindowResizeEvent e) {
		for (GUI g : GUIManager.getGUIS()) {
			g.windowResize(e.getWidth(), e.getHeight());
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onChar(CharInputEvent e) {
		String letter = new String(new int[] { e.getCodepoint() }, 0, 1);
		for (GUIComponent component : components) {
			if (!component.isVisible() || component.getWidth() == 0 || component.getHeight() == 0
					|| component.getGUI() == null || !(component instanceof GTextBox)) {
				continue;
			}
			GTextBox textBox = (GTextBox) component;
			if (textBox.isSelected()) {
				textBox.fireCharInput(letter, INPUT_MODE.TEXT);
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onKey(KeyEvent e) {
		if (e.getKey() == GLFW.GLFW_KEY_V && e.getAction() == GLFW.GLFW_PRESS && e.getMods() == GLFW.GLFW_MOD_CONTROL) {

			try {
				String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
						.getData(DataFlavor.stringFlavor);
				
				System.out.println(data);
				for (GUIComponent component : components) {
					if (!component.isVisible() || component.getWidth() == 0 || component.getHeight() == 0
							|| component.getGUI() == null || !(component instanceof GTextBox)) {
						continue;
					}
					GTextBox textBox = (GTextBox) component;
					if (textBox.isSelected()) {
						for(int i = 0; i < data.length(); i++) {
							System.out.println(data.substring(i, i + 1));
							textBox.fireCharInput(data.substring(i, i + 1), INPUT_MODE.TEXT);
						}
						e.setCancelled(true);
					}
				}
				
				
			} catch (HeadlessException | UnsupportedFlavorException | IOException e1) {
				e1.printStackTrace();
			}
			
		}

		if (e.getKey() == GLFW.GLFW_KEY_BACKSPACE
				&& (e.getAction() == GLFW.GLFW_PRESS || e.getAction() == GLFW.GLFW_REPEAT)) {
			for (GUIComponent component : components) {
				if (!component.isVisible() || component.getWidth() == 0 || component.getHeight() == 0
						|| component.getGUI() == null || !(component instanceof GTextBox)) {
					continue;
				}
				GTextBox textBox = (GTextBox) component;
				if (textBox.isSelected()) {
					e.setCancelled(true);
					textBox.fireCharInput(null, INPUT_MODE.DELETE);
				}
			}
		}
		if (e.getKey() == GLFW.GLFW_KEY_ENTER && e.getAction() == GLFW.GLFW_PRESS) {
			for (GUIComponent component : components) {
				if (!component.isVisible() || component.getWidth() == 0 || component.getHeight() == 0
						|| component.getGUI() == null || !(component instanceof GTextBox)) {
					continue;
				}
				GTextBox textBox = (GTextBox) component;
				if (textBox.isSelected()) {
					e.setCancelled(true);
					textBox.fireCharInput(null, INPUT_MODE.SEND);
				}
			}
		}
		for (GUIComponent c : components) {
			if (c instanceof GTextBox) {
				GTextBox box = (GTextBox) c;
				if (box.isSelected()) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onScroll(ScrollEvent e) {
		Vector2f mousePixel = WorldPosition.getAbsCursorPos();
		for (GUIComponent component : components) {

			if (!component.isVisible() || component.getWidth() == 0 || component.getHeight() == 0
					|| component.getGUI() == null) {
				continue;
			}

			int height = component.getHeight();
			if (component instanceof GDropDown) {
				height = ((GDropDown) component).getRealHeight();
			}

			Vector2f corner1 = new Vector2f(component.getX(), component.getY());
			Vector2f corner2 = new Vector2f(component.getX() + component.getWidth(), component.getY() + height);

			if (component.getGUI() != null) {
				GUI gui = component.getGUI();
				corner1.x += gui.getX();
				corner1.y += gui.getY();
				corner2.x += gui.getX();
				corner2.y += gui.getY();
			}

			if (isMouseInside(mousePixel, corner1, corner2)) {
				e.setCancelled(true);
				component.scroll(e.getxOffset(), e.getyOffset());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onMove(CursorPositionEvent e) {
		Vector2f mousePixel = new Vector2f((float) e.getXpos(), (float) e.getYpos());
		mousePosition = new Vector2f((float) e.getXpos(), (float) e.getYpos());
		for (GUIComponent component : components) {

			if (!component.isVisible() || component.getWidth() == 0 || component.getHeight() == 0
					|| component.getGUI() == null) {
				continue;
			}

			int height = component.getHeight();
			if (component instanceof GDropDown) {
				height = ((GDropDown) component).getRealHeight();
			}

			Vector2f corner1 = new Vector2f(component.getX(), component.getY());
			Vector2f corner2 = new Vector2f(component.getX() + component.getWidth(), component.getY() + height);

			if (component.getGUI() != null) {
				GUI gui = component.getGUI();
				corner1.x += gui.getX();
				corner1.y += gui.getY();
				corner2.x += gui.getX();
				corner2.y += gui.getY();
			}

			component.hover(isMouseInside(mousePixel, corner1, corner2));
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onClick(MouseButtonEvent e) {
		Vector2f mousePixel = WorldPosition.getAbsCursorPos();
		for (GUIComponent component : components) {

			if (!component.isVisible() || component.getWidth() == 0 || component.getHeight() == 0
					|| component.getGUI() == null) {
				continue;
			}

			Vector2f corner1 = new Vector2f(component.getX(), component.getY());
			Vector2f corner2 = new Vector2f(component.getX() + component.getWidth(),
					component.getY() + component.getHeight());

			if (component.getGUI() != null) {
				GUI gui = component.getGUI();
				corner1.x += gui.getX();
				corner1.y += gui.getY();
				corner2.x += gui.getX();
				corner2.y += gui.getY();
			}

			if (isMouseInside(mousePixel, corner1, corner2)) {
				component.click(e.getButton(), e.getAction(), (int) mousePixel.x, (int) mousePixel.y);
				e.setCancelled(true);
			} else {
				if (component instanceof GTextBox) {
					if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
						GTextBox box = (GTextBox) component;
						box.setSelected(false);
						box.fireTextFinish();
					}
				}
			}
		}

		mouseDown = e.getAction() == GLFW.GLFW_PRESS;

	}

	public static boolean isMouseDown() {
		return mouseDown;
	}

	public static boolean isMouseInside(Vector2f mousePixel, Vector2f corner1, Vector2f corner2) {
		if ((mousePixel.x > corner1.x && mousePixel.x < corner2.x)
				|| (mousePixel.x < corner1.x && mousePixel.x > corner2.x)) {
			if ((mousePixel.y > corner1.y && mousePixel.y < corner2.y)
					|| (mousePixel.y < corner1.y && mousePixel.y > corner2.y)) {
				return true;
			}
		}
		return false;
	}

}
