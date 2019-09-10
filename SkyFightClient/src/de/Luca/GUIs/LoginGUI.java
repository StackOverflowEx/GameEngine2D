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
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.Packet;
import de.Luca.Security.SHAHasing;
import de.Luca.Utils.Calc;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Window.Window;

public class LoginGUI extends GUI {

	public LoginGUI() {
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
		this.addComponent(background);

		GTextBox username = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.4f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		this.addComponent(username);
		
		Description desc = new Description(username, "Benutzername: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(desc);
		
		GTextBox password = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.5f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		password.setPasswordBox(true);
		this.addComponent(password);
		
		Description pwDesc = new Description(password, "Passwort: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(pwDesc);
		
		GButton login = new GButton(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.57f), Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.05f));
		login.setText("Anmelden", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		this.addComponent(login);
		
		GButton register = new GButton(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.63f), Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.05f));
		register.setText("Registrieren", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		this.addComponent(register);
		
		register.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.loginGUI.setVisible(false);
					SkyFightClient.registesrGUI.setVisible(true);
				}
			}
		});
		
		login.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					
					String name = username.getText();
					String pw = password.getText();
					boolean ret = false;
					if(name.isEmpty()) {
						username.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if(pw.isEmpty()) {
						password.setColor(new Vector4f(1, 0, 0, 1));
						ret = true;
					}
					if(ret)
						return;
					
					Packet packet = new Packet();
					packet.packetType = Packet.LOGIN;
					packet.a = name;
					packet.b = SHAHasing.getHash(pw);
					SkyFightClient.handleServerConnection.send(packet);
					SkyFightClient.loadingGUI.setVisible(true);
					SkyFightClient.loginGUI.setVisible(false);
				}
			}
		});
		
		GLabel test = new GLabel(0, 0, 100, 100);
		test.setTexture(Loader.loadTexture("D:\\Icons\\Icon2.png", "gui"));
		this.addComponent(test);

	}

	@Override
	public void visibleUpdaet(boolean visible) {
		if(visible) {
			calc();
		}
	}

}
