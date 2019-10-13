package de.Luca.Loading;

import java.util.UUID;

public class Frame {
	
	//ein Frame
	
	//id
	private UUID frameID;
	//die beiden Buffer
	private float[] verticies;
	private float[] textureCoords;
	//die VAO-ID
	private int vaoID;
	
	public Frame(float[] verticies, float[] textureCoords) {
		this.frameID = UUID.randomUUID();
		this.verticies = verticies;
		this.textureCoords = textureCoords;
		this.vaoID = -1;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public void setVaoID(int vaoID) {
		this.vaoID = vaoID;
	}
	
	public float[] getVerticies() {
		return verticies;
	}
	
	public float[] getTextureCoords() {
		return textureCoords;
	}
	
	public UUID getFrameID() {
		return frameID;
	}
}
