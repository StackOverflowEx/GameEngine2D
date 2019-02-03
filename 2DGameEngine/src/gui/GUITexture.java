package gui;

import models.Texture;

public class GUITexture {
	
	private Texture topLeftCorner;
	private Texture topLine;
	private Texture background;
	
	/**
	 * 
	 * If topLeftCorner == null or topLine == null the GUIElement will only have one texture
	 * 
	 * @param topLeftCorner
	 * @param topLine
	 * @param background
	 */
	public GUITexture(Texture topLeftCorner, Texture topLine, Texture background) {
		super();
		this.topLeftCorner = topLeftCorner;
		this.topLine = topLine;
		this.background = background;
	}

	public Texture getTopLeftCorner() {
		return topLeftCorner;
	}

	public Texture getTopLine() {
		return topLine;
	}

	public Texture getBackground() {
		return background;
	}
	
	

}
