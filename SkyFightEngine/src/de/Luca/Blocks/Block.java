package de.Luca.Blocks;

import org.joml.Vector2f;

public class Block {
	
	private BlockData blockData;
	private Vector2f worldPos;
	private float breakPercentage;
	
	public Block(BlockData blockData, Vector2f worldPos) {
		this.blockData = blockData;
		this.breakPercentage = 0;
		setPosition(worldPos);
	}
	
	public void setBreakPercentage(float percentage) {
		this.breakPercentage = percentage;
	}
	
	public BlockData getBlockData() {
		return blockData;
	}
	
	public void setPosition(Vector2f worldPos) {
		this.worldPos = worldPos;
		this.getOpenGLPos();
	}
	
	public Vector2f getOpenGLPos() {
		float x = worldPos.x * BlockData.BLOCK_SCALE;
		float y = worldPos.y * BlockData.BLOCK_SCALE;
		return new Vector2f(x, y);
	}
	
	public float getBreakPercentage() {
		return breakPercentage;
	}
	
	public Vector2f getWorldPos() {
		return worldPos;
	}

}
