package de.Luca.GUI;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Entities.Model;
import de.Luca.Entities.RenderModel;
import de.Luca.Text.Paragraph;
import de.Luca.Text.TextManager;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.WorldPosition;
import de.Luca.Window.Window;

public class GLabel extends GUIComponent{
	
	private Paragraph p;

	public GLabel(int x, int y, int width, int height) {
		super(x, y, width, height);
		setColor(new Vector4f(0, 0, 0, 0));
	}
	
	public void setText(String text, long font, Vector4f color, TEXT_ALIGN align, int margin) {
		removeText();
		String[] lines = text.split("\n");
		p = new Paragraph(this.getX() + margin, this.getY(), lines, font, color);
		Vector2f bounds = p.getBounds();
		
		float textHeight = bounds.y;
		float offsetY = getHeight() - textHeight;
		offsetY = offsetY / 2f;
		p.setY((int) (p.getY() + this.getHeight() - offsetY - 3));
		if(align == TEXT_ALIGN.CENTER) {
			p.setX((int) (p.getX() + (this.getWidth() - bounds.x) / 2f) - margin);		
		}else if(align == TEXT_ALIGN.RIGHT) {
			float textWidth = bounds.x;
			p.setX((int) (p.getX() + this.getWidth() - textWidth) - margin);
		}
		
		TextManager.addParagraph(p);
	}
	
	protected GUIComponent[] getComponents() {
		return new GUIComponent[] {null};
	}
	
	public void removeText() {
		if(p != null) {
			TextManager.removeParagraph(p);
		}
	}
	
	@Override
	protected RenderModel genRenderModel() {
		Vector2f windowSize = Window.getWindowSize();
		Vector2f location = WorldPosition.toOpenGLCoords(new Vector2f(this.getX(), this.getY()));
		Vector2f scale = WorldPosition.toOpenGLCoords(new Vector2f(this.getWidth() + windowSize.x/2f, this.getHeight() + windowSize.y/2f));
		
		RenderModel model;
		if(this.getRenderModel() != null) {
			this.getRenderModel().setLocation(location);
			this.getRenderModel().getModel().setScale(scale);
			return this.getRenderModel();
		}else {
			model = new RenderModel(location, new Model(scale), 0);
		}
		
		return model;
	}

	@Override
	protected void visibleUpdate(boolean visible) {
		if(p != null) {
			p.setVisible(visible);
		}
	}

	@Override
	protected void dispose() {
		removeText();
		GUIListener.removeComponent(this);
	}

	@Override
	protected void addedToGUI(GUI gui) {
		if(p != null) {
			p.setX(p.getX() + gui.getX());
			p.setY(p.getY() + gui.getY());
		}
	}

}
