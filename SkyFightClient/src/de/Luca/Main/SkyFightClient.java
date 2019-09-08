package de.Luca.Main;

import java.io.File;

import de.Luca.Connection.Connection;
import de.Luca.Entities.Player;
import de.Luca.GUIs.LoginGUI;
import de.Luca.GameLogic.GameState;

public class SkyFightClient {
	
	//Connection
	public static Connection handleServerConnection;
	
	//Gamelogic
	public static Player p;
	
	//GUIs
	public static LoginGUI loginGUI;
	
	//Gamestate
	public static GameState gameState = GameState.MENUE;
	
	//Files
	public static String root = System.getenv("APPDATA") + "/SkyFight";
	
	public static void load() {
		root.replace("\\", "/");
		if(!new File(root).exists()) {
			new File(root).mkdir();
		}
		loginGUI = new LoginGUI();
	}

}
