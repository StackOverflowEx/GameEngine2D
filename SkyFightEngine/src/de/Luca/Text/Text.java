package de.Luca.Text;

import org.joml.Vector4f;

import de.t0b1.freetype_wrapper.classes.Font;
import de.t0b1.freetype_wrapper.classes.Vector2D;

public class Text {
	
	private long font;
	private int x, y;
	private String text;
	private Vector4f color;
	
	public Text(long font, int x, int y, String text, Vector4f color) {
		super();
		this.font = font;
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
	}
	
	public Vector2D getTextSize() {
		return Font.calcTextSize(font, Font.getFontSize(font), Float.MAX_VALUE, -1f, text);
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}

	public long getFont() {
		return font;
	}

	public void setFont(long font) {
		this.font = font;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	

}
