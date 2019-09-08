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
import de.Luca.Text.TextManager;
import de.Luca.Utils.Calc;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Window.Window;

public class LoginGUI extends GUI {
	
	private GLabel error;
	private boolean e;

	public LoginGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		e = false;
		calc();
	}

	public void calc() {
		
		for(GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		Vector2f windowSize = Window.getWindowSize();

		GLabel background = new GLabel(0, 0, (int) windowSize.x, (int) windowSize.y);
		background.setTexture(Loader.loadTexture("D:\\Downloads\\login.png", "gui"));
		background.setColor(new Vector4f(1, 1, 1, 1));
		this.addComponent(background);
		
		error = new GLabel(0, 0, (int)windowSize.x, 40);
		error.setColor(new Vector4f(1, 0, 0, 1));
		error.setText("Verbindung zum Server nicht möglich", TextManager.getFont("Impact"), new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		error.setVisible(e && isVisible());
		this.addComponent(error);

		GTextBox username = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.4f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), TextManager.getFont("Impact"), new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		this.addComponent(username);
		
		Description desc = new Description(username, "Benutzername: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(desc);
		
		GTextBox password = new GTextBox(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.5f), Calc.getPixelWidth(0.2f),
				Calc.getPixelHeight(0.05f), TextManager.getFont("Impact"), new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		password.setPasswordBox(true);
		this.addComponent(password);
		
		Description pwDesc = new Description(password, "Passwort: ", "Impact", new Vector4f(0, 0, 0, 1));
		this.addComponent(pwDesc);
		
		GButton login = new GButton(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.57f), Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.05f));
		login.setText("Anmelden", TextManager.getFont("Impact"), new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		this.addComponent(login);
		
		GButton register = new GButton(Calc.getPixelWidth(0.4f), Calc.getPixelHeight(0.63f), Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.05f));
		register.setText("Registrieren", TextManager.getFont("Impact"), new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		this.addComponent(register);
		
		login.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String name = username.getText();
					//name only "^[a-zA-Z0-9_]*$"
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
					
					//MUSST DU NICHT MACHEN, DAMIT SENDE ICH NUR INFORMATIONEN AN DEN SERVER
					Packet packet = new Packet();
					packet.packetType = Packet.LOGIN;
					packet.a = name;
					packet.b = SHAHasing.getHash(pw);
					SkyFightClient.handleServerConnection.send(packet);
				}
			}
		});

	}
	
	public void showNotConnected() {
		if(error != null && isVisible()) {
			error.setVisible(true);
		}
		e = true;
	}
	
	public void hideNotConnected() {
		if(error != null) {
			error.setVisible(false);
		}
		e = false;
	}

	@Override
	public void visibleUpdaet(boolean visible) {
		error.setVisible(e && visible);
	}

}
