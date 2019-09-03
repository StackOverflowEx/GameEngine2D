package de.Luca.Main;

import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.MainBeat;
import de.Luca.EventManager.EventManager;
import de.Luca.GUI.GSlider;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIListener;
import de.Luca.GUI.GUIManager;
import de.Luca.Loading.Loader;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Rendering.RenderLoop;
import de.Luca.Text.TextManager;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Window.Window;

public class SkyFightEngine {
	
	public static void init() {
		Window.init(1280, 720, "SkyFight");
		
		GL11.glGenTextures();
		
		TextManager.init();
		GUIManager.init();
		EventManager.registerEvent(new GUIListener());
		EventManager.registerEvent(new DefaultKeyListener());
		
		new MasterRenderer(new RenderLoop());
		MasterRenderer.begin();
		
		TextManager.generateFont("C:\\Windows\\Fonts\\Arial.ttf", 20f, "Arial", false, false);		
//		String[] lines = new String[] {"Das ist die erste Zeile.", "Das die 2.", "Und die letzte"};
//		Main.paragraph = new Paragraph(500, 500, lines, TextManager.getFont("Arial") , new Vector4f(1, 1, 1, 1));
//		TextManager.addParagraph(Main.paragraph);
		
		GUI ui = new GUI(100, 100, 200, 300);
//		GDropDown dropDown = new GDropDown(10, 10, 100, 20);
//		dropDown.addElement("Test", TextManager.getFont("Arial"), new Vector4f(1, 1, 1, 1));
//		ui.addComponent(dropDown);
//		GCheckBox box = new GCheckBox(10, 10, 50, 50);
//		ui.addComponent(box);
		GSlider slider = new GSlider(0, 0, 20, 200, true);
		slider.flipSides();
		ui.addComponent(slider);
		
		MainBeat.init();
	}
	
	public static void stop() {
		TextManager.cleanUP();
		MasterRenderer.cleanUP();
		GUIManager.cleanUP();
		Loader.cleanUP();
		Window.closeWindow();
	}

}
