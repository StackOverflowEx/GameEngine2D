package de.Luca.Entities;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Entity {
	
	//Die Weltkoordinaten des Entity
	private Vector2f location;
	//Die Entitydaten (Texture, ...)
	private Model model;
	//Entity roll
	private float roll;
	
	public Entity(Vector2f location, Model model, float roll) {
		this.location = location;
		this.model = model;
		this.roll = roll;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public void addRoll(float droll) {
		roll += droll;
	}

	public Vector2f getLocation() {
		return location;
	}

	public Model getModel() {
		return model;
	}
	
	public void setLocation(Vector2f loc) {
		this.location = loc;
	}
	
	//pvt
	public Vector4f getHitBox() {
		return null;
	}
	

}
