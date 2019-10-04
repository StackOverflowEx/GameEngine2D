package de.Luca.GameTraffic;


import de.Luca.Connection.ConnectionHandler;
import de.Luca.Packets.GamePacket;
import de.Luca.Packets.Packet;

public class ServerTicker{
	
	private static ConnectionHandler con1, con2;
	private static GamePacket in1, in2;
	
	public static void executeTick() {
		
		if(con1 != null && con1.finishedHandshaking() && con1.isConnected() && con2 != null && con2.finishedHandshaking() && con2.isConnected()) {
			con1.send(craftPacketForPlayer(con1));
			con2.send(craftPacketForPlayer(con2));
		}
		
	}
	
	public static void recievePacket(GamePacket p, ConnectionHandler con) {
		if(con == con1) {
			in1 = p;
		}else {
			in2 = p;
		}
	}
	
	private static Packet craftPacketForPlayer(ConnectionHandler con) {
		
		GamePacket p = new GamePacket();
		p.packetType = Packet.GAME_DATA;
		p.setGamePacketType(GamePacket.POSITION);
		
		if(con == con1) {
			p.c = in2.c;
			p.d = in2.d;
			p.e = in2.e;
		}else {
			p.c = in1.c;
			p.d = in1.d;
			p.e = in1.e;
		}
		
		return p;
	}
	
	

}
