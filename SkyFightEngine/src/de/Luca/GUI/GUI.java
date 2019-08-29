package de.Luca.GUI;

public abstract class GUI {
	
	private boolean visible;
	
	public GUI(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void hide() {
		visible = false;
	}
	
	public void show() {
		visible = true;
	}
	
	public abstract void render();

}
