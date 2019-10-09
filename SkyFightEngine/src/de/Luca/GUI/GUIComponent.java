package de.Luca.GUI;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Calculation.Camera;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;
import de.Luca.Sound.AudioManager;
import de.Luca.Sound.Source;
import de.Luca.Utils.WorldPosition;

public abstract class GUIComponent {
	
	private int x, y, width, height;
	private boolean visible = true;
	private RenderModel model;
	private Vector4f color;
	private Vector4f currentColor;
	private GUI gui;
	private boolean mouseOn;
	private GUIComponent parent;
	
	private CopyOnWriteArrayList<ClickCallback> clickCallbacks;
	private CopyOnWriteArrayList<HoverCallback> hoverHollbacks;
	private CopyOnWriteArrayList<ScrollCallback> scrollHollbacks;

	public GUIComponent(int x, int y, int width, int height) {
		super();
		clickCallbacks = new CopyOnWriteArrayList<ClickCallback>();
		hoverHollbacks = new CopyOnWriteArrayList<HoverCallback>();
		scrollHollbacks = new CopyOnWriteArrayList<ScrollCallback>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mouseOn = false;
		currentColor = color;
		GUIListener.addComponent(this);
		setRenderModel();
	}
	
	public GUIComponent getParent() {
		return parent;
	}
	
	protected void setParent(GUIComponent parent) {
		this.parent = parent;
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
	
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setRenderModel();
		reCalc();
	}

	public void setX(int x) {
		this.x = x;
		setRenderModel();
		reCalc();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		setRenderModel();
		reCalc();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		setRenderModel();
		reCalc();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		setRenderModel();
		reCalc();
	}
	
	public void addClickCallback(ClickCallback cc) {
		clickCallbacks.add(cc);
	}
	
	public void removeClickCallback(ClickCallback cc) {
		clickCallbacks.remove(cc);
	}
	
	public void click(int key, int action, int mouseX, int mouseY) {
		for(ClickCallback cc : clickCallbacks) {
			if(GUIManager.getClickSound() != null && this.getClass().getSimpleName().equals("GButton")) {
				if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					Source s = AudioManager.genSource();
					Vector2f pos = WorldPosition.getExactWorldPos(Camera.getPosition());
					s.setPosition(pos);
					s.setVolume(0.3f);
					s.playSound(GUIManager.getClickSound());
					AudioManager.deleteWhenFinished(s);
				}
			}
			cc.run(this, key, action, mouseX, mouseY);
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
	}
	
	public void addScrollCallback(ScrollCallback sc) {
		scrollHollbacks.add(sc);
	}
	
	public void removeScrollCallback(ScrollCallback sc) {
		scrollHollbacks.remove(sc);
	}
	
	public void scroll(double xOffset, double yOffset) {
		for(ScrollCallback sc : scrollHollbacks) {
			sc.run(xOffset, yOffset);
		}
	}
	
	protected abstract void reCalc();
	
	public abstract void dispose();

}
