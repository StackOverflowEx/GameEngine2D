package de.Luca.Connection;

import de.Luca.Events.Event;
import de.Luca.Packets.Packet;

public class ConnectionRecieveEvent extends Event{

	private Packet packet;
	private Connection con;
	
	public ConnectionRecieveEvent(Packet packet, Connection con) {
		super(System.currentTimeMillis());
		this.packet = packet;
		this.con = con;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public Packet getPacket() {
		return packet;
	}

}
