package de.Luca.WorldEditor;

import de.Luca.Blocks.BlockData;
import de.Luca.Loading.Loader;

public class BlockDataPre extends BlockData{
	
	private String texture;
	
	public BlockDataPre(float value, float hardness, String name, String texture) {
		super(value, hardness, name, Loader.loadTexture(texture));
		this.texture = texture;		
	}

	public String getTexture() {
		return texture;
	}
	

}
