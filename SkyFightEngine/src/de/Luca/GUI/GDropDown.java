package de.Luca.GUI;

import java.util.HashMap;

import org.joml.Vector4f;

import de.Luca.Entities.RenderModel;
import de.Luca.Text.Paragraph.TEXT_ALIGN;

public class GDropDown extends GUIComponent{
	
	private HashMap<String, GButton> elements;
	private String selected;
	private GButton button;
	private int height;
	private boolean open;

	public GDropDown(int x, int y, int width, int height) {
		super(x, y, width, height);
		button = new GButton(x, y, width, height);
		button.setParent(this);
		this.height = height;
		button.setTextAlign(TEXT_ALIGN.CENTER);
		elements = new HashMap<String, GButton>();
		hoverCallback();
		open = false;
	}
	
	public String getSelected() {
		return selected;
	}
	
	public GButton getButton() {
		return button;
	}
	
	public int getRealHeight() {
		return height;
	}
	
	private void hoverCallback() {
		addHoverCallback(new HoverCallback() {
			
			@Override
			public void run(GUIComponent component, boolean mouseOn) {
				if(!mouseOn && open) {
					setOpen(!open);
				}
			}
		});
	}
	
	protected void clicked(GButton button) {
		if(button == this.button) {
			setOpen(!open);
		}else {
			for(String s : elements.keySet()) {
				if(elements.get(s) == button) {
					selected = s;
					this.button.setText(button.getParagraph());
					break;
				}
			}
		}
	}
	
	private void setOpen(boolean open) {
		this.open = open;
		
		for(GButton b : elements.values()) {
			b.setVisible(open);
		}
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void addElement(String e, long font, Vector4f textColor) {
		if(elements.containsKey(e)) {
			throw new IllegalArgumentException("Element already added");
		}
		GButton element = new GButton(getX(), getY() + button.getHeight() * (elements.size() + 1), getWidth(), button.getHeight());
		element.setText(e, font, textColor, TEXT_ALIGN.CENTER, 0);
		element.setParent(this);
		element.setVisible(isVisible() && open);
		element.setGUI(getGUI());
		height += button.getHeight();
		elements.put(e, element);
	}
	
	public void addElement(String e, GButton element) {
		if(elements.containsKey(e)) {
			throw new IllegalArgumentException("Element already added");
		}
		element.setY(getY() + button.getHeight() * (elements.size() + 1));
		element.setWidth(button.getWidth());
		element.setHeight(button.getHeight());
		element.setParent(this);
		element.setVisible(isVisible() && open);
		element.setGUI(getGUI());
		height += button.getHeight();
		elements.put(e, element);
	}
	
	public void removeElement(String e) {
		GButton b = elements.get(e);
		if(b != null) {
			b.dispose();
		}
		height -= button.getHeight();
	}
	
	@Override
	protected GUIComponent[] getComponents() {
		GUIComponent[] ret = new GUIComponent[elements.size() + 1];
		int i = 0;
		for(GButton button : elements.values()) {
			ret[i] = button;
			i++;
		}
		ret[ret.length-1] = button;
		return ret;
	}

	@Override
	protected void addedToGUI(GUI gui) {
		button.setGUI(gui);
		for(GButton elements : elements.values()) {
			elements.setGUI(gui);
		}
	}

	@Override
	protected RenderModel genRenderModel() {
		return null;
	}

	@Override
	protected void visibleUpdate(boolean visible) {
		button.setVisible(visible);
		for(GButton element : elements.values()) {
			element.setVisible(visible);
		}
	}

	@Override
	protected void dispose() {
		button.dispose();
		for(GButton element : elements.values()) {
			element.dispose();
		}
	}

	@Override
	protected void reCalc() {
		button.setX(getX());
		button.setY(getY());
		button.setWidth(getWidth());
		button.setHeight(getHeight());
		
		int i = 0;
		for(GButton button : elements.values()) {
			button.setWidth(getWidth());
			button.setHeight(button.getHeight());
			button.setX(getX());
			button.setY(getY() + this.button.getHeight() * (i + 1));
			i++;
		}
		
		height = getHeight() * (elements.size() + 1);
	}

}
