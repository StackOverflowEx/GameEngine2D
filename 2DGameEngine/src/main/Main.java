package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import block.Block;
import block.BlockModel;
import block.BlockRenderer;
import gui.GUI;
import gui.GUIRenderer;
import gui.GUITexture;
import gui.GUIType;
import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;

public class Main {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay(1240, 720, "Test");
		
		MasterRenderer mr = new MasterRenderer();
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
		GUI g = new GUI(new Vector2f(0, 0), new Vector2f(1, 1), gt, GUIType.BACKGROUND);
		gr.addGUI(g);
		//EventHandler.registerListener(new CameraEvent());
		
		while(!GLFW.glfwWindowShouldClose(DisplayManager.getWINDOW())) {
			//render
			mr.render();
			
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();

	}

}
