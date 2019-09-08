package de.Luca.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.Luca.Main.Main;

public class ClientConnector {

	private static ServerSocket serverSocket;
	
	public static void start() {
		if(serverSocket == null) {
			try {
				serverSocket = new ServerSocket(Main.port);
				System.out.println("Listening on port " + Main.port);
				
				while(true) {
					Socket socket = serverSocket.accept();
					Thread th = new Thread(new ConnectionHandler(socket));
					th.start();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
