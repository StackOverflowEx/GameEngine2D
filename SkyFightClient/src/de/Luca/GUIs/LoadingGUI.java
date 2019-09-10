package de.Luca.GUIs;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.GUI.GLabel;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Window.Window;

public class LoadingGUI extends GUI{
	

	public LoadingGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		calc();
	}

	public void calc() {
		for(GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		Vector2f windowSize = Window.getWindowSize();

		GLabel background = new GLabel(0, 0, (int) windowSize.x, (int) windowSize.y);
		background.setTexture(SkyFightClient.backgroundLOGIN);
		background.setColor(new Vector4f(1, 1, 1, 1));
		background.setText("Loading...", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		this.addComponent(background);
				
	}
	
	@Override
	public void visibleUpdaet(boolean visible) {
		if(visible) {
			calc();
		}
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}
	
	

}
