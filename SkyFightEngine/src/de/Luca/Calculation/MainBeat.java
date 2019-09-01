package de.Luca.Calculation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import de.Luca.Entities.RenderModel;
import de.Luca.Loading.BufferLoader;
import de.Luca.Loading.Frame;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Window.Window;

public class MainBeat {
	
	public static List<RenderModel> entities = new ArrayList<RenderModel>();

	public static void init() {
//		addEntity(new Entity(new Vector2f(0, 0), new Model(Loader.loadTexture("D:\\Icons\\test.png"), 0.05f), 0));
//		addEntity(new Entity(new Vector2f(-1, -1), new Model(Loader.loadTexture("D:\\Icons\\Icon2T.png"), 2f), 0));
		while(!Window.shouldClose()) {
			long start = System.nanoTime();
			GLFW.glfwPollEvents();
			Frame frame = BufferLoader.loadFrameBuffer(entities);
			while((float)((System.nanoTime() - start)) < Window.getAvgFrameTime()) {}
			MasterRenderer.queueFrame(frame);
		}
	}
	
	public static void addEntity(RenderModel e) {
		synchronized (entities) {
			entities.add(e);
		}
	}
	
	public static void removeEntity(RenderModel e) {
		synchronized (entities) {
			entities.remove(e);
		}
	}
}
