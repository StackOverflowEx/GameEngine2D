package de.Luca.Models;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class RenderModel {
	
	//Die OpenGL-Koordinaten
	private Vector2f location;
	//Die Daten (Texture, Scale)
	private Model model;
	//Entity roll
	private float roll;
	
	public RenderModel(Vector2f location, Model model, float roll) {
		this.location = location;
		this.model = model;
		this.roll = roll;
	}
	
	public void setRoll(float roll) {
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
