package de.Luca.Connection;

import de.Luca.Events.Event;

public class ConnectionDisconnectedEvent extends Event{
	
	//Event wird ausgelöst, wenn eine Verbindung zum Server getrennt wird

	private Connection con;
	
	public ConnectionDisconnectedEvent(Connection con) {
		super(System.currentTimeMillis());
		this.con = con;
	}
	
	public Connection getConnection() {
		return con;
	}

}
