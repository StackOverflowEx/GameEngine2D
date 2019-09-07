package de.Luca.WorldEditor;

import de.Luca.GameLogic.GameState;
import de.Luca.Main.SkyFightClient;

public class WorldEditor {
	
	public static void start() {
		SkyFightClient.gameState = GameState.WORLDEDITOR;
	}
	
	public static void stop() {
		
	}

}
