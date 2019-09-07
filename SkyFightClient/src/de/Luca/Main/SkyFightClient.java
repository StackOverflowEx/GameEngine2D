package de.Luca.Main;

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
	
	public static void load() {
		loginGUI = new LoginGUI();
	}

}
