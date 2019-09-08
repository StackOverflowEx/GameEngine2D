package de.Luca.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServerConnector {

	public static final int PORT = 33331;
	private static ServerSocket serverSocket;
	
	public static void start() {
		if(serverSocket == null) {
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Listening on port " + PORT);
				
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
