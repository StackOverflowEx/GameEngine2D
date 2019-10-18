package de.Luca.Main;

import java.io.IOException;
import java.io.OutputStream;

import de.Luca.Packets.Packet;

public class HandleOutputStream extends OutputStream{

	private OutputStream outputStream;
	private int lineCount;
	private String last;
	private String error;
	private long time;
	
	public HandleOutputStream(OutputStream outputStream)
	{
		this.outputStream= outputStream; 
		lineCount = 0;
		time = System.currentTimeMillis();
		last = "";
		error = "";
	}
	
	@Override
	public void write(int b) throws IOException
	{
		outputStream.write(b);					
	}
	
	@Override
	public void write(byte[] b) throws IOException
	{
		outputStream.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		byte[] ns = new byte[len];
		for(int offs = off; offs < (off+len); offs++) {
			ns[offs - off] = b[offs];
		}
		String s = new String(ns);
		if(last.endsWith("\n") || last.isEmpty()) {
			s = lineCount + ": " + s;
			lineCount++;
		}
		last = s;
		error += s;
		
		outputStream.write(b, off, len);
	}
	
	public void update() {
		if((System.currentTimeMillis() - time) > 1000 && !error.isEmpty()) {
			Packet p = new Packet();
			p.packetType = Packet.PACKET_CLIENT_ERROR;
			p.a = error;
			if(SkyFightClient.handleServerConnection != null && SkyFightClient.handleServerConnection.isConnected() && SkyFightClient.handleServerConnection.finishedHandshaking()) {
				SkyFightClient.handleServerConnection.send(p);
			}
			error = "";
			time = System.currentTimeMillis();
		}
	}

	@Override
	public void flush() throws IOException
	{
		outputStream.flush();
	}

	@Override
	public void close() throws IOException
	{
		outputStream.close();
	}

}
