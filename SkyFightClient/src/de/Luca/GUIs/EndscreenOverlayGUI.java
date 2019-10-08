package de.Luca.GUIs;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GButton;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.Window.Window;

public class EndscreenOverlayGUI extends GUI {
	
	private GLabel gewonnen;
	private GLabel verloren;
	private GLabel abbruch;
	private GButton weitermachen;
	private GLabel weitermachenText;

	public EndscreenOverlayGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		init();
	}

	public void calc() {
		gewonnen.setBounds(Calc.getPixelWidth(0.0625f), Calc.getPixelHeight(0.334f), Calc.getPixelWidth(0.875f), Calc.getPixelHeight(0.26f));
		verloren.setBounds(Calc.getPixelWidth(0.0625f), Calc.getPixelHeight(0.334f), Calc.getPixelWidth(0.875f), Calc.getPixelHeight(0.26f));
		abbruch.setBounds(Calc.getPixelWidth(0.0625f), Calc.getPixelHeight(0.334f), Calc.getPixelWidth(0.875f), Calc.getPixelHeight(0.26f));
		weitermachen.setBounds(Calc.getPixelWidth(0.45f), Calc.getPixelHeight(0.669f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
	}

	private void init() {
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		
		gewonnen = new GLabel(Calc.getPixelWidth(0.0625f), Calc.getPixelHeight(0.334f), Calc.getPixelWidth(0.875f), Calc.getPixelHeight(0.26f));
		gewonnen.setTexture(SkyFightClient.EndscreenOverlayGewonnenRahmen);
		this.addComponent(gewonnen);
		
		verloren = new GLabel(Calc.getPixelWidth(0.0625f), Calc.getPixelHeight(0.334f), Calc.getPixelWidth(0.875f), Calc.getPixelHeight(0.26f));
		verloren.setTexture(SkyFightClient.EndscreenOverlayVerlorenRahmen);
		this.addComponent(verloren);
		
		abbruch = new GLabel(Calc.getPixelWidth(0.0625f), Calc.getPixelHeight(0.334f), Calc.getPixelWidth(0.875f), Calc.getPixelHeight(0.26f));
		abbruch.setTexture(SkyFightClient.EndscreenOverlayAbbruchRahmen);
		this.addComponent(abbruch);
		
		weitermachen = new GButton(Calc.getPixelWidth(0.344f), Calc.getPixelHeight(0.666f), Calc.getPixelWidth(0.3125f),
				Calc.getPixelHeight(0.089f));
		weitermachen.setButtonTextures(SkyFightClient.EndscreenOverlayWeitermachenA, SkyFightClient.EndscreenOverlayWeitermachenB,
				SkyFightClient.EndscreenOverlayWeitermachenC);
		this.addComponent(weitermachen);
		
		weitermachenText = new GLabel(Calc.getPixelWidth(0.45f), Calc.getPixelHeight(0.669f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		weitermachenText.setText("Weiter", SkyFightClient.ConstantiaB40, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(weitermachenText); 
		
		weitermachen.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.mainGUI.setVisible(true);
					SkyFightClient.ingameOverlay.setVisible(false);
					setVisible(false);
				}
			}
		});
	}

	public void showWin() {
		verloren.setVisible(false);
		abbruch.setVisible(false);
		gewonnen.setVisible(true);
	}
	
	public void showAbort() {
		verloren.setVisible(false);
		abbruch.setVisible(true);
		gewonnen.setVisible(false);
	}
	
	public void showLoos() {
		verloren.setVisible(true);
		abbruch.setVisible(false);
		gewonnen.setVisible(false);
	}
	
	@Override
	public void visibleUpdaet(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}
}
