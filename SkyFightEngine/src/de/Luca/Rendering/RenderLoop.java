package de.Luca.Rendering;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import de.Luca.Main.SkyFightEngine;
import de.Luca.Window.Window;

public class RenderLoop implements Runnable{
	
	//Runnable für den Render-Thread

	@Override
	public void run() {
		//Kontext wird an den Render-Thread gebunden
		GLFW.glfwMakeContextCurrent(Window.getWindowID());
		GL.createCapabilities();
				
		while(!Window.shouldClose()) {
			MasterRenderer.render();
			Window.updateWindow();
		}
		//SkyFightEngine wird beendet, wenn das Fenster geschlossen wird.
		SkyFightEngine.stop();
	}

}
