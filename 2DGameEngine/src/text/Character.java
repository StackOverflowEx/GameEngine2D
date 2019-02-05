package text;

import org.lwjgl.util.vector.Vector2f;

import models.RawModel;

public class Character {
	
	private int textureID;
	private RawModel model;
	private Vector2f size;
	private Vector2f bearing;
	private int advance;
	
	public Character(int textureID, Vector2f size, Vector2f bearing, int advance, RawModel model) {
		super();
		this.textureID = textureID;
		this.size = size;
		this.bearing = bearing;
		this.advance = advance;
		this.model = model;
	}
	
	public RawModel getModel() {
		return model;
	}

	public int getTextureID() {
		return textureID;
	}

	public Vector2f getSize() {
		return size;
	}

	public Vector2f getBearing() {
		return bearing;
	}

	public int getAdvance() {
		return advance;
	}
	
}
