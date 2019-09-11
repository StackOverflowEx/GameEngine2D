package de.Luca.Packets;

public class GamePacket extends Packet{
	
	public GamePacket(String input) {
		super(input);
	}
	
	public GamePacket() {
		super();
	}
	
	public Packet getMovementPacket() {
		if(a == null) {
			return null;
		}
		return new Packet((String) a);
	}

}
