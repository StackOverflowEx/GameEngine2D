package de.Luca.Loading;

import de.Luca.Text.TextManager;

public class BufferLoader {
	
	private static long last = 0;
	private static int frame = 0;
	
	private static Frame oldFrame;
	
	//VAO: 8-Quad für Rendering mit Texture - Quads für Fontrendering
	public static Frame loadFrameBuffer() {
		
		if(!TextManager.hasChanged() && oldFrame != null) {
			countFrame();
			return oldFrame;
		}
		
		float[] textureCoords;
		float[] verticies;
		
		float[][] buffer = TextManager.getBuffer();
		// -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f
		verticies = combineBuffer(new float[] {-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -1, -1, -1, 1, 1, -1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1 }, buffer[0]);
		textureCoords = combineBuffer(new float[] {0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1 }, buffer[1]);
		
		countFrame();
		frame++;
		
		oldFrame = new Frame(verticies, textureCoords);
		return oldFrame;
		
	}
	
	private static void countFrame() {
		if((System.currentTimeMillis() - last) > 1000) {
			System.out.println("CPU Frames: " + frame);
			last = System.currentTimeMillis();
			frame = 0;
		}
	}
	
	private static float[] combineBuffer(float[] first, float[] second) {
		float[] ret = new float[first.length + second.length];
		for(int i = 0; i < first.length; i++) {
			ret[i] = first[i];
		}
		for(int i = 0; i < second.length; i++) {
			ret[i + first.length] = second[i];
		}
		return ret;
	}

}
