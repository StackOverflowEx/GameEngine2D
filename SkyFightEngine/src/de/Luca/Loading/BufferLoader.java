package de.Luca.Loading;

import java.util.List;

import de.Luca.Entities.Entity;

public class BufferLoader {
	
	public static Frame loadFrameBuffer(List<Entity> enties) {
		
		return new Frame(enties);
		
	}

}
