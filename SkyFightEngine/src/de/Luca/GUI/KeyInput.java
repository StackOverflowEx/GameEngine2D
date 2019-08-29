package de.Luca.GUI;

public class KeyInput {
	
	private int key;
	private boolean pressed;
	
	public KeyInput(int key, boolean pressed) {
		super();
		this.key = key;
		this.pressed = pressed;
	}
	
	public int getKey() {
		return key;
	}
	public boolean isPressed() {
		return pressed;
	}
	
	

}
