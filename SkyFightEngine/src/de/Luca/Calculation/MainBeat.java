package de.Luca.Calculation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import de.Luca.Loading.BufferLoader;
import de.Luca.Loading.Frame;
import de.Luca.Models.RenderModel;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Window.Window;

public class MainBeat {
	
	public static List<RenderModel> entities = new ArrayList<RenderModel>();

	public static void init() {
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
