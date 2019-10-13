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
import de.Luca.World.WorldEditor;

public class WorldEditorOverlay extends GUI {

	private GLabel LinkerRahmen;
	private GLabel ObererRahmen;
	private GButton Links;
	private GButton Mitte;
	private GButton Rechts;
	private GLabel RechterRahmen;
	private GLabel UntererRahmen;
	private GButton Back;
	private GButton Save;

	private GLabel bloecke;
	private GLabel bloecke2;
	private GLabel bloecke3;
	private GLabel save2;
	
	public WorldEditorOverlay() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		init();
	}

	@Override
	public void visibleUpdaet(boolean arg0) {
		calc();
	}

	public void calc() {
		setX(0);
		setY(0);
		setWidth((int) Window.getWindowSize().x);
		setHeight((int) Window.getWindowSize().y);	
		
		
		LinkerRahmen.setBounds(Calc.getPixelWidth(0.23125f), Calc.getPixelHeight(0.9084f),
				Calc.getPixelWidth(0.0296875f), Calc.getPixelHeight(0.0917f));
		ObererRahmen.setBounds(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.9084f),
				Calc.getPixelWidth(0.478125f), Calc.getPixelHeight(0.017f));
		Links.setBounds(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.9254f),
				Calc.getPixelWidth(0.1609375f), Calc.getPixelHeight(0.0639f));
		Mitte.setBounds(Calc.getPixelWidth(0.421875f), Calc.getPixelHeight(0.9254f), Calc.getPixelWidth(0.1609375f),
				Calc.getPixelHeight(0.0639f));
		Rechts.setBounds(Calc.getPixelWidth(0.5828125f), Calc.getPixelHeight(0.9254f), Calc.getPixelWidth(0.15625f),
				Calc.getPixelHeight(0.0639f));
		RechterRahmen.setBounds(Calc.getPixelWidth(0.7390625f), Calc.getPixelHeight(0.9084f),
				Calc.getPixelWidth(0.0296875f), Calc.getPixelHeight(0.0917f));
		UntererRahmen.setBounds(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.9893f),
				Calc.getPixelWidth(0.478125f), Calc.getPixelHeight(0.012f));
		Back.setBounds(0, 0, Calc.getPixelWidth(0.056f), Calc.getPixelHeight(0.047f));
		Save.setBounds(Calc.getPixelWidth(0.883f), Calc.getPixelHeight(0.928f), Calc.getPixelWidth(0.117f),
				Calc.getPixelHeight(0.072f));
		
		bloecke.setBounds(Calc.getPixelWidth(0.29f), Calc.getPixelHeight(0.915f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		bloecke2.setBounds(Calc.getPixelWidth(0.45f), Calc.getPixelHeight(0.915f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		bloecke3.setBounds(Calc.getPixelWidth(0.61f), Calc.getPixelHeight(0.915f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		save2.setBounds(Calc.getPixelWidth(0.89f), Calc.getPixelHeight(0.925f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		
		SkyFightClient.worldEditorSettings.setVisible(!SkyFightClient.worldEditorSettings.isVisible());
		SkyFightClient.worldEditorSettings.setVisible(!SkyFightClient.worldEditorSettings.isVisible());
		SkyFightClient.worldEditorErstellen.setVisible(!SkyFightClient.worldEditorErstellen.isVisible());
		SkyFightClient.worldEditorErstellen.setVisible(!SkyFightClient.worldEditorErstellen.isVisible());

	}

	private void init() {
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}
		
		bloecke = new GLabel(Calc.getPixelWidth(0.29f), Calc.getPixelHeight(0.915f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		bloecke.setText("Blockauswahl", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(bloecke);
		
		bloecke2 = new GLabel(Calc.getPixelWidth(0.45f), Calc.getPixelHeight(0.915f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		bloecke2.setText("Welteinstel-" + "\n" + "lungen", SkyFightClient.ConstantiaB26, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(bloecke2);
		
		bloecke3 = new GLabel(Calc.getPixelWidth(0.61f), Calc.getPixelHeight(0.915f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		bloecke3.setText("Spiegeln", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(bloecke3);
		
		save2 = new GLabel(Calc.getPixelWidth(0.89f), Calc.getPixelHeight(0.925f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		save2.setText("Save", SkyFightClient.ConstantiaB38, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(save2);

		LinkerRahmen = new GLabel(Calc.getPixelWidth(0.23125f), Calc.getPixelHeight(0.9084f),
				Calc.getPixelWidth(0.0296875f), Calc.getPixelHeight(0.0917f));
		LinkerRahmen.setTexture(SkyFightClient.weOverlayRahmenLinks);
		this.addComponent(LinkerRahmen);

		ObererRahmen = new GLabel(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.9084f),
				Calc.getPixelWidth(0.478125f), Calc.getPixelHeight(0.017f));
		ObererRahmen.setTexture(SkyFightClient.weOverlayRahmenOben);
		this.addComponent(ObererRahmen);

		Links = new GButton(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.9254f),
				Calc.getPixelWidth(0.1609375f), Calc.getPixelHeight(0.0639f));
		Links.setButtonTextures(SkyFightClient.weOverlayLinksA, SkyFightClient.weOverlayLinksB,
				SkyFightClient.weOverlayLinksC);
		this.addComponent(Links);

		Mitte = new GButton(Calc.getPixelWidth(0.421875f), Calc.getPixelHeight(0.9254f), Calc.getPixelWidth(0.1609375f),
				Calc.getPixelHeight(0.0639f));
		Mitte.setButtonTextures(SkyFightClient.weOverlayMitteA, SkyFightClient.weOverlayMitteB,
				SkyFightClient.weOverlayMitteC);
		this.addComponent(Mitte);

		Rechts = new GButton(Calc.getPixelWidth(0.5828125f), Calc.getPixelHeight(0.9254f), Calc.getPixelWidth(0.15625f),
				Calc.getPixelHeight(0.0639f));
		Rechts.setButtonTextures(SkyFightClient.weOverlayRechtsA, SkyFightClient.weOverlayRechtsB,
				SkyFightClient.weOverlayRechtsC);
		this.addComponent(Rechts);

		RechterRahmen = new GLabel(Calc.getPixelWidth(0.7390625f), Calc.getPixelHeight(0.9084f),
				Calc.getPixelWidth(0.0296875f), Calc.getPixelHeight(0.0917f));
		RechterRahmen.setTexture(SkyFightClient.weOverlayRahmenRechts);
		this.addComponent(RechterRahmen);

		UntererRahmen = new GLabel(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.9893f),
				Calc.getPixelWidth(0.478125f), Calc.getPixelHeight(0.012f));
		UntererRahmen.setTexture(SkyFightClient.weOverlayRahmenUnten);
		this.addComponent(UntererRahmen);

		Back = new GButton(0, 0, Calc.getPixelWidth(0.056f), Calc.getPixelHeight(0.047f));
		Back.setButtonTextures(SkyFightClient.weOverlayBackA, SkyFightClient.weOverlayBackB,
				SkyFightClient.weOverlayBackC);
		this.addComponent(Back);

		Save = new GButton(Calc.getPixelWidth(0.883f), Calc.getPixelHeight(0.928f), Calc.getPixelWidth(0.117f),
				Calc.getPixelHeight(0.072f));
		Save.setButtonTextures(SkyFightClient.weOverlaySaveA, SkyFightClient.weOverlaySaveA,
				SkyFightClient.weOverlaySaveC);
		this.addComponent(Save);

		Back.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					WorldEditor.stop();
				}
			}
		});

		Save.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String name = SkyFightClient.worldEditorSettings.getName();
					if(name.isEmpty()) {
						new PopUp("Bitte gebe deiner Map einen Namen.", new Vector4f(1, 0, 0, 1));
						return;
					}
					if(WorldEditor.getSpawn1() == null) {
						new PopUp("Bitte setze den Spawn für Spieler 1", new Vector4f(1, 0, 0, 1));
						return;
					}
					if(WorldEditor.getSpawn2() == null) {
						new PopUp("Bitte setze den Spawn für Spieler 2", new Vector4f(1, 0, 0, 1));
						return;
					}
					WorldEditor.save(name);
				}
			}
		});

		Rechts.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					WorldEditor.setMirroring(!WorldEditor.isMirroring());
					if (WorldEditor.isMirroring()) {
						new PopUp(
								"ACHTUNG: Bei einem Klick in der Welt werden alle Blöcke an der y-Achse gespiegelt.\nKlicke erneut auf den Knopf um den Spiegelmodus zu verlassen.",
								new Vector4f(1, 0.7f, 0, 1));
					}
				}
			}
		});

		Mitte.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldEditorSettings.setVisible(true);
					SkyFightClient.worldEditorAuswahl.setVisible(false);
					SkyFightClient.worldEditorErstellen.setVisible(false);

				}
			}
		});

		Links.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldEditorAuswahl.setVisible(true);
					SkyFightClient.worldEditorSettings.setVisible(false);
				}
			}
		});
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}

}
