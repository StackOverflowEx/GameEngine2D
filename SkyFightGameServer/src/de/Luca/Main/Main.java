package de.Luca.Main;

import de.Luca.Connection.ClientConnector;
import de.Luca.Connection.Connection;

public class Main {

	public static int port;
	public static String mapFolder;
	public static String id;

	public static Connection con;

	public static void main(String[] args) {
		port = Integer.parseInt(args[0]);
		mapFolder = args[1]; //For later cheat protection
		id = args[2];

		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				ClientConnector.start();
			}
		});
		th.start();

		con = new Connection(Connection.HANDLE_SERVER_IP, Connection.HANDLE_SERVER_PORT);
	}

}
