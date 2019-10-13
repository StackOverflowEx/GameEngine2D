package de.Luca.Connection;

import de.Luca.Events.Event;

public class ConnectionErrorEvent extends Event{
	
	//Event wird ausgelöst, wenn ein Fehler erhalten wurde

	private Connection con;
	
	public ConnectionErrorEvent(Connection con) {
		super(System.currentTimeMillis());
		this.con = con;
	}
	
	public Connection getConnection() {
		return con;
	}

}
