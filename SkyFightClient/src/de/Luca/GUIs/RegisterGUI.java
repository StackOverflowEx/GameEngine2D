package de.Luca.GUIs;

import org.joml.Vector2f;
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
import de.Luca.Utils.Calc;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Window.Window;

public class RegisterGUI extends GUI {
	
	public RegisterGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		calc();
	}

	public void calc() {
		
		for(GUIComponent c : getComponents()) {
			c.dispose();
		}

		Vector2f windowSize = Window.getWindowSize();

		GLabel background = new GLabel(0, 0, (int) windowSize.x, (int) windowSize.y);
		background.setTexture(SkyFightClient.backgroundLOGIN);
		background.setColor(new Vector4f(1, 1, 1, 1));
		this.addComponent(background);

		GTextBox username = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.25f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		this.addComponent(username);
		
		Description desc = new Description(username, "Benutzername: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(desc);
		
		GTextBox email = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.34f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		this.addComponent(email);
		
		Description edesc = new Description(email, "E-Mail: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(edesc);
		
		GTextBox password = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.43f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		password.setPasswordBox(true);
		this.addComponent(password);
		
		GTextBox passwordWDH = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.52f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		passwordWDH.setPasswordBox(true);
		this.addComponent(passwordWDH);
		
		Description pwDesc = new Description(password, "Passwort: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(pwDesc);
		
		Description pwWDHDesc = new Description(passwordWDH, "Passwort wiederholen: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(pwWDHDesc);
		
		GButton register = new GButton(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.59f), Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.05f));
		register.setText("Registrieren", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		this.addComponent(register);
		
		GButton back = new GButton(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.66f), Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.05f));
		back.setText("Zurück", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		this.addComponent(back);
		
		back.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					username.setText("");
					password.setText("");
					passwordWDH.setText("");
					email.setText("");
					SkyFightClient.loginGUI.setVisible(true);
					SkyFightClient.registesrGUI.setVisible(false);
				}
			};
		});
		
		register.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					
					String name = username.getText();
					String mail = email.getText();
					if(!name.matches("^[a-zA-Z0-9_]*$")) {
						new PopUp("Dein Benutzername darf nur Buchstaben, Zahlen und Unterstriche besitzen", new Vector4f());
						username.setColor(new Vector4f(1, 0, 0, 1));
						return;
					}
					String pw = password.getText();
					String pwWDH = passwordWDH.getText();
					if(!pw.equals(pwWDH)) {
						new PopUp("Die beiden Passwörter stimmen nicht über ein.", new Vector4f(1, 0, 0, 1));
						return;
					}
					if(!mail.contains("@") || mail.endsWith("@") || !mail.contains(".")) {
						new PopUp("Du hast eine ungültige E-Mail eingegeben.", new Vector4f(1, 0, 0, 1));
						return;
					}
					boolean ret = false;
					if(name.isEmpty()) {
						username.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if(pw.isEmpty()) {
						password.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if(pwWDH.isEmpty()) {
						passwordWDH.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if(mail.isEmpty()) {
						email.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if(ret)
						return;
					
					Packet packet = new Packet();
					packet.packetType = Packet.REGISTRATION;
					packet.a = name;
					packet.b = SHAHasing.getHash(pw);
					packet.c = mail;
					SkyFightClient.handleServerConnection.send(packet);
					SkyFightClient.loadingGUI.setVisible(true);
					SkyFightClient.registesrGUI.setVisible(false);
				}
			}
		});

	}
	
	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}

	@Override
	public void visibleUpdaet(boolean visible) {
		if(visible) {
			calc();
		}
	}

}
