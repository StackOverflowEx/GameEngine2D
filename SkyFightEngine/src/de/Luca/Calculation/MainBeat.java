package de.Luca.Calculation;

import org.lwjgl.glfw.GLFW;

import de.Luca.Loading.BufferLoader;
import de.Luca.Loading.Frame;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Sound.AudioManager;
import de.Luca.Window.Window;

public class MainBeat {
	
	public static void init(BeatHandler bh) {
		bh.init();
		while(!Window.shouldClose()) {
			long start = System.nanoTime();
			GLFW.glfwPollEvents();
			AudioManager.update();
			bh.loop();
			Frame frame = BufferLoader.loadFrameBuffer();
			while((float)((System.nanoTime() - start)) < Window.getAvgFrameTime()) {}
			MasterRenderer.queueFrame(frame);
		}
	}
}
