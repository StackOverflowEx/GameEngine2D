package de.Luca.Rendering;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import de.Luca.Window.Window;

public class RenderLoop implements Runnable{
	

	@Override
	public void run() {
		GLFW.glfwMakeContextCurrent(Window.window.getWindowID());
		GL.createCapabilities();
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
		while(!Window.window.shouldClose()) {
			MasterRenderer.masterRenderer.render();
			Window.window.updateWindow();
		}
		Window.window.closeWindow();
	}

}
