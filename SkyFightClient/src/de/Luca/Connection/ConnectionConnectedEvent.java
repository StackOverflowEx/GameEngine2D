package de.Luca.Connection;

import de.Luca.Events.Event;

public class ConnectionConnectedEvent extends Event{

	private Connection con;
	
	public ConnectionConnectedEvent(Connection con) {
		super(System.currentTimeMillis());
		this.con = con;
	}
	
	public Connection getConnection() {
		return con;
	}

}
