package main;

import org.lwjgl.glfw.GLFW;

import block.Block;
import block.BlockModel;
import block.BlockRenderer;
import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;

public class Main {

	public static void main(String[] args) {
		
		MasterRenderer mr = new MasterRenderer();

		DisplayManager.createDisplay(1240, 720, "Test", 0, mr);
		
//		MasterRenderer mr = new MasterRenderer();
		BlockRenderer br = new BlockRenderer();
		mr.addRenderer(br);
		
		BlockModel bm = new BlockModel(Loader.loadTexture("res/grass.png"), null, 1, 1);
		Block b = new Block(bm, 0, 0, false);
		BlockModel bm1 = new BlockModel(Loader.loadTexture("res/waterDUDV.png"), null, 1, 1);
		Block b1 = new Block(bm1, 1, 1, false);
		br.addRenderBlock(b);
		br.addRenderBlock(b1);
		
		
//		TextManager.loadFont("res/arial.ttf");
//		Text t = new Text("Test", new Vector2f(0, 0), 1f, new Vector3f(1, 1, 0), "arial");
//		tr.addText(t);
		
		
		while(!GLFW.glfwWindowShouldClose(DisplayManager.getWINDOW())) {				
			mr.render();
						
			DisplayManager.updateDisplay(false);
		}
		mr.cleanUP();
		Loader.cleanUP();
		DisplayManager.closeDisplay();

	}

}
