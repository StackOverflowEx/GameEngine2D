package de.Luca.Entities;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

public class Texture {
	
	private int textureID;
	private ByteBuffer buffer;
	private int width, height;
	private float trimedHeight, trimedWidth;
	
	public Texture(ByteBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
		textureID = -1;
//		trim();
	}
	
//	private void trim() {
//		int mostleft = width;
//		int mostright = 0;
//		for(int i = 0; i < height; i++) {
//			boolean transp = true;
//			int lastNotTransP = 0;
//			for(int n = 3; n < width*4; n += 4) {
//				byte alpha = buffer.get(i*height*4 + n);
//				int x = n / 4 + 1;
//				if(alpha != 0) {
//					lastNotTransP = x;
//					if(transp) {
//						if(x < mostleft) {
//							mostleft = x;
//						}
//					}
//					transp = false;
//				}
//			}
//			if(lastNotTransP > mostright) {
//				mostright = lastNotTransP;
//			}
//			if(transp) {
//				trimedHeight--;
//			}
//		}
//		mostleft--;
//		trimedWidth = mostright - mostleft;
//		
//		trimedHeight = trimedHeight / (float) height;
//		trimedWidth = trimedWidth / (float)width;
//	}
	
	public Vector2f getTrimed() {
		return new Vector2f(trimedWidth, trimedHeight);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public void setTextureID(int textureID) {
		this.textureID = textureID;
		buffer = null;
	}
	
	public int getTextureID() {
		return textureID;
	}

}
