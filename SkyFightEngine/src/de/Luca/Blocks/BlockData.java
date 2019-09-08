package de.Luca.Blocks;

import org.joml.Vector2f;

import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;
import de.Luca.Sound.AudioManager;
import de.Luca.Sound.SoundData;

public class BlockData {
	
	public static final float BLOCK_SCALE = 0.05f;
	
	private float value;
	private float hardness;
	private String name;
	private RenderModel blockModel;
	private SoundData placeSound, breakSound, walkSound;
	
	public BlockData(float value, float hardness, String name, Texture blockTexture, String breakSound, String placeSound, String walkSound) {
		this.blockModel = new RenderModel(new Vector2f(0, 0), new Model(blockTexture, new Vector2f(BLOCK_SCALE, BLOCK_SCALE)), 0);
		this.value = value;
		this.hardness = hardness;
		this.name = name;
		if(placeSound != null) {
			this.placeSound = AudioManager.loadSound(placeSound, "block");
		}
		if(breakSound != null) {
			this.breakSound = AudioManager.loadSound(breakSound, "block");
		}
		if(walkSound != null) {
			this.walkSound = AudioManager.loadSound(walkSound, "block");
		}
	}

	public SoundData getPlaceSound() {
		return placeSound;
	}
	
	public SoundData getBreakSound() {
		return breakSound;
	}
	
	public SoundData getWalkSound() {
		return walkSound;
	}
	
	
	public void setValue(float value) {
		this.value = value;
	}

	public void setHardness(float hardness) {
		this.hardness = hardness;
	}

	public void setPlaceSound(SoundData placeSound) {
		this.placeSound = placeSound;
	}

	public void setBreakSound(SoundData breakSound) {
		this.breakSound = breakSound;
	}

	public void setWalkSound(SoundData walkSound) {
		this.walkSound = walkSound;
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
