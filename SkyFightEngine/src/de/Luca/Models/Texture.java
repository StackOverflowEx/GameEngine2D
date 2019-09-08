package de.Luca.Models;

import java.nio.ByteBuffer;

public class Texture {
	
	private int textureID;
	private ByteBuffer buffer;
	private int width, height;
	private String textureType;
	
	public Texture(ByteBuffer buffer, int width, int height, String textureType) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
		textureID = -1;
		this.textureType = textureType;
	}
	
	public String getTextureType() {
		return textureType;
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
