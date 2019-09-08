package de.Luca.Connection;

import org.joml.Vector4f;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.GUIs.PopUp;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.Packet;

public class ConnectionListener implements Listener{
	
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
					//Logged in;
					System.out.println("LOGGED IN");
				}else if((int) packet.a == Packet.REGISTRATION) {
					//Registered
				}
			}else if(packet.packetType == Packet.ERROR) {
				int errorCode = (int) packet.a;
				System.out.println("Recieved error: " + errorCode);
			}
		}
	}
	
	@EventHandler
	public void onErrrorConnectino(ConnectionErrorEvent e) {
		if(e.getConnection().getPort() == Connection.HANDLE_SERVER_PORT && e.getConnection().getIP().equals(Connection.HANDLE_SERVER_IP)) {
			if(SkyFightClient.loginGUI != null) {
				SkyFightClient.loginGUI.showNotConnected();
				e.getConnection().retry();
			}
		}
	}
	
	@EventHandler
	public void onErrrorConnection(ConnectionConnectedEvent e) {
		if(e.getConnection().getPort() == Connection.HANDLE_SERVER_PORT && e.getConnection().getIP().equals(Connection.HANDLE_SERVER_IP)) {
			if(SkyFightClient.loginGUI != null) {
				SkyFightClient.loginGUI.hideNotConnected();
				new PopUp("Verbindung zum Server hergestellt", new Vector4f(0, 1, 0, 1));
			}
		}
	}

}
