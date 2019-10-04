package de.Luca.Networking;

import org.joml.Vector4f;

import de.Luca.Connection.Connection;
import de.Luca.GUIs.PopUp;
import de.Luca.Main.SkyFightClient;
import de.Luca.Packets.Packet;

public class HandelServerPacketHandler {
	
	public static void handlePacket(Packet packet) {
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
//				SkyFightClient.loadingGUI.setVisible(false);
//				SkyFightClient.loginGUI.setVisible(true);
//				new PopUp("Falscher Benutzername/Email oder falsches Passwort.", new Vector4f(1, 0, 0, 1));
			}else {
				System.out.println("Unknown error recieved (ID: " + errorCode + ")");
			}
		}else if(packet.packetType == Packet.CONNECT){
			int port = (int) packet.a;
			String ip = (String) packet.b;
			if(ip.equals("127.0.0.1")) {
				ip = Connection.HANDLE_SERVER_IP;
			}
			new Connection(ip, port);
			System.out.println("MATCH STARTED");
		}
	}

}
