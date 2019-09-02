package de.Luca.GUI;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Entities.Texture;

public class GCheckBox extends GButton{
		
	private Texture checkedDefault;
	private Texture checkedHover;
	private Texture checkedPressed;
	
	private Texture[] storeOld;
	private Vector4f oldColor;
	
	private boolean checked;
	
	public GCheckBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		checked = false;
		
		addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action) {
				if(key != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					return;
				}
				GCheckBox box = (GCheckBox) component;
				if(action == GLFW.GLFW_RELEASE) {
					box.setClicked(!box.isChecked());
				}
			}
		});
	}
	
	public void setCheckdTextures(Texture checkedDefault, Texture checkedHover, Texture checkedPressed) {
		this.checkedDefault = checkedDefault;
		this.checkedHover = checkedHover;
		this.checkedPressed = checkedPressed;
	}
	
	protected GUIComponent[] getComponents() {
		return new GUIComponent[] {this};
	}
	
	private void updateTexture(boolean checked) {
		if(checked) {
			storeOld = new Texture[] {getDefaultTexture(), getHoverTexture(), getPressTexture()};
			if(checkedDefault != null) {
				setDefaultTexture(checkedDefault);
			}else {
				oldColor = getColor();
				setColor(new Vector4f(0, 1 , 0, 1));
			}
			if(checkedHover != null) {
				setDefaultTexture(checkedHover);
			}
			if(checkedPressed != null) {
				setDefaultTexture(checkedPressed);
			}
		}else {
			if(storeOld != null) {
				setDefaultTexture(storeOld[0]);
				setHoverTexture(storeOld[1]);
				setPressTexture(storeOld[2]);
			}
			if(oldColor != null) {
				setColor(oldColor);
			}
			oldColor = null;
			storeOld = null;
		}
	}
	
	protected void setClicked(boolean checked) {
		this.checked = checked;
		updateTexture(checked);
	}
	
	public boolean isChecked() {
		return checked;
	}

}
