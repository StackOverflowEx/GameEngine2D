package de.Luca.Rendering;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import de.Luca.Window.Window;

public class RenderLoop implements Runnable{
	

	@Override
	public void run() {
		GLFW.glfwMakeContextCurrent(Window.window.getWindowID());
		GL.createCapabilities();
				
		while(!Window.window.shouldClose()) {
			MasterRenderer.masterRenderer.render();
			Window.window.updateWindow();
		}
		Window.window.closeWindow();
	}

}
