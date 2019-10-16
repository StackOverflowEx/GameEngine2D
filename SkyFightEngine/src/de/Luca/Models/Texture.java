package de.Luca.Models;

import java.nio.ByteBuffer;

public class Texture {
	
	//OpenGL-ID
	private int textureID;
	//Buffer
	private ByteBuffer buffer;
	//size
	private int width, height;
	//type
	private String textureType;
	//File
	private String file;
	
	public Texture(ByteBuffer buffer, int width, int height, String textureType, String file) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
		textureID = -1;
		this.textureType = textureType;
		this.file = file;
	}
	
	public String getFile() {
		return file;
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
	
	//Ist eine Textur erfolgreich geladen, wird der Buffer gelöscht
	public void setTextureID(int textureID) {
		this.textureID = textureID;
		if(buffer != null) {
			buffer.clear();
			buffer = null;
		}
	}
	
	public int getTextureID() {
		return textureID;
	}

}
