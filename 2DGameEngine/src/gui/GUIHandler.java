package gui;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import tools.Mouse;

public class GUIHandler {
	
	private static ArrayList<GUI> guis = new ArrayList<GUI>();

	public static void addGUI(GUI gui) {
		guis.add(gui);
	}
	
	public static GUIElement getElementByID(long id) {
		GUIElement ret = null;
		for(GUI gui : guis) {
			for(GUIElement guiElement : gui.getGUIElements()) {
				if(guiElement.getID() == id) {
					ret = guiElement;
					break;
				}
			}
			if(ret != null) {
				break;
			}
		}
		return ret;
	}
	
	public static GUI getGUI(long id) {
		for(GUI gui : guis) {
			if(gui.getID() == id) {
				return gui;
			}
		}
		return null;
	}
	
	public static ArrayList<GUIElement> getAllGuiElements(boolean onlyshown){
		ArrayList<GUIElement> ret = new ArrayList<GUIElement>();
		for(GUI gui : guis) {
			if((gui.isShown() && onlyshown) || !onlyshown) {
				ret.addAll(gui.getGUIElements());
			}
		}
		return ret;
	}
	
	public static ArrayList<GUIElement> getElementsHovering(){
		ArrayList<GUIElement> ret = new ArrayList<GUIElement>();
		for(GUIElement guiElement : getAllGuiElements(true)) {
			Vector2f mouse = Mouse.getWorldPosWithoutCamera();
			Vector2f nn = Mouse.getWorldPosWithoutCamera(new Vector2f(0, 0));;
			Vector2f topleft = Mouse.getWorldPosWithoutCamera(GUIElement.getDisplayCoords(guiElement.getPosition()));		
			Vector2f scale = new Vector2f(guiElement.getSize().x *2, guiElement.getSize().y * 2);
			scale.y = Math.abs(nn.y * scale.y);
			scale.x = Math.abs(nn.x * scale.x);
			Vector2f bottomright = new Vector2f(topleft.x + scale.x, topleft.y - scale.y);
			
			if((mouse.x >= topleft.x) && (mouse.x <= bottomright.x)) {
				if((mouse.y <= topleft.y) && (mouse.y >= bottomright.y)) {
					ret.add(guiElement);
				}
			}
		}
		return ret;
	}
	
}
