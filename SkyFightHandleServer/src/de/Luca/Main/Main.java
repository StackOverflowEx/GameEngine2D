package de.Luca.Main;

import de.Luca.Connection.Connection;
import de.Luca.Connection.DemonConnection;
import de.Luca.MySQL.MySQL;

public class Main {

	public static void main(String[] args) {
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Connection.start();
			}
		});
		th.start();
		Thread th1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				DemonConnection.start();
			}
		});
		th1.start();
		
		MySQL.connect();
		MySQL.create();
	}

}
