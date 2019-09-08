package de.Luca.GUIs;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Vector4f;

import de.Luca.GUI.GLabel;
import de.Luca.GUI.GUI;
import de.Luca.Text.TextManager;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Window.Window;

public class PopUp extends GUI{
	
	private static CopyOnWriteArrayList<PopUp> popups = new CopyOnWriteArrayList<PopUp>();

	private long gen;
	
	public PopUp(String message, Vector4f color) {
		super(0, 0, (int)Window.getWindowSize().x, 40);
		popups.add(this);
		gen = System.currentTimeMillis();
		GLabel label = new GLabel(0, 0, (int)Window.getWindowSize().x, 40);
		label.setColor(color);
		label.setText(message, TextManager.getFont("Impact"), new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		this.addComponent(label);
		setY(popups.size() * (int)(Window.getWindowSize().y/25f));
		setVisible(true);
	}
	
	public PopUp(String message, Vector4f color, boolean perma) {
		super(0, 0, (int)Window.getWindowSize().x, 40);
		popups.add(this);
		gen = System.currentTimeMillis();
		if(perma){
			gen += System.currentTimeMillis();
		}
		GLabel label = new GLabel(0, 0, (int)Window.getWindowSize().x, 40);
		label.setColor(color);
		label.setText(message, TextManager.getFont("Impact"), new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		this.addComponent(label);
		setY(popups.size() * (int)(Window.getWindowSize().y/25f));
		setVisible(true);
	}
	
	public long getGen() {
		return gen;
	}
	
	public static void update() {
		for(PopUp p : popups) {
			if((System.currentTimeMillis() - p.getGen()) > 5000) {
				p.dispose();
				popups.remove(p);
			}
		}
		int i = 0;
		for(PopUp p : popups) {
			p.setY(i * (int)(Window.getWindowSize().y/25f));
			i++;
		}
	}

	@Override
	public void visibleUpdaet(boolean visible) {}

}
