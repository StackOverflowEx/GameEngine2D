package de.Luca.Loading;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.Luca.Entities.Entity;

public class Frame {
	
	private UUID frameID;
	private List<Entity> entities;
	
	public Frame(List<Entity> entities) {
		this.entities = new ArrayList<Entity>();
		this.entities.addAll(entities);
		this.frameID = UUID.randomUUID();
	}
	
	public UUID getFrameID() {
		return frameID;
	}
	
	public List<Entity> getEntities(){
		return entities;
	}
	
	public int getBufferedEntities() {
		return entities.size();
	}

}
