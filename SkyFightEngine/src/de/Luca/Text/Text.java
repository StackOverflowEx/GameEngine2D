package de.Luca.Text;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Utils.WorldPosition;
import de.t0b1.freetype_wrapper.classes.Font;
import de.t0b1.freetype_wrapper.classes.Vector2D;

public class Text {
	
	//eine Zeiel
	
	
	//Pointer der Font f�r den FreeTypeWrapper
	private long font;
	//Position
	private int x, y;
	//Open-GL Position
	private float openGLx, openGLy;
	private String text;
	private Vector4f color;
	//size
	private Vector2D bounds;
	private boolean visible;
	private float scale;
	
	public Text(long font, int x, int y, String text, Vector4f color) {
		super();
		this.font = font;
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		this.visible = true;
		this.scale = 1.0f;
		
		bounds = Font.calcTextSize(font, Font.getFontSize(font), Float.MAX_VALUE, -1f, text);
		
		Vector2f wc = WorldPosition.toOpenGLCoords(new Vector2f(x, y));
		openGLx = wc.x();
		openGLy = wc.y();
	}
	
	public Text(long font, int x, int y, String text, Vector4f color, float scale) {
		super();
		this.font = font;
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		this.visible = true;
		this.scale = 1.0f;
		
		bounds = Font.calcTextSize(font, Font.getFontSize(font), Float.MAX_VALUE, -1f, text);
		setScale(scale);
		
		Vector2f wc = WorldPosition.toOpenGLCoords(new Vector2f(x, y));
		openGLx = wc.x();
		openGLy = wc.y();
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		bounds.x = bounds.x * scale;
		bounds.y = bounds.y * scale;
	}
	
	public float getScale() {
		return scale;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible; 
		TextManager.setChanged();
	}
	
	public Vector2D getBounds() {
		return bounds;
	}	
	
	
	public Vector2D getTextSize() {
		return bounds;
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
	
	public Vector2f getOpenGLPos() {
		return new Vector2f(openGLx, openGLy);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		Vector2f wc = WorldPosition.toOpenGLCoords(new Vector2f(x, y));
		openGLx = wc.x();
	}

	public int getY() {
		return y;
	}
	
	public void reCalcOpenGL() {
		Vector2f wc = WorldPosition.toOpenGLCoords(new Vector2f(x, y));
		openGLx = wc.x();
		openGLy = wc.y();
	}

	public void setY(int y) {
		this.y = y;
		Vector2f wc = WorldPosition.toOpenGLCoords(new Vector2f(x, y));
		openGLy = wc.y();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		bounds = Font.calcTextSize(font, Font.getFontSize(font), Float.MAX_VALUE, -1f, text);
	}
	

}
