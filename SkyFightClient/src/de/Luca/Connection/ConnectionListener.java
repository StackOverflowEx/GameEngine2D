package de.Luca.Connection;

import org.joml.Vector4f;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.GUIs.PopUp;
import de.Luca.GameLogic.GameState;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.Packet;

public class ConnectionListener implements Listener{
	
	private PopUp notConnected;
	
	@EventHandler
	public void onPacket(ConnectionRecieveEvent e) {
		if(e.getConnection() == SkyFightClient.handleServerConnection) {
			Packet packet = e.getPacket();
			if(packet.packetType == Packet.PING) {
				long sent = (long) packet.a;
				int ms = (int) (System.currentTimeMillis() - sent);
				System.out.println("Ping: " + ms);
			}else if(packet.packetType == Packet.SUCCESS) {
				if((int)packet.a == Packet.LOGIN) {
					SkyFightClient.loadingGUI.setVisible(false);
					//Show main
				}else if((int) packet.a == Packet.REGISTRATION) {
					SkyFightClient.loadingGUI.setVisible(false);
					SkyFightClient.loginGUI.setVisible(true);
					new PopUp("Du hast dich erfolgreich registriert.", new Vector4f(0, 1, 0, 1));
				}
			}else if(packet.packetType == Packet.ERROR) {
				int errorCode = (int) packet.a;
				if(errorCode == Packet.ERROR_MISSING_HANDSHAKE) {
					new PopUp("Es ist ein kritischer Fehler bei der Kommunikation mit dem Server aufgetreten.", new Vector4f(1, 0, 0, 1));
				}else if(errorCode == Packet.ERROR_SERVER) {
					new PopUp("Eine Aktion konnte nicht ausgeführt werden. (Serverfehler)", new Vector4f(1, 0, 0, 1));
				}else if(errorCode == Packet.ERROR_USERNAME_EXISTS) {
					SkyFightClient.loadingGUI.setVisible(false);
					SkyFightClient.registesrGUI.setVisible(true);
					new PopUp("Dieser Benutzername wird bereits verwendet.", new Vector4f(1, 0, 0, 1));
				}else if(errorCode == Packet.ERROR_EMAIL_EXISTS) {
					SkyFightClient.loadingGUI.setVisible(false);
					SkyFightClient.registesrGUI.setVisible(true);
					new PopUp("Diese E-Mail wird bereits für einen anderen Account verwendet.", new Vector4f(1, 0, 0, 1));
				}else if(errorCode == Packet.ERROR_EMAIL_EXISTS) {
					SkyFightClient.loadingGUI.setVisible(false);
					SkyFightClient.registesrGUI.setVisible(true);
					new PopUp("Diese E-Mail wird bereits für einen anderen Account verwendet.", new Vector4f(1, 0, 0, 1));
				}else if(errorCode == Packet.ERROR_NO_USERNAME_EMAIL || errorCode == Packet.ERROR_WRONG_PASSWORD) {
					SkyFightClient.loadingGUI.setVisible(false);
					SkyFightClient.loginGUI.setVisible(true);
					new PopUp("Falscher Benutzername/Email oder falsches Passwort.", new Vector4f(1, 0, 0, 1));
				}else if(errorCode == Packet.ERROR_COULD_NOT_RESET_PASSWORD) {
//					SkyFightClient.loadingGUI.setVisible(false);
//					SkyFightClient.loginGUI.setVisible(true);
//					new PopUp("Falscher Benutzername/Email oder falsches Passwort.", new Vector4f(1, 0, 0, 1));
				}
			}
		}
	}
	
	@EventHandler
	public void onErrrorConnectino(ConnectionErrorEvent e) {
		if(e.getConnection().getPort() == Connection.HANDLE_SERVER_PORT && e.getConnection().getIP().equals(Connection.HANDLE_SERVER_IP)) {
			if(SkyFightClient.gameState == GameState.MENUE) {
				if(notConnected != null) {
					notConnected.destroy();
				}
				notConnected = new PopUp("Es konnte keine Verbindung mit dem Server hergestellt werden.", new Vector4f(1, 0, 0, 1), true);
			}
			e.getConnection().retry();
		}
	}
	
	@EventHandler
	public void onConnectConnection(ConnectionConnectedEvent e) {
		if(e.getConnection().getPort() == Connection.HANDLE_SERVER_PORT && e.getConnection().getIP().equals(Connection.HANDLE_SERVER_IP)) {
			if(SkyFightClient.gameState == GameState.MENUE) {
				if(notConnected != null) {
					notConnected.destroy();
				}
				new PopUp("Verbindung zum Server hergestellt", new Vector4f(0, 1, 0, 1));
			}
		}
	}
	
	@EventHandler
	public void onDisconnectConnection(ConnectionDisconnectedEvent e) {
		if(e.getConnection().getPort() == Connection.HANDLE_SERVER_PORT && e.getConnection().getIP().equals(Connection.HANDLE_SERVER_IP)) {
			if(SkyFightClient.gameState == GameState.MENUE) {
				if(notConnected != null) {
					notConnected.destroy();
				}
				notConnected = new PopUp("Die Verbindung zum Server wurde unterbrochen.", new Vector4f(1, 0, 0, 1), true);
			}
			e.getConnection().retry();
		}
	}

}
