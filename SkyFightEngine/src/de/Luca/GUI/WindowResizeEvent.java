package de.Luca.GUI;

import de.Luca.Events.Event;

public class WindowResizeEvent extends Event{

	private int width, height;
	
	public WindowResizeEvent(int width, int height) {
		super(System.currentTimeMillis());
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	

}
