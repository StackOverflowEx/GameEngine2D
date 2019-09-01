package de.Luca.GUI;

import org.joml.Vector4f;

import de.Luca.Entities.Texture;
import de.Luca.Text.Paragraph;

public class GButton extends GUIComponent{
	
	private Texture tex;
	private String text;
	private Paragraph p;
	

	public GButton(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void setTexture(Texture tex) {
		this.tex = tex;
	}
	
	public void setText(String text, long font, Vector4f color) {
		String[] lines = text.split("\n");
		p = new Paragraph(this.getX(), this.getY(), lines, font, color);
	}

}
