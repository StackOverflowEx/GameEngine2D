package de.Luca.GUIs;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GButton;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.GameLogic.GameState;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.Packet;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.Window.Window;

public class MainMenuGUI extends GUI {

	private GLabel background;
	private GButton spiel;
	private GLabel spielSuchen;
	private GButton world;
	private GLabel worldEditor;
	private GButton einst;
	private GLabel einstellungen;

	public MainMenuGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		init();
	}

	public void calc() {
		setX(0);
		setY(0);
		setWidth((int) Window.getWindowSize().x);
		setHeight((int) Window.getWindowSize().y);	
		
		
		background.setBounds(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		spiel.setBounds(Calc.getPixelWidth(0.6f), Calc.getPixelHeight(0.23f), Calc.getPixelWidth(0.34f),
				Calc.getPixelHeight(0.11f));
		spielSuchen.setBounds(Calc.getPixelWidth(0.725f), Calc.getPixelHeight(0.24f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		world.setBounds(Calc.getPixelWidth(0.6f), Calc.getPixelHeight(0.39f), Calc.getPixelWidth(0.34f),
				Calc.getPixelHeight(0.11f));
		worldEditor.setBounds(Calc.getPixelWidth(0.725f), Calc.getPixelHeight(0.4f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		einst.setBounds(Calc.getPixelWidth(0.6f), Calc.getPixelHeight(0.55f), Calc.getPixelWidth(0.34f),
				Calc.getPixelHeight(0.11f));
		einstellungen.setBounds(Calc.getPixelWidth(0.725f), Calc.getPixelHeight(0.56f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));

	}

	private void init() {
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		
		background = new GLabel(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		background.setTexture(SkyFightClient.MainBackground);
		this.addComponent(background);
		
		spiel = new GButton(Calc.getPixelWidth(0.6f), Calc.getPixelHeight(0.23f), Calc.getPixelWidth(0.34f),
				Calc.getPixelHeight(0.11f));
		spiel.setButtonTextures(SkyFightClient.MainButtonA, SkyFightClient.MainButtonB,
				SkyFightClient.MainButtonC);
		this.addComponent(spiel);
		
		spielSuchen = new GLabel(Calc.getPixelWidth(0.725f), Calc.getPixelHeight(0.24f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		spielSuchen.setText("Spiel Suchen", SkyFightClient.ConstantiaB56, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(spielSuchen);
		
		world = new GButton(Calc.getPixelWidth(0.6f), Calc.getPixelHeight(0.39f), Calc.getPixelWidth(0.34f),
				Calc.getPixelHeight(0.11f));
		world.setButtonTextures(SkyFightClient.MainButtonA, SkyFightClient.MainButtonB,
				SkyFightClient.MainButtonC);
		this.addComponent(world);
		
		worldEditor = new GLabel(Calc.getPixelWidth(0.725f), Calc.getPixelHeight(0.4f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		worldEditor.setText("World Editor", SkyFightClient.ConstantiaB56, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(worldEditor);
		
		einst = new GButton(Calc.getPixelWidth(0.6f), Calc.getPixelHeight(0.55f), Calc.getPixelWidth(0.34f),
				Calc.getPixelHeight(0.11f));
		einst.setButtonTextures(SkyFightClient.MainButtonA, SkyFightClient.MainButtonB,
				SkyFightClient.MainButtonC);
		this.addComponent(einst);
		
		einstellungen = new GLabel(Calc.getPixelWidth(0.725f), Calc.getPixelHeight(0.56f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		einstellungen.setText("Spiel beenden", SkyFightClient.ConstantiaB56, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(einstellungen);
		
		
		spiel.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					isSearching = !isSearching;
					Packet send = new Packet();
					send.packetType = Packet.SEARCHING;
					SkyFightClient.handleServerConnection.send(send);
				}
			}
		});
		
		world.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldSelctGUI.setVisible(true);
					SkyFightClient.mainGUI.setVisible(false);
					if(isSearching) {
						Packet send = new Packet();
						send.packetType = Packet.SEARCHING;
						SkyFightClient.handleServerConnection.send(send);
						setIsSearching(false, false);
					}
				}
			}
		});
		
		einstellungen.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					System.exit(0);
				}
			}
		});
		
		


	}

	private boolean isSearching = false;;
	private PopUp info;

	public void setIsSearching(boolean b, boolean msg) {
		isSearching = b;
		if (b) {
			spielSuchen.setText("Suche abbrechen", SkyFightClient.ConstantiaB56, new Vector4f(1f, 1f, 1f, 1f),
					TEXT_ALIGN.CENTER, 0);
			SkyFightClient.gameState = GameState.SEARCHING;
			if (msg) {
				info = new PopUp("Es wird nach einem Gegner gesucht...", new Vector4f(0.26f, 0.96f, 0.63f, 1f), true);
			}
		} else {
			spielSuchen.setText("Spiel Suchen", SkyFightClient.ConstantiaB56, new Vector4f(1f, 1f, 1f, 1f),
					TEXT_ALIGN.CENTER, 0);
			if (info != null) {
				info.destroy();
			}
			info = null;
			if (msg) {
				new PopUp("Die Gegnersuche wurde abgebrochen.", new Vector4f(0.5f, 0.96f, 0.63f, 1f));
			}
			SkyFightClient.gameState = GameState.MENUE;
		}
	}

	@Override
	public void visibleUpdaet(boolean arg0) {
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}
}
