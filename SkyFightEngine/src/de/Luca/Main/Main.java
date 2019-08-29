package de.Luca.Main;

import de.Luca.Calculation.MainBeat;
import de.Luca.EventManager.EventManager;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Rendering.RenderLoop;
import de.Luca.Window.Window;

public class Main {
	
	public static void main(String[] args) {	
		new Window(1280, 720, "SkyFight");
		new MasterRenderer(new RenderLoop());
		new EventManager();
		
		MasterRenderer.masterRenderer.start();
		
		MainBeat.init();
		
	}

}
