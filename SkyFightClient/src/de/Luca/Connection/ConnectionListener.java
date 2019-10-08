package de.Luca.Connection;

import org.joml.Vector4f;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.GUIs.PopUp;
import de.Luca.GameLogic.GameState;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.GameServerHandler;
import de.Luca.Networking.HandelServerPacketHandler;
import de.Luca.Networking.ServerTicker;
import de.Luca.Packets.Packet;

public class ConnectionListener implements Listener{
	
	private PopUp notConnected;
	
	@EventHandler
	public void onPacket(ConnectionRecieveEvent e) {
		if(e.getConnection() == SkyFightClient.handleServerConnection) {
			Packet packet = e.getPacket();
			HandelServerPacketHandler.handlePacket(packet);
		}else {
			Packet packet = e.getPacket();
			GameServerHandler.handlePacket(packet);
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
		}else {
			if(SkyFightClient.gameState == GameState.RUNNING) {
				ServerTicker.stopTicking();
				SkyFightClient.gameState = GameState.MENUE;
				SkyFightClient.endGUI.setVisible(true);
				SkyFightClient.endGUI.showAbort();
			}
		}
	}

}
