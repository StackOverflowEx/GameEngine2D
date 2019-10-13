package de.Luca.GUI;


import java.util.ArrayList;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Models.RenderModel;

public class GSlider extends GUIComponent{
	
	//ein Slider

	private GButton slider;
	private GLabel label;
	private boolean flip;
	private boolean vertical;
	private float value;
	private float sliderScale;
	private boolean selected;
	private ArrayList<ValueChangeCallback> callbacks;
	
	public GSlider(int x, int y, int width, int height, boolean vertical) {
		super(x, y, width, height);
		callbacks = new ArrayList<ValueChangeCallback>();
		flip = false;
		slider = new GButton(0, 0, 0, 0);
		label = new GLabel(0, 0, 0, 0);
		slider.setParent(this);
		label.setParent(this);
		label.setColor(new Vector4f(0.9f, 0.9f, 0.9f, 1f));
		flip = false;
		sliderScale = 0.1f;
		value = 0;
		this.vertical = vertical;
		calc();
		addCallbacks();
	}
	
	public void addValueChangeCallback(ValueChangeCallback vcc) {
		callbacks.add(vcc);
	}
	
	public void removeValueChangeCallback(ValueChangeCallback vcc) {
		callbacks.remove(vcc);
	}
	
	private void fireCallbacks(float value) {
		for(ValueChangeCallback vcc : callbacks) {
			vcc.run(value);
		}
	}
	
	public GLabel getLabel() {
		return label;
	}
	
	public GButton getSlider() {
		return slider;
	}
	
	protected boolean isSelected() {
		return selected;
	}
	
	protected void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setSliderPercentage(int percentage) {
		if(percentage > 100)
			percentage = 100;
		if(percentage < 0)
			percentage = 0;
		

		sliderScale = percentage / 100f;
		calc();
	}
	
	private void addCallbacks() {
		addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					int guiSubX = 0;
					int guiSubY = 0;
					if(getGUI() != null) {
						guiSubX = getGUI().getX();
						guiSubY = getGUI().getY();
					}
					setValue(getValue(mouseX - guiSubX, mouseY - guiSubY));
				}
			}
		});
		
		slider.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					if(action == GLFW.GLFW_PRESS) {
						selected = true;
					}else {
						selected = false;
					}
				}
			}
		});
		addScrollCallback(new ScrollCallback() {
			
			@Override
			public void run(double xOffset, double yOffset) {
				float add = (float) (sliderScale * yOffset) / 2f;
				if(vertical) {
					if(flip) {
						setValue(getValue() + add);
					}else {
						setValue(getValue() - add);
					}
				}else {
					if(flip) {
						setValue(getValue() - add);
					}else {
						setValue(getValue() + add);
					}
				}
			}
		});
	}
	
	public float getSliderScale() {
		return sliderScale;
	}
	
	public boolean isVertical() {
		return vertical;
	}
	
	public boolean isFlipped() {
		return flip;
	}
	
	public void flipSides() {
		flip = true;
		calc();
	}
	
	//Berechnet den Schieber und das Label
	private void calc() {
		int x = getX();
		int y = getY();
		int width = getWidth();
		int height = getHeight();
		label.setBounds(x, y, width, height);
		if(!flip) {
			if(vertical) {
				slider.setBounds(x, y, width, (int)(height * sliderScale));
			}else {
				slider.setBounds(x, y, (int)(width * sliderScale), height);
			}
		}else {
			if(vertical) {
				slider.setBounds(x, (int)(y + (height * sliderScale) * 9f), width, (int)(height * sliderScale));
			}else {
				slider.setBounds((int)(x + (width * sliderScale) * 9f), y, (int)(width * sliderScale), height);
			}
		}
		setValue(value);
	}
	
	//berechnet, wo der Schieber für einen Wert stehen muss
	public void setValue(float f) {
		float value = f;
		if(value > 1)
			value = 1;
		if(value < 0)
			value = 0;
		
		float travelValue;
		float sliderTravel;
		if(vertical) {
			travelValue = getHeight() - getHeight()*sliderScale;
		}else {
			travelValue = getWidth() - getWidth()*sliderScale;
		}
		sliderTravel = value * travelValue;
		
		if(vertical) {
			if(flip) {
				slider.setY(getY() + (int) (travelValue - sliderTravel));
			}else {
				slider.setY(getY() + (int) (sliderTravel));
			}
		}else {
			if(flip) {
				slider.setX(getX() + (int) (travelValue - sliderTravel));
			}else {
				slider.setX(getX() + (int) (sliderTravel));
			}
		}		
		this.value = value;
		fireCallbacks(value);
	}
	
	public float getValue() {
		return value;
	}
	
	
	//Gibt zurück, welchen Wert der Slider an einer bestimmten Position hat.
	protected float getValue(int x, int y) {
		
		if(sliderScale == 1) {
			return 0;
		}
		
		float addX = (slider.getWidth()/2);
		float addY = (slider.getHeight()/2);
		float get;
		float travelValue;
		float sliderTravel;
		if(vertical) {
			get = y;
			travelValue = getHeight() - getHeight()*sliderScale;
			sliderTravel = get - getY() - addY;
		}else {
			get = x;
			travelValue = getWidth() - getWidth()*sliderScale;
			sliderTravel = get - getX() - addX;
		}
		if(sliderTravel < 0) {
			sliderTravel = 0;
		}
		float value = sliderTravel / travelValue;
		if(flip) {
			value = 1 - value;
		}
//		System.out.println(x + " | " + y);
//		System.out.println(get + " " + travelValue + " " + sliderTravel + " " + value);
		return value;
	}

	@Override
	protected void addedToGUI(GUI gui) {
		slider.setGUI(gui);
		label.setGUI(gui);
	}

	@Override
	protected GUIComponent[] getComponents() {
		return new GUIComponent[] {label, slider};
	}

	@Override
	protected RenderModel genRenderModel() {
		return null;
	}

	@Override
	protected void visibleUpdate(boolean visible) {
		slider.setVisible(visible);
		label.setVisible(visible);
	}

	@Override
	protected void reCalc() {
		calc();
	}

	@Override
	public void dispose() {
		setVisible(false);
		slider.dispose();
		label.dispose();
	}

}
