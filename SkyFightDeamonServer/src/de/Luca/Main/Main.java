package de.Luca.Main;

import de.Luca.Connection.Connection;

public class Main {

	public static void main(String[] args) {
		new Connection(Connection.HANDLE_SERVER_IP, Connection.HANDLE_SERVER_PORT);
	}

}
