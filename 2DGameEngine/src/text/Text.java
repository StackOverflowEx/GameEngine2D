package text;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Text {
	
	private String text;
	private String fontName;
	private Vector2f position;
	private float scale;
	private Vector3f color;
	
	public Text(String text, Vector2f position, float scale, Vector3f color, String fontName) {
		super();
		this.text = text;
		this.position = position;
		this.scale = scale;
		this.color = color;
		this.fontName = fontName;
	}

	public String getText() {
		return text;
	}
	
	public String getFontName() {
		return fontName;
	}

	public Vector2f getPosition() {
		return position;
	}

	public float getScale() {
		return scale;
	}

	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f c) {
		color = c;
	}

}
