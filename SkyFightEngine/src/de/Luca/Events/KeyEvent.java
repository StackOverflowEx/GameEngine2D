package de.Luca.Events;

public class KeyEvent extends Event{
	
	//wird ausgelöst, wen eine Taste auf der Tastatur gedrückt wird.
	
	private int key;
	private int scanCode;
	private int action;
	private int mods;

	public KeyEvent(int key, int scanCode, int action, int mods) {
		super(System.currentTimeMillis());
		this.key = key;
		this.scanCode = scanCode;
		this.action = action;
		this.mods = mods;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getMods() {
		return mods;
	}
	
	public int getKey() {
		return key;
	}
	
	public int getScanCode() {
		return scanCode;
	}

}
