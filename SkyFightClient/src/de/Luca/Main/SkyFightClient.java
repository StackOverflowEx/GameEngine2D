package de.Luca.Main;

import java.io.File;

import de.Luca.Connection.Connection;
import de.Luca.Entities.Player;
import de.Luca.GUIs.LoadingGUI;
import de.Luca.GUIs.LoginGUI;
import de.Luca.GUIs.RegisterGUI;
import de.Luca.GameLogic.GameState;
import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;
import de.Luca.Text.TextManager;

public class SkyFightClient {
	
	//Connection
	public static Connection handleServerConnection;
	
	//Gamelogic
	public static Player p;
	
	//GUIs
	public static LoginGUI loginGUI;
	public static RegisterGUI registesrGUI;
	public static LoadingGUI loadingGUI;
	
	//Textures
	public static Texture backgroundLOGIN;
	public static Texture playerDown;
	public static Texture playerUP;
	public static Texture background;
	
	//Fonts
	public static long Impact20;
	
	//Gamestate
	public static GameState gameState = GameState.MENUE;
	
	//Files
	public static String root = System.getenv("APPDATA") + "/SkyFight";
	
	public static void load() {
		root.replace("\\", "/");
		if(!new File(root).exists()) {
			new File(root).mkdir();
		}
		
		TextManager.generateFont("C:\\Windows\\Fonts\\impact.ttf", 20, "Impact", false, false);
		Impact20 = TextManager.getFont("Impact");
		
		backgroundLOGIN = Loader.loadTexture("D:\\Downloads\\login.png", "gui");
		playerUP = Loader.loadTexture("D:\\Downloads\\up.png", "player");
		playerDown = Loader.loadTexture("D:\\Downloads\\down.png", "player");
		background = Loader.loadTexture("D:\\Downloads\\background.png", "background");
		
		loginGUI = new LoginGUI();
		registesrGUI = new RegisterGUI();
		loadingGUI = new LoadingGUI();
	}

}
