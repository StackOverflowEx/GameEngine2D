package de.Luca.Events;

public class CharInputEvent extends Event{

	//Wird ausgelöst, wenn ein Charakter eingegeben wird.
	
	private int codepoint;
	
	public CharInputEvent(int codepoint) {
		super(System.currentTimeMillis());
		this.codepoint = codepoint;
	}
	
	public int getCodepoint() {
		return codepoint;
	}

}
