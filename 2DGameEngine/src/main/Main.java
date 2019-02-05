package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import block.Block;
import block.BlockModel;
import block.BlockRenderer;
import events.EventHandler;
import gui.GUI;
import gui.GUIElement;
import gui.GUIHandler;
import gui.GUIRenderer;
import gui.GUITexture;
import gui.GUIType;
import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;
import text.Text;
import text.TextManager;
import text.TextRenderer;

public class Main {

	public static void main(String[] args) {
		
		MasterRenderer mr = new MasterRenderer();

		DisplayManager.createDisplay(1240, 720, "Test", 0, mr);
		
//		MasterRenderer mr = new MasterRenderer();
		BlockRenderer br = new BlockRenderer();
		GUIRenderer gr = new GUIRenderer();
		TextRenderer tr = new TextRenderer();
		mr.addRenderer(br);
		mr.addRenderer(gr);
		mr.addRenderer(tr);
		
		BlockModel bm = new BlockModel(Loader.loadTexture("res/grass.png"), null, 1, 1);
		Block b = new Block(bm, 0, 0, false);
		BlockModel bm1 = new BlockModel(Loader.loadTexture("res/waterDUDV.png"), null, 1, 1);
		Block b1 = new Block(bm1, 1, 1, false);
		br.addRenderBlock(b);
		br.addRenderBlock(b1);
		
		GUITexture gt = new GUITexture(Loader.loadTexture("res/corner.png"), Loader.loadTexture("res/line.png"), Loader.loadTexture("res/background.png"));
		GUIElement g = new GUIElement(new Vector2f(0.1f, 0.1f), new Vector2f(0.5f, 0.5f), gt, GUIType.BACKGROUND);
		GUI gui = new GUI(true);
		gui.addGUIElement(g);
		GUIHandler.addGUI(gui);		
		
		TextManager.loadFont("res/arial.ttf");
		Text t = new Text("Test", new Vector2f(0, 0), 1f, new Vector3f(1, 1, 0), "arial");
		tr.addText(t);
		
		while(!GLFW.glfwWindowShouldClose(DisplayManager.getWINDOW())) {	
			EventHandler.pollEvents();
			
			mr.render();
			
						
			DisplayManager.updateDisplay(false);
		}
		mr.cleanUP();
		Loader.cleanUP();
		DisplayManager.closeDisplay();

	}

}
