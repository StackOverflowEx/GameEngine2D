package gui;

import java.util.ArrayList;

public class GUI {
	
	private static long counter = 0;
	private boolean shown;
	private ArrayList<GUIElement> guielements;
	private long id;
	
	public GUI(boolean shown) {
		this.shown = shown;
		this.id = counter;
		counter++;
		guielements = new ArrayList<GUIElement>();
	}
	
	public long getID() {
		return id;
	}
	
	public void addGUIElement(GUIElement guiElement) {
		guiElement.setParent(id);
		guielements.add(guiElement);
	}
	
	public void removeGUIElement(GUIElement guiElement) {
		guielements.remove(guiElement);
	}
	
	public void setShown(boolean shown) {
		this.shown = shown;
	}
	
	public boolean isShown() {
		return shown;
	}
	
	public ArrayList<GUIElement> getGUIElements(){
		return guielements;
	}

}
