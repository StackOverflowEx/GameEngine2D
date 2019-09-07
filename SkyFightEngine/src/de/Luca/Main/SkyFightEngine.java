package de.Luca.Main;

import org.lwjgl.opengl.GL11;

import de.Luca.Blocks.BlockManager;
import de.Luca.Calculation.BeatHandler;
import de.Luca.Calculation.MainBeat;
import de.Luca.Effects.EffectManager;
import de.Luca.Entities.EntityManager;
import de.Luca.EventManager.EventManager;
import de.Luca.GUI.GUIListener;
import de.Luca.GUI.GUIManager;
import de.Luca.Loading.Loader;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Rendering.RenderLoop;
import de.Luca.Sound.AudioManager;
import de.Luca.Text.TextManager;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Window.Window;

public class SkyFightEngine {
	
	public static void main(String[] args) {
		init(new BeatHandler() {
			
			@Override
			public void loop() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void init() {
				// TODO Auto-generated method stub
				
			}
		});
	}
		
	public static void init(BeatHandler bh) {
		Window.init(1280, 720, "SkyFight");
		
		GL11.glGenTextures();
		
		AudioManager.init();
		TextManager.init();
		GUIManager.init();
		BlockManager.init();
		EntityManager.init();
		EffectManager.init();
		EventManager.registerEvent(new GUIListener());
		EventManager.registerEvent(new DefaultKeyListener());
		
		new MasterRenderer(new RenderLoop());
		MasterRenderer.begin();
		
		TextManager.generateFont("C:\\Windows\\Fonts\\Arial.ttf", 20f, "Arial", false, false);	
		
				
		MainBeat.init(bh);
	}
	
	public static void stop() {
		AudioManager.cleanUP();
		TextManager.cleanUP();
		MasterRenderer.cleanUP();
		GUIManager.cleanUP();
		Loader.cleanUP();
		Window.closeWindow();
	}

}
