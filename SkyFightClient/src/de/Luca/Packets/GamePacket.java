package de.Luca.Packets;

public class GamePacket extends Packet{
	
	public static final int INFO = 0;
	public static final int POSITION = 1;
	
	public GamePacket(String json) {
		super(json);
	}
	
	public GamePacket() {
		super();
	}
	
	public void setGamePacketType(int type) {
		a = type;
	}
	
	public int getGamePacketType() {
		return Integer.parseInt(a.toString());
	}

}
