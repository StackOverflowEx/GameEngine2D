package de.Luca.GUI;

import org.joml.Vector4f;

import de.Luca.Entities.RenderModel;
import de.Luca.Entities.Texture;

public class GCheckBox extends GUIComponent{
	
	
	private GButton check;
	private GLabel label;
	
	private Texture checkedDefault;
	private Texture checkedHover;
	private Texture checkedPressed;
	
	private Texture[] storeOld;
	private Vector4f oldColor;
	
	private boolean checked;
	
	public GCheckBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		checked = false;
		addClickCallback(new DefaultButtonAnimation());
		
		check = new GButton(x, y, width/6, height);
		label = new GLabel(x + width/6, y, (width/6) * 5, height);
	}
	
	public void setCheckdTextures(Texture checkedDefault, Texture checkedHover, Texture checkedPressed) {
		this.checkedDefault = checkedDefault;
		this.checkedHover = checkedHover;
		this.checkedPressed = checkedPressed;
	}
	
	protected GUIComponent[] getComponents() {
		return new GUIComponent[] {check, label};
	}
	
	public GButton getCheckBoxButton() {
		return check;
	}
	
	public GLabel getCheckBoxLabel() {
		return label;
	}
	
	private void updateTexture(boolean checked) {
		if(checked) {
			storeOld = new Texture[] {check.getDefaultTexture(), check.getHoverTexture(), check.getPressTexture()};
			if(checkedDefault != null) {
				check.setDefaultTexture(checkedDefault);
			}else {
				oldColor = check.getColor();
				check.setColor(new Vector4f(0, 1 , 0, 1));
			}
			if(checkedHover != null) {
				check.setDefaultTexture(checkedHover);
			}
			if(checkedPressed != null) {
				check.setDefaultTexture(checkedPressed);
			}
		}else {
			if(storeOld != null) {
				check.setDefaultTexture(storeOld[0]);
				check.setHoverTexture(storeOld[1]);
				check.setPressTexture(storeOld[2]);
			}
			if(oldColor != null) {
				check.setColor(oldColor);
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
	
	@Override
	protected void addedToGUI(GUI gui) {
		check.setGUI(gui);
		check.setGUI(gui);
	}
	
	@Override
	protected RenderModel genRenderModel() {
		return null;
	}
	
	@Override
	protected void visibleUpdate(boolean visible) {
		check.setVisible(visible);
		label.setVisible(visible);
	}
	
	@Override
	protected void dispose() {
		check.dispose();
		label.dispose();
	}
	
	

}
