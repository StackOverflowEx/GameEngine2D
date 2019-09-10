package de.Luca.Connection;

import de.Luca.Events.Event;

public class ConnectionDisconnectedEvent extends Event{

	private Connection con;
	
	public ConnectionDisconnectedEvent(Connection con) {
		super(System.currentTimeMillis());
		this.con = con;
	}
	
	public Connection getConnection() {
		return con;
	}

}
