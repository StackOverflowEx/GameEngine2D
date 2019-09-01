package de.Luca.Entities;

import org.joml.Vector2f;

public class Model {

	private Texture texture;
	private Vector2f scale;
	
	public Model(Texture texture, Vector2f scale) {
		this.texture = texture;
		this.scale = scale;
	}
	
	public Model(Vector2f scale) {
		this.scale = scale;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
}
