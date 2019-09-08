package de.Luca.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.Luca.Packets.Packet;

public class DatabaseManager {
	
	public static int register(String username, String email, String pw) {
		email = email.toLowerCase();
		username = username.toLowerCase();
		if(doseUserExist(username)) {
			return Packet.ERROR_USERNAME_EXISTS;
		}
		if(doseEmailExist(email)) {
			return Packet.ERROR_EMAIL_EXISTS;
		}
		if(registerSQL(username, email, pw)) {
			return 0;
		}
		
		return Packet.ERROR_SERVER;
	}
	
	public static String getUsername(String email) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT 1 FROM Userdata WHERE EMAIL = ?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static boolean setPassword(String email, String password) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Userdata SET PASSWORD=? WHERE EMAIL=?");
			ps.setString(1, password);
			ps.setString(2, email);
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int login(String username, String pw) {
		username = username.toLowerCase();
		boolean isEmail = false;
		if(username.contains("@")) {
			isEmail = true;
			if(!doseEmailExist(username)){
				return Packet.ERROR_NO_USERNAME_EMAIL;
			}
		}else {
			if(!doseUserExist(username)){
				return Packet.ERROR_NO_USERNAME_EMAIL;
			}
		}
		String pass = getPW(username, isEmail);
		if(pass == null) {
			return Packet.ERROR_SERVER;
		}
		if(pass.equals(pw))
			return 0;
		
		return Packet.ERROR_WRONG_PASSWORD;
	}
	
	public static boolean doseUserExist(String username) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT 1 FROM Userdata WHERE USERNAME = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean doseEmailExist(String email) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT 1 FROM Userdata WHERE EMAIL = ?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
		
	public static String getPW(String username, boolean isEmail) {
		try {
			String where = "USERNAME";
			if(isEmail) {
				where = "EMAIL";
			}
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT PASSWORD FROM Userdata WHERE " + where + " = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean registerSQL(String username, String email, String password) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Userdata (USERNAME, PASSWORD, EMAIL)" + " values (?, ?, ?)");
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, email);
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
