package de.Luca.Main;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.MainBeat;
import de.Luca.EventManager.EventManager;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GPanel;
import de.Luca.GUI.GScrollPanel;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIListener;
import de.Luca.GUI.GUIManager;
import de.Luca.GUI.GScrollPanel.SLIDER_POSITION;
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
//		
		GUI ui = new GUI(100, 100, 200, 300);
//		GDropDown dropDown = new GDropDown(10, 10, 100, 20);
//		dropDown.addElement("Test", TextManager.getFont("Arial"), new Vector4f(1, 1, 1, 1));
//		GCheckBox box = new GCheckBox(10, 10, 50, 50);
//		ui.addComponent(box);
//		GSlider slider = new GSlider(0, 0, 20, 200, true);
//		slider.flipSides();
//		ui.addComponent(slider);
		GLabel label = new GLabel(0, 0, 150, 150);
		label.setColor(new Vector4f(0,0,0,-1));
		label.setTexture(Loader.loadTexture("D:\\Downloads\\PxButton1A.png"));
		GLabel label2 = new GLabel(150, 0, 150, 150);
		label2.setColor(new Vector4f(0, 1, 0, 1));
		GLabel label3 = new GLabel(300, 0, 150, 150);
		label3.setColor(new Vector4f(1, 0, 0, 1));
		GLabel label4 = new GLabel(0, 0, 150, 150);
		label4.setColor(new Vector4f(1, 1, 1, 1));
		GLabel label5 = new GLabel(0, 0, 150, 150);
		label5.setColor(new Vector4f(0, 0, 0, 1));
		
		GPanel panel = new GPanel(0, 0, 600, 150);
		panel.addComponent(label);
		panel.addComponent(label2);
		panel.addComponent(label3);
		
		GScrollPanel sp = new GScrollPanel(0, 0, 400, 400);
		sp.getPanel().setColor(new Vector4f(0, 1, 1, 0.3f));
		sp.setSlider(SLIDER_POSITION.LEFT, 30);
		sp.setSlider(SLIDER_POSITION.TOP, 30);
		sp.addItem(panel);
		sp.addItem(label4);
		sp.addItem(label5);
		ui.addComponent(sp);
		ui.setVisible(true);
		
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
