package de.Luca.GUI;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Vector2f;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.CursorPositionEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Utils.WorldPosition;

public class GUIListener implements Listener{
	
	private static CopyOnWriteArrayList<GUIComponent> components = new CopyOnWriteArrayList<GUIComponent>();
	
	public static void addComponent(GUIComponent component) {
		components.add(component);
	}
	
	public static void removeComponent(GUIComponent component) {
		components.remove(component);
	}
	
	@EventHandler
	public void onMove(CursorPositionEvent e) {
		Vector2f mousePixel = new Vector2f((float)e.getXpos(), (float)e.getYpos());
		for(GUIComponent component : components) {
			Vector2f corner1 = new Vector2f(component.getX(), component.getY());
			Vector2f corner2 = new Vector2f(component.getX() + component.getWidth(), component.getY() + component.getHeight());
			
			if(component.getGUI() != null) {
				GUI gui = component.getGUI();
				corner1.x += gui.getX();
				corner1.y += gui.getY();
				corner2.x += gui.getX();
				corner2.y += gui.getY();
			}

			component.hover(isMouseInside(mousePixel, corner1, corner2));
		}
	}
	
	@EventHandler
	public void onClick(MouseButtonEvent e) {
		boolean clicked = false;
		Vector2f mousePixel = WorldPosition.getAbsCursorPos();
		for(GUIComponent component : components) {
			Vector2f corner1 = new Vector2f(component.getX(), component.getY());
			Vector2f corner2 = new Vector2f(component.getX() + component.getWidth(), component.getY() + component.getHeight());
			
			if(component.getGUI() != null) {
				GUI gui = component.getGUI();
				corner1.x += gui.getX();
				corner1.y += gui.getY();
				corner2.x += gui.getX();
				corner2.y += gui.getY();
			}

			if(isMouseInside(mousePixel, corner1, corner2)) {
				component.click(e.getButton(), e.getAction());
				clicked = true;
			}
		}
		e.setCancelled(clicked);
	}
	
	private boolean isMouseInside(Vector2f mousePixel, Vector2f corner1, Vector2f corner2) {
		if((mousePixel.x > corner1.x && mousePixel.x < corner2.x) || (mousePixel.x < corner1.x && mousePixel.x > corner2.x)) {
			if((mousePixel.y > corner1.y && mousePixel.y < corner2.y) || (mousePixel.y < corner1.y && mousePixel.y > corner2.y)) {
				return true;
			}
		}
		return false;
	}

}
