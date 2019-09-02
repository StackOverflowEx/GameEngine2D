package de.Luca.Main;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.MainBeat;
import de.Luca.EventManager.EventManager;
import de.Luca.GUI.GCheckBox;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIListener;
import de.Luca.GUI.GUIManager;
import de.Luca.Loading.Loader;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Rendering.RenderLoop;
import de.Luca.Text.Paragraph;
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
		String[] lines = new String[] {"Das ist die erste Zeile.", "Das die 2.", "Und die letzte"};
		Main.paragraph = new Paragraph(500, 500, lines, TextManager.getFont("Arial") , new Vector4f(1, 1, 1, 1));
		TextManager.addParagraph(Main.paragraph);
		
		GUI ui = new GUI(10, 10, 200, 300);
		GCheckBox box = new GCheckBox(10, 10, 240, 40);
		box.getCheckBoxLabel().setColor(new Vector4f(1, 1, 1, 1));
		box.setVisible(true);
		ui.addComponent(box);
		
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
