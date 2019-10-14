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
import de.t0b1.freetype_wrapper.classes.Font;

public class GLabel extends GUIComponent{
	
	//Ein GLabel (ähnlich eines JLabels (swing))
	
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
	
	//Set einen Text auf das Label
	public void setText(String text, long font, Vector4f color, TEXT_ALIGN align, int margin, float scale) {
		this.align = align;
		this.margin = margin;
		//Zuerst wird der alte Text, falls vorhanden entfernt
		removeText();
		//Ein neuer Paragraph aus den Zeilen wird erstellt.
		String[] lines = text.split("\n");
		p = new Paragraph(this.getX() + margin, this.getY(), lines, font, color, align, scale);
		p.setVisible(isVisible());

		//die Position des Textes wird berechnet
		calcText();
		
		//Der Paragraph wird zum Render freigegeben
		TextManager.addParagraph(p);
	}
	
	//siehe oben, nur ohne scale
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

	//rekursive Methode um einen Text anzuzeigen, der abgeschnitten wird, falls er zu lang ist.
	public void setTextCut(String text, long font, Vector4f color, TEXT_ALIGN align, int margin, float scale) {
		this.align = align;
		this.margin = margin;
		int maxWith = getWidth() - margin * 2;
		removeText();
		String[] lines = text.split("\n");
		p = new Paragraph(this.getX() + margin, this.getY(), lines, font, color, align, scale);
		p.setVisible(isVisible());
		
		//ist der Text zu lang, wird er abgeschnitten
		if(p.getBounds().x >= maxWith) {
			//wie viel der Paragraph kleiner oder größer als die erlaubte größe ist
			int delta = (int) (maxWith - p.getBounds().y);
			//Fontsize
			int size = (int) Font.getFontSize(font);
			int letters = 1;
			//Entfernt buchstaben abhängig von der Schriftgröße und dem Unterschied
			if(delta > size) {
				letters = delta / size;
			}
			System.out.println("Attempting to remove " + letters + " from String (Length: " + text.length());
			if(text.length() > letters) {
				text = text.substring(0, text.length() - letters);
				//erneuter Aufruf der Methode, wenn der Text trotzdem noch zu lang ist
				setTextCut(text, font, color, align, margin);
				return;
			}
		}

		//Berechnung der Position des Text
		calcText();
		
		
		//Freigabe zum rendern
		TextManager.addParagraph(p);
	}
	
	//siehe oben
	public void setTextCut(String text, long font, Vector4f color, TEXT_ALIGN align, int margin) {
		this.align = align;
		this.margin = margin;
		int maxWith = getWidth() - margin * 2;
		removeText();
		String[] lines = text.split("\n");
		p = new Paragraph(this.getX() + margin, this.getY(), lines, font, color, align);
		p.setVisible(isVisible());
		
		if(p.getBounds().x >= maxWith) {
			int delta = (int) (maxWith - p.getBounds().y);
			int size = (int) Font.getFontSize(font);
			int letters = 1;
			if(delta > size) {
				letters = delta / size;
			}
			System.out.println("Attempting to remove " + letters + " from String (Length: " + text.length());
			if(text.length() > letters) {
				text = text.substring(0, text.length() - letters);
				setTextCut(text, font, color, align, margin);
				return;
			}
		}
		calcText();
		
		TextManager.addParagraph(p);
	}
	
	//gibt den Text auf dem Label zurück
	public String getLabelText() {
		String ret = "";
		if(p != null) {
			for(Text t : p.getTexts()) {
				if(ret.isEmpty()) {
					ret += t.getText();
				}else {
					ret += "\n" + t.getText();
				}
			}
		}
		return ret;
	}
	
	//Berechnet die Position des Textes
	public void calcText() {
		if(p == null) {
			return;
		}
		Vector2f bounds = p.getBounds();
		
		//Linksbündig
		p.setX(this.getX() + margin);
		p.setY((int) (this.getY() - p.getBounds().y));
		
		//Vertical zentriert
		float textHeight = bounds.y;
		float offsetY = getHeight() - textHeight;
		offsetY = offsetY / 2f;
		p.setY((int) (p.getY() + this.getHeight() - offsetY - 3));
		//Zentriert
		if(align == TEXT_ALIGN.CENTER) {
			p.setX((int) (p.getX() + (this.getWidth() - bounds.x) / 2f) - margin);		
		//Rechtsbündig
		}else if(align == TEXT_ALIGN.RIGHT) {
			float textWidth = bounds.x;
			p.setX((int) (p.getX() + this.getWidth() - textWidth) - margin);
		}
		
		//Wird sichtbar gemacht, wenn das GUI sichtbar ist oder das Label sichbar ist.
		p.setVisible(isVisible());

		if(getGUI() != null) {
			p.setX(p.getX() + getGUI().getX());
			p.setY(p.getY() + getGUI().getY());
			p.setVisible(getGUI().isVisible());
		}
		
		
	}
	
	//Setzt den Textalign neu
	public void setTextAlign(TEXT_ALIGN align) {
		this.align = align;
		calcText();
	}
	
	public Paragraph getParagraph() {
		return p;
	}
	
	//Setzt einen schon erstellen Paragraphen dar
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
			p = null;
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
	public void dispose() {
		removeText();
		GUIListener.removeComponent(this);
		if(this.getGUI() != null) {
			this.getGUI().removeComponent(this);
		}
	}

	@Override
	protected void addedToGUI(GUI gui) {
		setVisible(false);
		if(p != null && gui != null) {
			calcText();
			p.setVisible(gui.isVisible());
		}
	}

	@Override
	protected void reCalc() {
		calcText();
	}

}
