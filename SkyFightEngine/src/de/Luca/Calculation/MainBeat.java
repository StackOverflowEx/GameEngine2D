package de.Luca.Calculation;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Entities.Entity;
import de.Luca.Entities.Model;
import de.Luca.Loading.BufferLoader;
import de.Luca.Loading.Frame;
import de.Luca.Loading.Loader;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Window.Window;

public class MainBeat {
	
	public static List<Entity> entities = new ArrayList<Entity>();

	public static synchronized void init() {
		entities.add(new Entity(new Vector2f(0, 0), new Model(Loader.loadTexture("D:\\Icons\\test.png"), 0.02f), 0));
		while(!Window.window.shouldClose()) {
			GLFW.glfwWaitEvents();
			Frame frame = BufferLoader.loadFrameBuffer(entities);
			MasterRenderer.masterRenderer.queueFrame(frame);
		}
	}
	
}
