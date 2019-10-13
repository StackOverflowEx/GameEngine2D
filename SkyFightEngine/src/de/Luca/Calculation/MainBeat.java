package de.Luca.Calculation;

import org.lwjgl.glfw.GLFW;

import de.Luca.Loading.BufferLoader;
import de.Luca.Loading.Frame;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Sound.AudioManager;
import de.Luca.Window.Window;

public class MainBeat {
	
	//eine statische Klasse, in der die Spiellogik ausgeführt wird.
	//Es muss ein Beathandler übergeben werden und es werden solange Berechnungen angestellt, solange die Engine läuft.
	
	public static void init(BeatHandler bh) {
		//Init
		bh.init();
		//Solange das Fenster nicht geschlossen wurde
		while(!Window.shouldClose()) {
			long start = System.nanoTime();
			//Es werden GLFW-Effents aktuallisiert (Buttonpress, Windowresize, ...)
			GLFW.glfwPollEvents();
			//Der Audiomangaer wird geupdatet
			AudioManager.update();
			//Loop
			bh.loop();
			//Die CPU berechnet die Vertex- und Texturbuffer für die GPU
			Frame frame = BufferLoader.loadFrameBuffer();
			//Es wird solange gewartet, dass ungefähr so viele Berechnungen pro Sekunde ausgeführt werden, wie die GPU Frames rendert.
			while((float)((System.nanoTime() - start)) < Window.getAvgFrameTime()) {}
			//Der Frame, der von der CPU berechnet wurde, wird dem Render-Thread übergeben
			MasterRenderer.queueFrame(frame);
		}
	}
}
