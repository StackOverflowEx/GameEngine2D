package de.Luca.Entities;


public class Model {

	private Texture texture;
	private float scale;
	
	public Model(Texture texture, float scale) {
		this.texture = texture;
		this.scale = scale;
	}
	
	public Model(float scale) {
		this.scale = scale;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		if(this.texture == null) {
			this.texture = texture;
		}
	}
	
	public float getScale() {
		return scale;
	}
	
}
