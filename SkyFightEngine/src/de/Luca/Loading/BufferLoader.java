package de.Luca.Loading;

import java.util.List;

import de.Luca.Entities.Entity;
import de.Luca.Text.TextManager;

public class BufferLoader {
	
	private static long last = 0;
	private static int frame = 0;
	
	//VAO: 8-Quad für Rendering mit Texture - Quads für Fontrendering
	public static Frame loadFrameBuffer(List<Entity> entities) {
		
		float[] verticies;
//		if(TextManager != null) {
//			float[][] buffer = TextManager.manager.getBuffer();
//			verticies = combineBuffer(new float[] { 0, 0, 0, 1, 1, 0, 1, 1 }, buffer[0]);
//			textureCoords = combineBuffer(new float[] { 0, 1, 0, 0, 1, 1, 1, 0 }, buffer[1]);
//		}else {
//			verticies = new float[] { 0, 0, 0, 1, 1, 0, 1, 1 };
//			textureCoords = new float[] { 0, 1, 0, 0, 1, 1, 1, 0};
//		}
		
		float[][] buffer = TextManager.getBuffer();
		verticies = combineBuffer(new float[] { 0, 0, 0, 1, 1, 0, 1, 1 }, buffer[0]);
//		textureCoords = combineBuffer(new float[] { 0, 1, 0, 0, 1, 1, 1, 0 }, buffer[1]);
		System.out.println("VERTEXBUFFER");
		for(float f : verticies) {
			System.out.print(f + ", ");
		}
		System.out.println();
		
		countFrame();
		
		return new Frame(entities, verticies);
		
	}
	
	private static void countFrame() {
		if((System.currentTimeMillis() - last) > 1000) {
			System.out.println("CPU Frames: " + frame);
			last = System.currentTimeMillis();
			frame = 0;
		}
		frame++;
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
