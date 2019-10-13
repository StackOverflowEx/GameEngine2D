package de.Luca.Connection;

import de.Luca.Events.Event;

public class ConnectionConnectedEvent extends Event{
	
	//Event, das ausgelöst wird, wenn eine Verbindung mit einem Server aufgebaut wird

	private Connection con;
	
	public ConnectionConnectedEvent(Connection con) {
		super(System.currentTimeMillis());
		this.con = con;
	}
	
	public Connection getConnection() {
		return con;
	}

}
