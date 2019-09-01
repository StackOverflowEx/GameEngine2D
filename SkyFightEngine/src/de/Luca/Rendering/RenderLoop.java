package de.Luca.Rendering;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import de.Luca.Main.SkyFightEngine;
import de.Luca.Window.Window;

public class RenderLoop implements Runnable{
	

	@Override
	public void run() {
		GLFW.glfwMakeContextCurrent(Window.getWindowID());
		GL.createCapabilities();
				
		while(!Window.shouldClose()) {
			MasterRenderer.render();
			Window.updateWindow();
		}
		SkyFightEngine.stop();
	}

}
