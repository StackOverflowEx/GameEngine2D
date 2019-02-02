package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import block.Block;
import block.BlockModel;
import block.BlockRenderer;
import gui.GUI;
import gui.GUIElement;
import gui.GUIHandler;
import gui.GUIRenderer;
import gui.GUITexture;
import gui.GUIType;
import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;

public class Main {

	public static void main(String[] args) {
		
		MasterRenderer mr = new MasterRenderer();

		DisplayManager.createDisplay(1240, 720, "Test", 0, mr);
		
//		MasterRenderer mr = new MasterRenderer();
		BlockRenderer br = new BlockRenderer();
		GUIRenderer gr = new GUIRenderer();
//		mr.addRenderer(br);
		mr.addRenderer(gr);
		
		BlockModel bm = new BlockModel(Loader.loadTexture("res/grass.png"), null, 1, 1);
		Block b = new Block(bm, 0, 0, false);
		BlockModel bm1 = new BlockModel(Loader.loadTexture("res/waterDUDV.png"), null, 1, 1);
		Block b1 = new Block(bm1, 1, 1, false);
		br.addRenderBlock(b);
		br.addRenderBlock(b1);
		
		GUITexture gt = new GUITexture(Loader.loadTexture("res/grass.png"), Loader.loadTexture("res/waterDUDV.png"), Loader.loadTexture("res/grass.png"));
		GUIElement g = new GUIElement(new Vector2f(0, 0), new Vector2f(1, 0.5f), gt, GUIType.BACKGROUND);
		GUI gui = new GUI(true);
		gui.addGUIElement(g);
		GUIHandler.addGUI(gui);
		
		while(!GLFW.glfwWindowShouldClose(DisplayManager.getWINDOW())) {			
			mr.render();
			
						
			DisplayManager.updateDisplay(false);
		}
		
		
		DisplayManager.closeDisplay();

	}

}
