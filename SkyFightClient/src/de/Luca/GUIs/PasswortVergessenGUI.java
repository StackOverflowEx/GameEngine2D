package de.Luca.GUIs;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GButton;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GTextBox;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.Packet;
import de.Luca.Security.SHAHasing;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.Window.Window;

public class PasswortVergessenGUI extends GUI {

	private GLabel background;
	private GLabel rahmen;
	private GLabel text;
	private GLabel token;
	private GTextBox tok;
	private GLabel passwort;
	private GTextBox pass;
	private GLabel passwortwdh;
	private GTextBox passwdh;
	private GLabel abbrechen;
	private GButton abbr;
	private GLabel zuruecksetzen;
	private GButton zurueck;

	public PasswortVergessenGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		init();
	}

	public void calc() {
		setX(0);
		setY(0);
		setWidth((int) Window.getWindowSize().x);
		setHeight((int) Window.getWindowSize().y);	
		
		background.setBounds(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		rahmen.setBounds(Calc.getPixelWidth(0.63f), Calc.getPixelHeight(0.06f), Calc.getPixelWidth(0.31f),
				Calc.getPixelHeight(0.84f));
		text.setBounds(Calc.getPixelWidth(0.63f), Calc.getPixelHeight(0.09f), Calc.getPixelWidth(0.31f),
				Calc.getPixelHeight(0.1f));
		token.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.19f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		tok.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.26f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		passwort.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.35f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		pass.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.42f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		passwortwdh.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.51f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		passwdh.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.58f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		abbr.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.67f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		abbrechen.setBounds(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.665f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		zurueck.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.77f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		zuruecksetzen.setBounds(Calc.getPixelWidth(0.735f), Calc.getPixelHeight(0.765f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
	}

	private void init() {
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		background = new GLabel(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		background.setTexture(SkyFightClient.LoginBackground);
		this.addComponent(background);

		rahmen = new GLabel(Calc.getPixelWidth(0.63f), Calc.getPixelHeight(0.06f), Calc.getPixelWidth(0.31f),
				Calc.getPixelHeight(0.84f));
		rahmen.setTexture(SkyFightClient.LoginRahmen);
		this.addComponent(rahmen); 

		text = new GLabel(Calc.getPixelWidth(0.63f), Calc.getPixelHeight(0.09f), Calc.getPixelWidth(0.31f),
				Calc.getPixelHeight(0.1f));
		text.setText("Passwort vergessen", SkyFightClient.ConstantiaB40, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER, 0);
		this.addComponent(text);

		token = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.19f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		token.setText("Token", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(token);

		tok = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.26f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB32, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		tok.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB,
				SkyFightClient.LoginEingabeC);
		tok.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		this.addComponent(tok);

		passwort = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.35f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		passwort.setText("Passwort", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(passwort);

		pass = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.42f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB32, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		pass.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB,
				SkyFightClient.LoginEingabeC);
		pass.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		pass.setPasswordBox(true);
		this.addComponent(pass);

		passwortwdh = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.51f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		passwortwdh.setText("Passwort wiederholen", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(passwortwdh);

		passwdh = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.58f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB32, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		passwdh.setPasswordBox(true);
		passwdh.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB,
				SkyFightClient.LoginEingabeC);
		passwdh.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		this.addComponent(passwdh);
		
		abbr = new GButton(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.67f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		abbr.setButtonTextures(SkyFightClient.LoginVergessenA, SkyFightClient.LoginVergessenB,
				SkyFightClient.LoginVergessenC);
		this.addComponent(abbr);
		
		abbrechen = new GLabel(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.665f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		abbrechen.setText("Abbrechen", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(abbrechen);
		
		zurueck = new GButton(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.77f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		zurueck.setButtonTextures(SkyFightClient.LoginRegistrierenA, SkyFightClient.LoginRegistrierenB,
				SkyFightClient.LoginRegistrierenC);
		this.addComponent(zurueck);
		
		zuruecksetzen = new GLabel(Calc.getPixelWidth(0.735f), Calc.getPixelHeight(0.765f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		zuruecksetzen.setText("Passwort zurücksetzen", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(zuruecksetzen);
		
		abbr.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					pass.setText("");
					passwdh.setText("");
					tok.setText("");
					setVisible(false);
					SkyFightClient.loginGUI.setVisible(true);
				}
			}
		});
		
		zurueck.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					
					String p = pass.getText();
					String pw = passwdh.getText();
					if(!p.equals(pw)) {
						new PopUp("Die Passwörter stimmen nicht überein.", new Vector4f(1, 0, 0, 1));
						return;
					}
					
					Packet packet = new Packet();
					packet.packetType = Packet.PASSWORD_RESET;
					packet.a = tok.getText();
					packet.b = SHAHasing.getHash(pass.getText());
					SkyFightClient.handleServerConnection.send(packet);
				}
			}
		});

		
	}

	@Override
	public void visibleUpdaet(boolean arg0) {
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}

}
