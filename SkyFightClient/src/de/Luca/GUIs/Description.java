package de.Luca.GUIs;

import org.joml.Vector4f;

import de.Luca.GUI.GLabel;
import de.Luca.GUI.GUIComponent;
import de.Luca.Text.TextManager;
import de.Luca.Text.Paragraph.TEXT_ALIGN;

public class Description extends GLabel{
	
	public Description(GUIComponent parent, String description, String fontName, Vector4f textColor) {
		super(parent.getX(), parent.getY(), 0, 0);
		calc(description, fontName, textColor);
	}

	private void calc(String description, String fontName, Vector4f textColor) {
		setText(description, TextManager.getFont(fontName), textColor, TEXT_ALIGN.LEFT, 0);
		int height = (int) getParagraph().getBounds().y;
		setY(getY() - height);
		setWidth((int)getParagraph().getBounds().x + 10);
		setHeight(height);
	}
	
}
