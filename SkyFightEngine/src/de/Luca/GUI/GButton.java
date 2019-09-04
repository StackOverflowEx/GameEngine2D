package de.Luca.GUI;

import org.joml.Vector4f;

import de.Luca.Models.Texture;

public class GButton extends GLabel{
		
	private Texture defaultTexture;
	private Texture hoverTexture;
	private Texture pressTexture;

	public GButton(int x, int y, int width, int height) {
		super(x, y, width, height);
		addClickCallback(new DefaultButtonAnimation());
		addHoverCallback(new DefaultButtonAnimation());
		setColor(new Vector4f(.8f, .8f, .8f, 1));
	}
	
	public void setButtonTexture(Texture tex) {
		super.setTexture(tex);
		defaultTexture = tex;
		hoverTexture = tex;
		pressTexture = tex;
	}
	
	public void setDefaultTexture(Texture tex) {
		this.defaultTexture = tex;
	}
	
	public void setHoverTexture(Texture tex) {
		this.hoverTexture = tex;
	}
	
	public void setPressTexture(Texture tex) {
		this.pressTexture = tex;
	}
	
	public void setButtonTextures(Texture defaultTexture, Texture hoverTexture, Texture pressTexture) {
		this.defaultTexture = defaultTexture;
		this.hoverTexture = hoverTexture;
		this.pressTexture = pressTexture;
		super.setTexture(defaultTexture);
	}
	
	public Texture getDefaultTexture() {
		return defaultTexture;
	}
	
	public Texture getHoverTexture() {
		return hoverTexture;
	}
	
	public Texture getPressTexture() {
		return pressTexture;
	}


}
