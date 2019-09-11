package de.Luca.Main;

import de.Luca.Connection.ClientConnector;
import de.Luca.Connection.Connection;
import de.Luca.GameLogic.GameLogic;
import de.Luca.GameLogic.WorldLoader;

public class Main {

	public static int port;
	public static String mapFolder;
	public static String id;

	public static Connection con;

	public static void main(String[] args) {
		port = Integer.parseInt(args[0]);
		mapFolder = args[1];
		id = args[2];

		WorldLoader.loadMap(mapFolder);

		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				ClientConnector.start();
			}
		});
		th.start();

		Thread th1 = new Thread(new Runnable() {

			@Override
			public void run() {
				GameLogic.startTicking();
			}
		});
		th1.start();

		con = new Connection(Connection.HANDLE_SERVER_IP, Connection.HANDLE_SERVER_PORT);
	}

}
