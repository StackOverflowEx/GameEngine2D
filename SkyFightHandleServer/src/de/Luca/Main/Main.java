package de.Luca.Main;

import java.io.File;
import java.net.URISyntaxException;

import de.Luca.Connection.Connection;
import de.Luca.Connection.DemonConnection;
import de.Luca.Connection.DemonInfo;
import de.Luca.MySQL.MySQL;

public class Main {

	public static String root;
	public static void main(String[] args) {
		
		try {
			root = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
		} catch (URISyntaxException e) {
			System.err.println("Could not locate jar");
			System.exit(-1);
			e.printStackTrace();
		}
		root = new File(root).getParentFile().getPath();
		
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
		DemonInfo.requestLoopStart();
	}

}
