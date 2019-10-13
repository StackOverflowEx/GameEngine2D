package de.Luca.Events;

public class ScrollEvent extends Event{
	
	//Wird ausgelöst wenn mit der Maus gescrollt wird.

	private double xOffset, yOffset;
	
	public ScrollEvent(double xOffset, double yOffset) {
		super(System.currentTimeMillis());
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public double getxOffset() {
		return xOffset;
	}

	public double getyOffset() {
		return yOffset;
	}
	

}
