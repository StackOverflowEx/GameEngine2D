package de.Luca.Packets;

public class GamePacket extends Packet{
	
	public GamePacket(String input) {
		super(input);
	}
	
	public GamePacket() {
		super();
	}
	
	public Packet getOwnMovementPacket() {
		if(a == null) {
			return null;
		}
		return new Packet((String) a);
	}
	
	public Packet getOtherMovementPacket() {
		if(b == null) {
			return null;
		}
		return new Packet((String) b);
	}

}
