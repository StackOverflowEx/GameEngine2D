package models;

public class TexturedModel {
	
	private Texture texture;
	private RawModel model;
	
	public TexturedModel(Texture texture, RawModel model) {
		this.texture = texture;
		this.model = model;
	}

	public Texture getTexture() {
		return texture;
	}

	public RawModel getModel() {
		return model;
	}
	

}
