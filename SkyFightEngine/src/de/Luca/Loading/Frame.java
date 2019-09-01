package de.Luca.Loading;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.Luca.Entities.RenderModel;

public class Frame {
	
	private UUID frameID;
	private List<RenderModel> entities;
	private float[] verticies;
	private float[] textureCoords;
	private int vaoID;
	
	public Frame(List<RenderModel> entities, float[] verticies, float[] textureCoords) {
		this.entities = new ArrayList<RenderModel>();
		this.entities.addAll(entities);
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
	
	public List<RenderModel> getEntities(){
		return entities;
	}
	
	public int getBufferedEntities() {
		return entities.size();
	}

}
