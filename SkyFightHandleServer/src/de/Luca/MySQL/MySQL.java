package de.Luca.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
	
	private static Connection con;
	private static final String DB_PASSWORD = "UWQvbPs3yvqsNPp3";
	private static final String DB_USERNAME = "SkyFight";
	private static final String DB_NAME = "SkyFight";
	private static final String DB_HOST = "167.86.87.105:3306";
	
	public static void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?useLegacyDatetimeCode=false&serverTimezone=UTC",DB_USERNAME,DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void create() {
		sendStatement("CREATE TABLE IF NOT EXISTS Userdata (ID INT NOT NULL AUTO_INCREMENT, USERNAME VARCHAR(16), PASSWORD VARCHAR(255), EMAIL VARCHAR(255), PRIMARY KEY (ID));");
	}
	
	public static boolean sendStatement(String s) {
		try {
			return con.createStatement().execute(s);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Connection getConnection() {
		return con;
	}

}
