package de.Luca.Main;

import java.io.File;
import java.net.URISyntaxException;

import de.Luca.Connection.Connection;

public class Main {

	public static String root;
	public static Connection con;

	public static void main(String[] args) {
		try {
			root = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
		} catch (URISyntaxException e) {
			System.err.println("Could not locate jar");
			System.exit(-1);
			e.printStackTrace();
		}
		
//		String ip = args[0];
//		Connection.HANDLE_SERVER_IP = ip;
		
		con = new Connection(Connection.HANDLE_SERVER_IP, Connection.HANDLE_SERVER_PORT);
	}

}
