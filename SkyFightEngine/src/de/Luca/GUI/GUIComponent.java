package de.Luca.GUI;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Entities.RenderModel;
import de.Luca.Entities.Texture;
import de.Luca.Window.Window;

public abstract class GUIComponent {
	
	private int x, y, width, height;
	private boolean visible = true;
	private RenderModel model;
	private Vector4f color;
	private Vector4f currentColor;
	private GUI gui;
	private boolean mouseOn;
	
	private CopyOnWriteArrayList<ClickCallback> clickCallbacks;
	private CopyOnWriteArrayList<HoverCallback> hoverHollbacks;

	public GUIComponent(int x, int y, int width, int height) {
		super();
		clickCallbacks = new CopyOnWriteArrayList<ClickCallback>();
		hoverHollbacks = new CopyOnWriteArrayList<HoverCallback>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mouseOn = false;
		currentColor = color;
		GUIListener.addComponent(this);
		setRenderModel();
	}
	
	public boolean isMouseOn() {
		return mouseOn;
	}
	
	public void setTexture(Texture tex) {
		model.getModel().setTexture(tex);
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
		addedToGUI(gui);
	}
	
	protected abstract void addedToGUI(GUI gui);
	
	public Vector4f getCurrentColor() {
		return currentColor;
	}
	
	public void setCurrentColor(Vector4f color) {
		this.currentColor = color;
	}
	
	public GUI getGUI() {
		return gui;
	}
	
	public Vector4f getColor() {
		return color;
	}
		
	public void setColor(Vector4f color) {
		this.color = color;
		currentColor = color;
	}
	
	public RenderModel getRenderModel() {
		return model;
	}
	
	public void setRenderModel() {
		this.model = genRenderModel();
	}
	
	protected abstract GUIComponent[] getComponents();
	
	protected abstract RenderModel genRenderModel();
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		visibleUpdate(visible);
	}
	
	protected abstract void visibleUpdate(boolean visible);

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void addClickCallback(ClickCallback cc) {
		clickCallbacks.add(cc);
	}
	
	public void removeClickCallback(ClickCallback cc) {
		clickCallbacks.remove(cc);
	}
	
	public void click(int key, int action) {
		for(ClickCallback cc : clickCallbacks) {
			cc.run(this, key, action);
		}
	}
	
	public void addHoverCallback(HoverCallback hc) {
		hoverHollbacks.add(hc);
	}
	
	public void removeHoverCallback(HoverCallback hc) {
		hoverHollbacks.remove(hc);
	}
	
	public void hover(boolean mouseOn) {
		if(mouseOn == this.mouseOn) {
			return;
		}
		this.mouseOn = mouseOn;
		for(HoverCallback cc : hoverHollbacks) {
			cc.run(this, mouseOn);
		}
		if(mouseOn && GLFW.glfwGetMouseButton(Window.getWindowID(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
			click(GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS);
		}
	}
	
	protected abstract void dispose();

}
