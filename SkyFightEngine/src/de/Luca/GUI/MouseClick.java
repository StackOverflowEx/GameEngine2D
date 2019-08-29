package de.Luca.GUI;

public class MouseClick {
	
	private int x, y;
	private int key;
	private boolean pressed;
	
	public MouseClick(int x, int y, int key, boolean pressed) {
		super();
		this.x = x;
		this.y = y;
		this.key = key;
		this.pressed = pressed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getKey() {
		return key;
	}

	public boolean isPressed() {
		return pressed;
	}
	
	

}
