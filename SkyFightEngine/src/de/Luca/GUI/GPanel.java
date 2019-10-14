package de.Luca.GUI;

import java.util.concurrent.CopyOnWriteArrayList;

import de.Luca.Models.RenderModel;


public class GPanel extends GUIComponent{
	
	//ähnlich einem JPanel
	
	//Komponenten des Panels
	private CopyOnWriteArrayList<GUIComponent> components;
	private int defaultWidht, defaultHeight;
	private int oldX, oldY;

	public GPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.defaultWidht = width;
		this.defaultHeight = height;
		oldX = x;
		oldY = y;
		components = new CopyOnWriteArrayList<GUIComponent>();
	}
	
	//Fügt ein Komponent hinzu
	public void addComponent(GUIComponent c) {
		calc(c);
		c.setGUI(getGUI());
		c.setParent(this);
		c.setVisible(isVisible());
		if(getGUI() != null) {
			c.setVisible(getGUI().isVisible());
		}
		components.add(c);
	}
	
	public void removeAll() {
		for(GUIComponent c : components) {
			removeComponent(c);
		}
	}
	
	
	//Ein Komponent wird entfernt und die Größe des Panels angepasst
	public void removeComponent(GUIComponent c) {
		if(components.contains(c)) {
			
			c.setParent(null);
			components.remove(c);
			int maxHeight = maxHeight();
			if(maxHeight < defaultHeight)
				maxHeight = defaultHeight;
			int maxWidth = maxWidth();
			if(maxWidth < defaultWidht)
				maxWidth = defaultWidht;
			setHeight(maxHeight);
			setHeight(maxWidth);
		}
	}
	
	private int maxHeight() {
		int height = 0;
		for(GUIComponent c : components) {
			if(c.getHeight() + getY() > height) {
				height = c.getHeight() + getY();
			}
		}
		return height;
	}
	
	private int maxWidth() {
		int width = 0;
		for(GUIComponent c : components) {
			if(c.getWidth() + getX() > width) {
				width = c.getWidth() + getX();
			}
		}
		return width;
	}
	
	//position eines Elements wird neu berechnet, damit die Position vom GPanel abhängig ist.
	private void calc(GUIComponent c) {
		if(c.getWidth() + getX() > getWidth()) {
			setWidth(c.getWidth());
		}
		if(c.getHeight() + getY() > getHeight()) {
			setHeight(c.getHeight());
		}
		c.setX(getX() + c.getX());
		c.setY(getY() + c.getY());
	}

	@Override
	protected void addedToGUI(GUI gui) {
		for(GUIComponent component : components) {
			component.setGUI(gui);
		}
	}

	@Override
	protected GUIComponent[] getComponents() {
		GUIComponent[] components = new GUIComponent[this.components.size()];
		for(int i = 0; i < this.components.size(); i++) {
			try {
				components[i] = this.components.get(i);
			}catch (ArrayIndexOutOfBoundsException e) {
				return new GUIComponent[this.components.size()];
			}
		}
		return components;
	}

	@Override
	protected void visibleUpdate(boolean visible) {
		for(GUIComponent c : components) {
			c.setVisible(visible);
		}
	}

	@Override
	protected void reCalc() {
		if(oldX != getX() || oldY != getY()) {
			for(GUIComponent component : components) {
				component.setX(component.getX() + getX() - oldX);
				component.setY(component.getY() + getY() - oldY);
			}
			oldX = getX();
			oldY = getY();
		}
	}

	@Override
	public void dispose() {
		for(GUIComponent c : components) {
			if(c == this) {
				return;
			}
			c.dispose();
		}
		GUIListener.removeComponent(this);
		this.getGUI().removeComponent(this);
		components.clear();
	}

	@Override
	protected RenderModel genRenderModel() {
		return null;
	}

}
