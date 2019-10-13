package de.Luca.Blocks;

import org.joml.Vector2f;

import de.Luca.Sound.AudioManager;
import de.Luca.Sound.SoundData;
import de.Luca.Sound.Source;

public class Block {
	
	private BlockData blockData;
	private Vector2f worldPos;
	private float breakPercentage;
	private Source source;
	
	public Block(BlockData blockData, Vector2f worldPos) {
		this.blockData = blockData;
		this.breakPercentage = 0;
		setPosition(worldPos);
	}
	
	public void playSound(SoundData d, float pitch, float volume) {
		if((source != null && source.isDeleted()) || (source != null && source.isPlaying())) {
			return;
		}
		source = AudioManager.genSource();
		source.setPosition(worldPos);
		source.setPitch(pitch);
		source.setVolume(volume);
		source.playSound(d);
		AudioManager.deleteWhenFinished(source);
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
