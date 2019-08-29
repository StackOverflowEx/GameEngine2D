package de.Luca.Events;

public class MouseButtonEvent extends Event{
	
	private int button, action, mods;
	
	public MouseButtonEvent(int button, int action, int mods) {
		super(System.currentTimeMillis());
		this.button = button;
		this.action = action;
		this.mods = mods;
	}

	public int getButton() {
		return button;
	}

	public int getAction() {
		return action;
	}

	public int getMods() {
		return mods;
	}
	

}
