package de.Luca.Calculation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import de.Luca.Entities.Entity;
import de.Luca.Loading.BufferLoader;
import de.Luca.Loading.Frame;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Window.Window;

public class MainBeat {
	
	public static List<Entity> entities = new ArrayList<Entity>();

	public static void init() {
//		addEntity(new Entity(new Vector2f(0, 0), new Model(Loader.loadTexture("D:\\Icons\\test.png"), 0.05f), 0));
//		addEntity(new Entity(new Vector2f(-1, -1), new Model(Loader.loadTexture("D:\\Icons\\Icon2T.png"), 2f), 0));
		while(!Window.window.shouldClose()) {
			long start = System.nanoTime();
			GLFW.glfwPollEvents();
			Frame frame = BufferLoader.loadFrameBuffer(entities);
			while((float)((System.nanoTime() - start)) < Window.window.getAvgFrameTime()) {}
			MasterRenderer.masterRenderer.queueFrame(frame);
		}
	}
	
	public static void addEntity(Entity e) {
		synchronized (entities) {
			entities.add(e);
		}
	}
	
	public static void removeEntity(Entity e) {
		synchronized (entities) {
			entities.remove(e);
		}
	}
}
