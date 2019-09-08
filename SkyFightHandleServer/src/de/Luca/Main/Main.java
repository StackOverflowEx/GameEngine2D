package de.Luca.Main;

import de.Luca.Connection.Connection;
import de.Luca.MySQL.DatabaseManager;
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
		
		MySQL.connect();
		MySQL.create();
		
		int i = DatabaseManager.login("test", "test");
		System.out.println(i);
	}

}
