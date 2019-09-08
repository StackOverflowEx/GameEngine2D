package de.Luca.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DemonConnection {
	
	public static final int PORT = 33332;
	private static ServerSocket serverSocket;
	
	public static void start() {
		if(serverSocket == null) {
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Listening on port " + PORT);
				
				while(true) {
					Socket socket = serverSocket.accept();
					Thread th = new Thread(new DemonConnectionHandler(socket));
					th.start();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
