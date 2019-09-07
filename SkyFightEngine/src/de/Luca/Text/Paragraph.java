package de.Luca.Text;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Paragraph {
	
	private ArrayList<Text> texts;
	private int x, y;
	private Vector2f bounds;
	private boolean visible = true;
	private TEXT_ALIGN align;
		
	public Paragraph(int x, int y, String[] lines, long font, Vector4f color) {
		texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		processLines(lines, font, color, TEXT_ALIGN.LEFT, 1.0f);
		this.align = TEXT_ALIGN.LEFT;
	}
	
	public Paragraph(int x, int y, String[] lines, long font, Vector4f color, float scale) {
		texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		processLines(lines, font, color, TEXT_ALIGN.LEFT, scale);
		this.align = TEXT_ALIGN.LEFT;
	}
	
	public Paragraph(int x, int y, String[] lines, long font, Vector4f color, TEXT_ALIGN align, float scale) {
		texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		processLines(lines, font, color, align, scale);
		this.align = align;
	}
	
	public Paragraph(int x, int y, String[] lines, long font, Vector4f color, TEXT_ALIGN align) {
		texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		processLines(lines, font, color, align, 1.0f);
		this.align = align;
	}
	
	public Paragraph(int x, int y, Text[] texts, TEXT_ALIGN align) {
		this.texts = new ArrayList<Text>();
		this.x = x;
		this.y = y;
		for(Text t : texts) {
			this.texts.add(t);
		}
		this.align = align;
		calcBounds();
	}
	
	public Paragraph clone() {
		Text[] t = new Text[texts.size()];
		for(int i = 0; i < t.length; i++) {
			t[i] = texts.get(i);
		}
		return new Paragraph(x, y, t, align);
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
	
	private void processLines(String[] lines, long font, Vector4f color, TEXT_ALIGN align, float scale) {
		int yOffset = 0;
		float longest = 0;
		for(String line : lines) {
			Text t = new Text(font, x, y + yOffset, line, color, scale);
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
		processTexts();
	}
	
	public void setY(int y) {
		this.y = y;
		processTexts();
	}
	
	public void setPosition(Vector2f pos) {
		this.x = (int) pos.x;
		this.y = (int) pos.y;
		processTexts();
	}
	
	private void processTexts() {
		int yOffset = 0;
		float longest = 0;
		for(Text line : texts) {
			line.setX(x);
			line.setY(y + yOffset);
			yOffset += line.getBounds().y;
			if(line.getBounds().x > longest) {
				longest = line.getBounds().x;
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
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
