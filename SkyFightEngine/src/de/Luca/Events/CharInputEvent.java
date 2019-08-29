package de.Luca.Events;

public class CharInputEvent extends Event{

	private int codepoint;
	
	public CharInputEvent(int codepoint) {
		super(System.currentTimeMillis());
		this.codepoint = codepoint;
	}
	
	public int getCodepoint() {
		return codepoint;
	}

}
