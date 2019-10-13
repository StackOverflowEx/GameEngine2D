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
import de.Luca.Networking.HandelServerPacketHandler;
import de.Luca.Packets.Packet;
import de.Luca.Security.SHAHasing;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.Window.Window;

public class LoginGUI extends GUI {

	private GLabel background;
	private GLabel rahmen;
	private GLabel text;
	private GLabel benutzername;
	private GTextBox benutzer;
	private GLabel passwort;
	private GTextBox pass;
	private GLabel login;
	private GButton log;
	private GLabel vergessen;
	private GButton verg;
	private GLabel registrieren;
	private GButton reg;

	public LoginGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		init();
	}

	public void calc() {
<<<<<<< HEAD
		setX(0);
		setY(0);
		setWidth((int) Window.getWindowSize().x);
		setHeight((int) Window.getWindowSize().y);	
		
		
=======
				
>>>>>>> branch 'master' of https://github.com/StackOverflowEx/GameEngine2D.git
		background.setBounds(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		rahmen.setBounds(Calc.getPixelWidth(0.63f), Calc.getPixelHeight(0.06f), Calc.getPixelWidth(0.31f),
				Calc.getPixelHeight(0.84f));
		text.setBounds(Calc.getPixelWidth(0.63f), Calc.getPixelHeight(0.09f), Calc.getPixelWidth(0.31f),
				Calc.getPixelHeight(0.1f));
		benutzername.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.19f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		benutzer.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.26f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		passwort.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.36f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		pass.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.43f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		log.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.54f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.11f));
		login.setBounds(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.555f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		verg.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.67f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		vergessen.setBounds(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.665f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		reg.setBounds(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.77f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		registrieren.setBounds(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.765f), Calc.getPixelWidth(0.1f),
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
		text.setText("Anmelden", SkyFightClient.ConstantiaB80, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER, 0);

		this.addComponent(text);

		benutzername = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.19f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		benutzername.setText("Benutzername", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
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

		passwort = new GLabel(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.36f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		passwort.setText("Passwort", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
				TEXT_ALIGN.LEFT, 0);
		this.addComponent(passwort);

		pass = new GTextBox(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.43f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f), SkyFightClient.ConstantiaB32, new Vector4f(0.2f, 0.18f, 0.17f, 1f),
				TEXT_ALIGN.LEFT, 20);
		pass.setButtonTextures(SkyFightClient.LoginEingabeA, SkyFightClient.LoginEingabeB,
				SkyFightClient.LoginEingabeC);
		pass.setSelectedTextures(SkyFightClient.LoginEingabeC, SkyFightClient.LoginEingabeC,
				SkyFightClient.LoginEingabeC);
		pass.setPasswordBox(true);
		this.addComponent(pass);

		log = new GButton(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.54f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.11f));
		log.setButtonTextures(SkyFightClient.LoginLoginA, SkyFightClient.LoginLoginB, SkyFightClient.LoginLoginC);
		this.addComponent(log);

		login = new GLabel(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.555f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		login.setText("LOGIN", SkyFightClient.ConstantiaB80, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER, 0);
		this.addComponent(login);

		verg = new GButton(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.67f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		verg.setButtonTextures(SkyFightClient.LoginVergessenA, SkyFightClient.LoginVergessenB,
				SkyFightClient.LoginVergessenC);
		this.addComponent(verg);

		vergessen = new GLabel(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.665f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		vergessen.setText("Passwort vergessen", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(vergessen);

		reg = new GButton(Calc.getPixelWidth(0.65f), Calc.getPixelHeight(0.77f), Calc.getPixelWidth(0.27f),
				Calc.getPixelHeight(0.08f));
		reg.setButtonTextures(SkyFightClient.LoginRegistrierenA, SkyFightClient.LoginRegistrierenB,
				SkyFightClient.LoginRegistrierenC);
		this.addComponent(reg);

		registrieren = new GLabel(Calc.getPixelWidth(0.73f), Calc.getPixelHeight(0.765f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		registrieren.setText("Registrieren", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(registrieren);

		reg.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.loginGUI.setVisible(false);
					SkyFightClient.registerGUI.setVisible(true);
				}
			}
		});
		
		verg.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					
					String eMail = benutzer.getText();
					if(eMail.isEmpty()) {
						new PopUp("Bitte gebe in das Feld \"Benutzername\" deine E-Mail Addresse ein.", new Vector4f(1, 0, 0, 1));
						return;
					}
					if(!eMail.contains("@")) {
						new PopUp("Bitte gebe in das Feld \"Benutzername\" deine E-Mail Addresse ein.", new Vector4f(1, 0, 0, 1));
						return;
					}
					
					Packet p = new Packet();
					p.packetType = Packet.PASSWORD_RESET;
					p.a = eMail;
					SkyFightClient.handleServerConnection.send(p);
										
//					SkyFightClient.loginGUI.setVisible(false);
//					SkyFightClient.forgotPWGUI.setVisible(true);
				}
			}
		});

		log.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					
					String name = benutzer.getText();
					boolean ret = false;
					
					String pw = pass.getText();
					if (name.isEmpty()) {
						benutzer.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if (pw.isEmpty()) {
						pass.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if (ret)
						return;

					Packet packet = new Packet();
					packet.packetType = Packet.LOGIN;
					packet.a = name;
					packet.b = SHAHasing.getHash(pw);
					HandelServerPacketHandler.username = name;
					SkyFightClient.handleServerConnection.send(packet);
				}
			}
		});

	}
	
	public void transitionToMain() {
		SkyFightClient.mainGUI.setX(Calc.getPixelWidth(-1));
		SkyFightClient.mainGUI.setVisible(true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i = 1; i <= 500; i++) {
					SkyFightClient.mainGUI.setX(Calc.getPixelWidth(-1f + (i / 500f)));
					SkyFightClient.loginGUI.setX(Calc.getPixelWidth((i / 500f)));
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				SkyFightClient.loginGUI.setVisible(false);
			}
		}).start();
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
