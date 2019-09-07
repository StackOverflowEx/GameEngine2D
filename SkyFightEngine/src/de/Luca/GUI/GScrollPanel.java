package de.Luca.GUI;

import java.util.ArrayList;

import de.Luca.Models.RenderModel;

public class GScrollPanel extends GUIComponent{
	
	private GPanel panel;
	private GSlider sliderLR;
	private GSlider sliderTB;
	private ArrayList<GUIComponent> items;
	
	public GScrollPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		items = new ArrayList<GUIComponent>();
		panel = new GPanel(x, y, width, height);
		sliderLR = new GSlider(0, 0, 0, height, true);
		sliderLR.setSliderPercentage(100);
		sliderTB = new GSlider(0, 0, width, 0, false);
		sliderTB.setSliderPercentage(100);
		sliderLR.setParent(this);
		sliderTB.setParent(this);
		panel.setParent(this);
		setCallbacks();
		calcPanel();
	}
	
	public enum SLIDER_POSITION {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT
	}
	
	private void setCallbacks(){
		sliderLR.addValueChangeCallback(new ValueChangeCallback() {
			
			@Override
			public void run(float value) {
				calcPanel();
			}
		});
		sliderTB.addValueChangeCallback(new ValueChangeCallback() {
			
			@Override
			public void run(float value) {
				calcPanel();
			}
		});
		addScrollCallback(new ScrollCallback() {
			
			@Override
			public void run(double xOffset, double yOffset) {
				float add = (float) (sliderLR.getSliderScale() * yOffset) / 2f;
				if(sliderLR.isVertical()) {
					if(sliderLR.isFlipped()) {
						sliderLR.setValue(sliderLR.getValue() + add);
					}else {
						sliderLR.setValue(sliderLR.getValue() - add);
					}
				}else {
					if(sliderLR.isFlipped()) {
						sliderLR.setValue(sliderLR.getValue() - add);
					}else {
						sliderLR.setValue(sliderLR.getValue() + add);
					}
				}
			}
		});
	}
	
	private int getHightAll() {
		int height = 0;
		for(GUIComponent c : items) {
			height += c.getHeight();
		}
		return height;
	}
	
	private int getWidthAll() {
		int width = 0;
		for(GUIComponent c : items) {
			if(c.getWidth() > width) {
				width = c.getWidth();
			}
		}
		
		return width;
	}
	
	private void calcPanel() {
		ArrayList<GUIComponent> added = new ArrayList<GUIComponent>();
		int height = 0;
		int yHeight = 0;
		
		int skippedHeight = (int) ((getHightAll() - (getHeight() - sliderTB.getHeight())) * sliderLR.getValue());
				
		for(GUIComponent item : items) {
						
			item.setY(0);
			item.setX(0);
			
			if(height < skippedHeight) {
				height += item.getHeight();
				continue;
			}
			
			if((getHeight() - sliderTB.getHeight()) < (yHeight + item.getHeight() - skippedHeight)) {
				break;
			}
			
			item.setY(item.getY() + (yHeight));
			added.add(item);
			height += item.getHeight();
			yHeight += item.getHeight();
		}
		
		int skippedWidth = (int) ((getWidthAll() - (getWidth() - sliderLR.getWidth())) * sliderTB.getValue());
		for(GUIComponent add : added) {
			int width = 0;
			int subWidth = 0;
			if(add.getWidth() > getWidth() - sliderLR.getWidth()) {
				GUIComponent[] compos = add.getComponents();
				for(int i = 0; i < compos.length; i++) {
					if(width < skippedWidth) {
						width += compos[i].getWidth();
						compos[i].setVisible(false);
						continue;
					}
					width += compos[i].getWidth();
					if((getWidth() - sliderLR.getWidth()) < (subWidth + compos[i].getWidth())) {
						compos[i].setVisible(false);
					}else {
						compos[i].setVisible(true);
					}
					subWidth += compos[i].getWidth();
					
				}
			}
			add.setX(-(width - subWidth));
		}
		
		panel.removeAll();
		for(GUIComponent c : added) {
			if(sliderLR.getWidth() != 0 && sliderLR.getX() == 0) {
				c.setX(c.getX() + sliderLR.getWidth());
			}
			
			if(sliderTB.getHeight() != 0 && sliderTB.getY() == 0) {
				c.setY(c.getY() + sliderTB.getHeight());
			}
			panel.addComponent(c);
		}
		
	}
	
	public ArrayList<GUIComponent> getItems(){
		return items;
	}
	
	public void addItem(GUIComponent item) {
		items.add(item);
		item.setGUI(getGUI());
		calcSliderPercentage();		
	}
	
	private void calcSliderPercentage() {
		int width = getWidthAll();
		int height = getHightAll();
		sliderTB.setSliderPercentage((int) (((getWidth() - sliderLR.getWidth()) / (float)width) * 100f));
		sliderLR.setSliderPercentage((int) (((getHeight() - sliderTB.getHeight()) / (float)height) * 100f));
	}
	
	public void removeItem(GUIComponent item) {
		items.remove(item);
	}
	
	public void setSlider(SLIDER_POSITION sliderPos, int thickness) {
		if(sliderPos == SLIDER_POSITION.LEFT || sliderPos == SLIDER_POSITION.RIGHT) {
			sliderLR.setWidth(thickness);
			panel.setWidth(getWidth() - thickness);
		}else {
			sliderTB.setHeight(thickness);
			panel.setHeight(getHeight() - thickness);
		}
		
		if(sliderPos == SLIDER_POSITION.LEFT) {
			sliderLR.setX(0);
			sliderLR.setY(0);
		}
		
		if(sliderPos == SLIDER_POSITION.RIGHT) {
			sliderLR.setX(getWidth() - thickness);
			sliderLR.setY(0);
		}
		
		if(sliderPos == SLIDER_POSITION.TOP) {
			sliderTB.setX(0);
			sliderTB.setY(0);
		}
		
		if(sliderPos == SLIDER_POSITION.BOTTOM) {
			sliderTB.setX(0);
			sliderTB.setY(getHeight() - thickness);
		}
		
		if(sliderLR.getX() == 0 && sliderLR.getWidth() != 0) {
			sliderTB.setX(sliderLR.getWidth());
			sliderTB.setWidth(getWidth() - sliderLR.getWidth());
		}else if(sliderLR.getX() != 0 && sliderLR.getWidth() != 0) {
			sliderTB.setWidth(getWidth() - sliderLR.getWidth());
		}
		calcSliderPercentage();
		
	}
	
	public GSlider getSliderLR() {
		return sliderLR;
	}
	
	public GSlider getSliderTB() {
		return sliderTB;
	}
	
	public GPanel getPanel() {
		return panel;
	}

	@Override
	protected void addedToGUI(GUI gui) {
		panel.setGUI(gui);
		sliderLR.setGUI(gui);
		sliderTB.setGUI(gui);
	}

	@Override
	protected GUIComponent[] getComponents() {
		GUIComponent[] compos = new GUIComponent[] {panel, sliderLR, sliderTB};
		return compos;
	}

	@Override
	protected RenderModel genRenderModel() {
		return null;
	}

	@Override
	protected void visibleUpdate(boolean visible) {
		panel.setVisible(visible);
		sliderLR.setVisible(visible);
		sliderTB.setVisible(visible);
	}

	@Override
	protected void reCalc() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		panel.dispose();
		sliderLR.dispose();
		sliderTB.dispose();
	}
	

}
