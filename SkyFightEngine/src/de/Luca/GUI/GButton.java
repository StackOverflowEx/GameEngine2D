package de.Luca.GUI;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Entities.Model;
import de.Luca.Entities.RenderModel;
import de.Luca.Entities.Texture;
import de.Luca.Text.Paragraph;
import de.Luca.Text.TextManager;
import de.Luca.Utils.WorldPosition;
import de.Luca.Window.Window;

public class GButton extends GUIComponent{
	
	private Paragraph p;
	
	private Texture defaultTexture;
	private Texture hoverTexture;
	private Texture pressTexture;

	public GButton(int x, int y, int width, int height) {
		super(x, y, width, height);
		addClickCallback(new DefaultButtonAnimation());
		addHoverCallback(new DefaultButtonAnimation());
	}
	
	public void setButtonTexture(Texture tex) {
		super.setTexture(tex);
		defaultTexture = tex;
		hoverTexture = tex;
		pressTexture = tex;
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
	
	public void setText(String text, long font, Vector4f color) {
		removeText();
		String[] lines = text.split("\n");
		p = new Paragraph(this.getX(), this.getY(), lines, font, color);
		Vector2f bounds = p.getBounds();
		p.setX((int) (p.getX() + (this.getWidth() - bounds.x) / 2f));
		
		float textHeight = bounds.y;
		float offsetY = getHeight() - textHeight;
		offsetY = offsetY / 2f;
		System.out.println(offsetY + " | " + textHeight + " | " + getHeight());
		p.setY((int) (p.getY() + this.getHeight() - offsetY - 3));
		TextManager.addParagraph(p);
	}
	
	public void removeText() {
		if(p != null) {
			TextManager.removeParagraph(p);
		}
	}
	
	@Override
	public RenderModel genRenderModel() {
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
	public void visibleUpdate(boolean visible) {
		if(p != null) {
			p.setVisible(visible);
		}
	}

	@Override
	public void dispose() {
		removeText();
		GUIListener.removeComponent(this);
	}

	@Override
	public void addedToGUI(GUI gui) {
		if(p != null) {
			p.setX(p.getX() + gui.getX());
			p.setY(p.getY() + gui.getY());
		}
	}

}
