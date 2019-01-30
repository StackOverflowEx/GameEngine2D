package main;

import org.lwjgl.glfw.GLFW;

import block.Block;
import block.BlockModel;
import block.BlockRenderer;
import models.RawModel;
import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;

public class Main {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay(1240, 720, "Test");
		
		RawModel quad;
		float[] positions = new float[] {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
		quad = Loader.loadToVAO(positions);
		
		MasterRenderer mr = new MasterRenderer();
		BlockRenderer br = new BlockRenderer(quad);
		mr.addRenderer(br);
		
		BlockModel bm = new BlockModel(Loader.loadTexture("res/grass.png"), quad, 1, 1);
		Block b = new Block(bm, 0, 0, false);
		BlockModel bm1 = new BlockModel(Loader.loadTexture("res/waterDUDV.png"), quad, 1, 1);
		Block b1 = new Block(bm1, 1, 1, false);
		br.addRenderBlock(b);
		br.addRenderBlock(b1);
		
		while(!GLFW.glfwWindowShouldClose(DisplayManager.getWINDOW())) {
			//render
			mr.render();
			
			
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();

	}

}
