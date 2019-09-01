package de.Luca.Text;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Paragraph {
	
	private ArrayList<Text> texts;
	private int x, y;
	private Vector2f bounds;
	private boolean visible = true;
		
	public Paragraph(int x, int y, String[] lines, long font, Vector4f color) {
		texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		processLines(lines, font, color, TEXT_ALIGN.LEFT);
	}
	
	public Paragraph(int x, int y, String[] lines, long font, Vector4f color, TEXT_ALIGN align) {
		texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		processLines(lines, font, color, align);
	}
	
	public Paragraph(int x, int y, Text[] texts) {
		this.texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		for(Text t : texts) {
			this.texts.add(t);
		}
		calcBounds();
	}
	
	public enum TEXT_ALIGN {
		CENTER,
		RIGHT,
		LEFT
	}
	
	private void calcBounds() {
		float xLargest = 0;
		float y = 0;
		for(Text t : texts) {
			y += t.getBounds().y;
			if(t.getBounds().x > xLargest) {
				xLargest = t.getBounds().x;
			}
		}
		bounds = new Vector2f(xLargest, y);
	}
	
	private void processLines(String[] lines, long font, Vector4f color, TEXT_ALIGN align) {
		int yOffset = 0;
		float longest = 0;
		for(String line : lines) {
			Text t = new Text(font, x, y + yOffset, line, color);
			texts.add(t);
			yOffset += t.getBounds().y;
			if(t.getBounds().x > longest) {
				longest = t.getBounds().x;
			}
		}
		
		bounds = new Vector2f(longest, yOffset);
		
		if(align.equals(TEXT_ALIGN.CENTER)) {
			for(Text t : texts) {
				float width = t.getBounds().x;
				float xOffset = (longest - width) / 2.0f;
				t.setX((int) (t.getX() + xOffset));
			}
		}
		
		if(align.equals(TEXT_ALIGN.RIGHT)) {
			for(Text t : texts) {
				float width = t.getBounds().x;
				float xOffset = (longest - width);
				t.setX((int) (t.getX() + xOffset));
			}
		}
		
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		for(Text t : texts) {
			t.setVisible(visible);
		}
		this.visible = visible;
	}
	
	public Vector2f getBounds() {
		return bounds;
	}
	
	public ArrayList<Text> getTexts(){
		ArrayList<Text> ret = new ArrayList<Text>();
		ret.addAll(texts);
		return ret;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
