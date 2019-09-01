package de.Luca.GUI;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class DefaultButtonAnimation implements HoverCallback, ClickCallback{

	@Override
	public void run(GUIComponent component, boolean mouseOn) {
		if(component instanceof GButton) {
			GButton button = (GButton) component;
			if(mouseOn) {
				if(button.getHoverTexture() != null) {
					button.setTexture(button.getHoverTexture());
					System.out.println(button.getHoverTexture().getTextureID() + " | "  + button.getDefaultTexture().getTextureID());
				}else {
					button.setCurrentColor(new Vector4f(button.getColor().x() - 0.1f, button.getColor().y() - 0.1f, button.getColor().z() - 0.1f, button.getColor().w()));
				}
			}else {
				if(button.getDefaultTexture() != null) {
					button.setTexture(button.getDefaultTexture());
				}else {
					button.setCurrentColor(button.getColor());
				}
			}
		}
	}

	@Override
	public void run(GUIComponent component, int key, int action) {
		if(component instanceof GButton) {
			GButton button = (GButton) component;
			if(action == GLFW.GLFW_PRESS) {
				if(button.getPressTexture() != null) {
					button.setTexture(button.getPressTexture());
				}else {
					button.setCurrentColor(new Vector4f(button.getColor().x() - 0.2f, button.getColor().y() - 0.2f, button.getColor().z() - 0.2f, button.getColor().w()));
				}
			}else {
				if(button.getHoverTexture() != null) {
					button.setTexture(button.getHoverTexture());
				}else {
					button.setCurrentColor(new Vector4f(button.getColor().x() - 0.1f, button.getColor().y() - 0.1f, button.getColor().z() - 0.1f, button.getColor().w()));
				}
			}
		}
	}

}
