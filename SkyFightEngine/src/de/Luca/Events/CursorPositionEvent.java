package de.Luca.Events;

public class CursorPositionEvent extends Event{
	
	//Wird ausgef�hrt, wenn der Mauszeiger die Position �ndert

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
