package de.Luca.Entities;

import java.nio.ByteBuffer;

public class Texture {
	
	private int textureID;
	private ByteBuffer buffer;
	private int width, height;
	
	public Texture(ByteBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
		textureID = -1;
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
