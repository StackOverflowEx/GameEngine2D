package de.Luca.GUI;

import java.util.ArrayList;

import de.Luca.Models.RenderModel;

public class GScrollPanel extends GUIComponent{
	
	//ein Scrollpanel
	
	//panel
	private GPanel panel;
	//Slider Links oder Rechts
	private GSlider sliderLR;
	//Slider oben oder Unten
	private GSlider sliderTB;
	
	//Sliderposition Links oder Rechts und Oben oder Unten
	private SLIDER_POSITION LR, TB;
	//Dicke der Slider
	private int LRT, TBT;
	
	//Elemente des Scrollpanels
	private ArrayList<GUIComponent> items;
	
	public GScrollPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		items = new ArrayList<GUIComponent>();
		panel = new GPanel(x, y, width, height);
		sliderLR = new GSlider(x, y, 0, height, true);
		sliderLR.setSliderPercentage(100);
		sliderTB = new GSlider(x, y, width, 0, false);
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
	
	//Callbacks für die Funktion des ScrollPanels
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
	
	//Berechnet das Panel, das angezeigt wird
	private void calcPanel() {
		ArrayList<GUIComponent> added = new ArrayList<GUIComponent>();
		//insgesammte Höhe
		int height = 0;
		//Angezeigte Höhe
		int yHeight = 0;
		
		int skippedHeight = (int) ((getHightAll() - (getHeight() - sliderTB.getHeight())) * sliderLR.getValue());
				
		//Jedes Item wird bearbeitet
		for(GUIComponent item : items) {
						
			item.setY(0);
			item.setX(0);
			
			//es wird geschaut, ob das Element angezeigt wird oder zu weit oben ist
			if(height < skippedHeight) {
				height += item.getHeight();
				continue;
			}
			
			//Es wird geschaut, ob genug Elemente untereinander angezeigt werden
			if((getHeight() - sliderTB.getHeight()) < (yHeight + item.getHeight() - skippedHeight)) {
				//Ist das Panel in vertikaler Richtung voll, wird die Schleife beendet
				break;
			}
			
			//Die Position des Elements wird entsprechende gesetzt und zum Hinzufügen markiert.
			item.setY(item.getY() + (yHeight));
			added.add(item);
			//Die Höhe wird aktulisiert
			height += item.getHeight();
			yHeight += item.getHeight();
		}
		
		//Wie viele Pixel links übersprungen werden.
		int skippedWidth = (int) ((getWidthAll() - (getWidth() - sliderLR.getWidth())) * sliderTB.getValue());
		for(GUIComponent add : added) {
			int width = 0;
			int subWidth = 0;
			//Ist ein Element zu breit wird dieses bearbeitet
			if(add.getWidth() > getWidth() - sliderLR.getWidth()) {
				GUIComponent[] compos = add.getComponents();
				for(int i = 0; i < compos.length; i++) {
					//Es wird für jedes Element des Elements überprüft, ob es in das Panel passt und je nach dem angezeigt oder versteckt.
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
		
		//Alle alten Element werden unsichtbargemacht und entfernt
		panel.setVisible(false);
		panel.removeAll();
		//Die zum Hinzufügen markierten Elemente werden hinzugefügt und die x- und y-Koordinate wird angepasst
		for(GUIComponent c : added) {
			if(sliderLR.getWidth() != 0 && sliderLR.getX() == 0) {
				c.setX(c.getX() + sliderLR.getWidth());
			}
			
			if(sliderTB.getHeight() != 0 && sliderTB.getY() == 0) {
				c.setY(c.getY() + sliderTB.getHeight());
			}
			panel.addComponent(c);
		}
		//das Panel wird angezeigt, falls das GUI sichtbar ist.
		panel.setVisible(isVisible());
		if(getGUI() != null) {
			panel.setVisible(getGUI().isVisible());
		}
		
	}
	
	public ArrayList<GUIComponent> getItems(){
		return items;
	}
	
	//Fügt ein Element hinzu
	public void addItem(GUIComponent item) {
		item.setVisible(false);
		items.add(item);
		item.setGUI(getGUI());
		calcSliderPercentage();		
	}
	
	//Berechnet wie breit der Slider sein soll.
	private void calcSliderPercentage() {
		int width = getWidthAll();
		int height = getHightAll();
		sliderTB.setSliderPercentage((int) (((getWidth() - sliderLR.getWidth()) / (float)width) * 100f));
		sliderLR.setSliderPercentage((int) (((getHeight() - sliderTB.getHeight()) / (float)height) * 100f));
	}
	
	public void removeItem(GUIComponent item) {
		items.remove(item);
	}
	
	//Set den Slider, abhängig von der Position
	public void setSlider(SLIDER_POSITION sliderPos, int thickness) {
				
		if(sliderPos == SLIDER_POSITION.LEFT || sliderPos == SLIDER_POSITION.RIGHT) {
			LR = sliderPos;
			LRT = thickness;
			sliderLR.setWidth(thickness);
			panel.setWidth(getWidth() - thickness);
		}else {
			TB = sliderPos;
			TBT = thickness;
			sliderTB.setHeight(thickness);
			panel.setHeight(getHeight() - thickness);
		}
		
		if(sliderPos == SLIDER_POSITION.LEFT) {
			sliderLR.setX(getX());
			sliderLR.setY(getY());
		}
		
		if(sliderPos == SLIDER_POSITION.RIGHT) {
			sliderLR.setX(getX() + getWidth() - thickness);
			sliderLR.setY(getY());
		}
		
		if(sliderPos == SLIDER_POSITION.TOP) {
			sliderTB.setX(getX());
			sliderTB.setY(getY());
		}
		
		if(sliderPos == SLIDER_POSITION.BOTTOM) {
			sliderTB.setX(getX());
			sliderTB.setY(getY() + getHeight() - thickness);
		}
		
		if(sliderLR.getX() == 0 && sliderLR.getWidth() != 0) {
			sliderTB.setX(getX() + sliderLR.getWidth());
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
		panel.setBounds(getX(), getY(), getWidth(), getHeight());
		calcPanel();
		
		sliderLR.setBounds(getX(), getY(), 0, getHeight());
		sliderTB.setBounds(getX(), getY(), getWidth(), 0);
		
		if(LR != null) {
			setSlider(LR, LRT);
		}
		if(TB != null) {
			setSlider(TB, TBT);
		}
	}

	@Override
	public void dispose() {
		setGUI(null);
		panel.dispose();
		sliderLR.dispose();
		sliderTB.dispose();
	}
	

}
