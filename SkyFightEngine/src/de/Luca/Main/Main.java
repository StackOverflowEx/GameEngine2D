package de.Luca.Main;

import org.joml.Vector4f;

import de.Luca.Calculation.MainBeat;
import de.Luca.EventManager.EventManager;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Rendering.RenderLoop;
import de.Luca.Text.Paragraph;
import de.Luca.Text.TextManager;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Window.Window;

public class Main {
	
	public static Paragraph paragraph;
	
	public static void main(String[] args) {	
		new Window(1280, 720, "SkyFight");
		TextManager.init();
		new EventManager();
		
		new MasterRenderer(new RenderLoop());
		
		MasterRenderer.masterRenderer.start();
		EventManager.eventMangaer.registerEvent(new DefaultKeyListener());
		
		TextManager.generateFont("C:\\Windows\\Fonts\\Arial.ttf", 40f, "Arial", false, false);		
		String[] lines = new String[] {"Das ist die erste Zeile.", "Das die 2.", "Und die letzte"};
		paragraph = new Paragraph(500, 500, lines, TextManager.getFont("Arial") , new Vector4f(1, 1, 1, 1));
		TextManager.addParagraph(paragraph);
		
		MainBeat.init();
		
	}

}
