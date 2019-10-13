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

public class RegistrierenGUI extends GUI {

	private GLabel background;
	private GLabel rahmen;
	private GLabel text;
	private GLabel benutzername;
	private GTextBox benutzer;
	private GLabel email;
	private GTextBox ema;
	private GLabel passwort;
	private GTextBox pass;
	private GLabel passwortwdh;
	private GTextBox passwdh;
	private GLabel registrieren;
	private GButton reg;
	
	private long visible;

	public RegistrierenGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		visible = 0;
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
		benutzername.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.19f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		benutzer.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.26f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		email.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.33f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		ema.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.40f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		passwort.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.47f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		pass.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.54f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		passwortwdh.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.61f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		passwdh.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.68f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		reg.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.77f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		registrieren.setBounds(Calc.getPixelWidth(0.735f), Calc.getPixelHeight(0.765f), Calc.getPixelWidth(0.1f),
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
		text.setText("Registrieren", SkyFightClient.ConstantiaB40, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER, 0);
		this.addComponent(text);

		benutzername = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.19f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		benutzername.setText("Benutzername", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(benutzername);

		benutzer = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.26f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB32, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		benutzer.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB,
				SkyFightClient.LoginEingabeC);
		benutzer.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		this.addComponent(benutzer);

		email = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.33f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		email.setText("Email Adresse", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(email);

		ema = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.40f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB26, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		ema.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB, SkyFightClient.LoginEingabeC);
		ema.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		this.addComponent(ema);

		passwort = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.47f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		passwort.setText("Passwort", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(passwort);

		pass = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.54f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB32, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		pass.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB,
				SkyFightClient.LoginEingabeC);
		pass.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		pass.setPasswordBox(true);
		this.addComponent(pass);

		passwortwdh = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.61f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		passwortwdh.setText("Passwort wiederholen", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(passwortwdh);

		passwdh = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.68f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB32, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		passwdh.setPasswordBox(true);
		passwdh.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB,
				SkyFightClient.LoginEingabeC);
		passwdh.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		this.addComponent(passwdh);

		reg = new GButton(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.77f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		reg.setButtonTextures(SkyFightClient.LoginRegistrierenA, SkyFightClient.LoginRegistrierenB,
				SkyFightClient.LoginRegistrierenC);
		this.addComponent(reg);

		registrieren = new GLabel(Calc.getPixelWidth(0.735f), Calc.getPixelHeight(0.765f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		registrieren.setText("Registrieren", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(registrieren);

//		back.addClickCallback(new ClickCallback() {
//
//			@Override
//			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
//				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
//					username.setText("");
//					password.setText("");
//					passwordWDH.setText("");
//					email.setText("");
//					SkyFightClient.loginGUI.setVisible(true);
//					SkyFightClient.registesrGUI.setVisible(false);
//				}
//			};
//		});

		reg.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					
					if((System.currentTimeMillis() - visible) < 100) {
						return;
					}

					String name = benutzer.getText();
					String mail = ema.getText();
					if (!name.matches("^[a-zA-Z0-9_]*$")) {
						new PopUp("Dein Benutzername darf nur Buchstaben, Zahlen und Unterstriche besitzen",
								new Vector4f());
						benutzer.setColor(new Vector4f(1, 0, 0, 1));
						return;
					}
					String pw = pass.getText();
					String pwWDH = passwdh.getText();
					if (!pw.equals(pwWDH)) {
						new PopUp("Die beiden Passwörter stimmen nicht über ein.", new Vector4f(1, 0, 0, 1));
						return;
					}
					if (!mail.contains("@") || mail.endsWith("@") || !mail.contains(".")) {
						new PopUp("Du hast eine ungültige E-Mail eingegeben.", new Vector4f(1, 0, 0, 1));
						return;
					}
					boolean ret = false;
					if (name.isEmpty()) {
						benutzer.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if (pw.isEmpty()) {
						pass.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if (pwWDH.isEmpty()) {
						passwdh.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if (mail.isEmpty()) {
						email.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if (ret)
						return;

					Packet packet = new Packet();
					packet.packetType = Packet.REGISTRATION;
					packet.a = name;
					packet.b = SHAHasing.getHash(pw);
					packet.c = mail;
					SkyFightClient.handleServerConnection.send(packet);
				}
			}
		});

	}

	@Override
	public void visibleUpdaet(boolean arg0) {
		visible = System.currentTimeMillis();
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}

}
