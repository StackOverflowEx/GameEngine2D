package de.Luca.Loading;

import de.Luca.Text.TextManager;

public class BufferLoader {
	
	//eine statische Klasse, in welcher die Buffer f¸r die Frames mit der CPU berechnet werden
	
	private static long last = 0;
	private static int frame = 0;
	
	private static Frame oldFrame;
	
	//VAO: 8-Quad f¸r Rendering mit Texture - Quads f¸r Fontrendering
	public static Frame loadFrameBuffer() {
		
		//Es wird ¸berpr¸ft, ob ein neuer Frame berechnet werden muss
		if(!TextManager.hasChanged() && oldFrame != null) {
			//ist dies nicht der Fall, wird der alte zur¸ck gegeben
			countFrame();
			return oldFrame;
		}
		
		//berechnung der Buffer
		float[] textureCoords;
		float[] verticies;
		
		float[][] buffer = TextManager.getBuffer();
		verticies = combineBuffer(new float[] {-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -1, -1, -1, 1, 1, -1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1 }, buffer[0]);
		textureCoords = combineBuffer(new float[] {0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1 }, buffer[1]);
		
		countFrame();
		frame++;
		
		oldFrame = new Frame(verticies, textureCoords);
		return oldFrame;
		
	}
	
	//Z‰hlt die CPU-Frames um die Anzahl dieser in der Konsole auszugeben
	private static void countFrame() {
		if((System.currentTimeMillis() - last) > 1000) {
			System.out.println("CPU Frames: " + frame);
			last = System.currentTimeMillis();
			frame = 0;
		}
	}
	
	//Combiniert zwei Float-Arrays zu einem groﬂen
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
