package de.Luca.GUI;

import java.util.ArrayList;

public class GUI {
	
	private int x, y, width, height;
	private ArrayList<GUIComponent> components;
	private boolean visible;
	
	public GUI(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.visible = false;
		this.components = new ArrayList<GUIComponent>();
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void addComponent(GUIComponent c) {
		synchronized (components) {
			components.add(c);
		}
	}
	
	public void removeComponent(GUIComponent c) {
		synchronized (components) {
			components.remove(c);
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
