package de.Luca.GUI;

import java.util.concurrent.CopyOnWriteArrayList;

public class GUI {
	
	private int x, y, width, height;
	private CopyOnWriteArrayList<GUIComponent> components;
	private boolean visible;
	
	public GUI(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.visible = false;
		this.components = new CopyOnWriteArrayList<GUIComponent>();
		GUIManager.addGUI(this);
	}
	
	public CopyOnWriteArrayList<GUIComponent> getComponents() {
		return components;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void addComponent(GUIComponent c) {
		components.add(c);
		c.setGUI(this);
	}
	
	public void removeComponent(GUIComponent c) {
		components.remove(c);
		if(c != null) {
			c.setGUI(null);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	

}
