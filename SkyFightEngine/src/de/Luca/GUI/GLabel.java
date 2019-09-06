package de.Luca.GUI;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Text.Paragraph;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Text.Text;
import de.Luca.Text.TextManager;
import de.Luca.Utils.WorldPosition;
import de.Luca.Window.Window;

public class GLabel extends GUIComponent{
	
	private Paragraph p;
	private TEXT_ALIGN align;
	private int margin;

	public GLabel(int x, int y, int width, int height) {
		super(x, y, width, height);
		setColor(new Vector4f(0, 0, 0, 0));
	}
	
	public TEXT_ALIGN getTextAlign() {
		return align;
	}
	
	public int getMargin() {
		return margin;
	}
	
	public void setText(String text, long font, Vector4f color, TEXT_ALIGN align, int margin) {
		this.align = align;
		this.margin = margin;
		removeText();
		String[] lines = text.split("\n");
		p = new Paragraph(this.getX() + margin, this.getY(), lines, font, color, align);
		p.setVisible(isVisible());

		calcText();
		
		TextManager.addParagraph(p);
	}
	
	public void calcText() {
		if(p == null) {
			return;
		}
		Vector2f bounds = p.getBounds();
		
		p.setX(this.getX() + margin);
		p.setY(this.getY());
		
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

		if(getGUI() != null) {
			p.setX(p.getX() + getGUI().getX());
			p.setY(p.getY() + getGUI().getY());
		}
		
	}
	
	public void setTextAlign(TEXT_ALIGN align) {
		this.align = align;
		calcText();
	}
	
	public Paragraph getParagraph() {
		return p;
	}
	
	public void setText(Paragraph p) {
		String lines = "";
		for(int i = 0; i < p.getTexts().size(); i++) {
			if(i != 0) {
				lines = lines + "\n";
			}
			lines = lines + p.getTexts().get(i).getText();
		}
		Text alphaText = p.getTexts().get(0);
		setText(lines, alphaText.getFont(), alphaText.getColor(), align, margin);
	}
	
	@Override
	protected GUIComponent[] getComponents() {
		return new GUIComponent[] {this};
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
		setVisible(false);
		removeText();
		GUIListener.removeComponent(this);
		this.getGUI().removeComponent(this);
	}

	@Override
	protected void addedToGUI(GUI gui) {
		if(p != null && gui != null) {
			calcText();
		}
	}

	@Override
	protected void reCalc() {
		calcText();
	}

}
