package de.Luca.Events;

public class CursorPositionEvent extends Event{

	private double xpos, ypos;
	
	public CursorPositionEvent(double xpos, double ypos) {
		super(System.currentTimeMillis());
		this.xpos = xpos;
		this.ypos = ypos;
	}

	public double getXpos() {
		return xpos;
	}

	public double getYpos() {
		return ypos;
	}
	
}
