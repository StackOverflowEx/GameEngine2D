package de.Luca.Blocks;

import org.joml.Vector2f;

import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;

public class BlockData {
	
	public static final float BLOCK_SCALE = 0.05f;
	
	private float value;
	private float hardness;
	private String name;
	private RenderModel blockModel;
	
	public BlockData(float value, float hardness, String name, Texture blockTexture) {
		this.blockModel = new RenderModel(new Vector2f(0, 0), new Model(blockTexture, new Vector2f(BLOCK_SCALE, BLOCK_SCALE)), 0);
		this.value = value;
		this.hardness = hardness;
		this.name = name;
	}

	public float getValue() {
		return value;
	}

	public float getHardness() {
		return hardness;
	}

	public String getName() {
		return name;
	}

	public RenderModel getBlockModel() {
		return blockModel;
	}
	
	
}
