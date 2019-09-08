package de.Luca.World;

import de.Luca.Blocks.BlockData;
import de.Luca.Loading.Loader;
import de.Luca.Sound.AudioManager;

public class BlockDataPre extends BlockData{
	
	private String texture;
	private String breakSoundFile;
	private String placeSoundFile;
	private String walkSoundFile;
	
	public BlockDataPre(float value, float hardness, String name, String texture, String breakSound, String placeSound, String walkSound) {
		super(value, hardness, name, Loader.loadTexture(texture, "block"), breakSound, placeSound, walkSound);
		this.texture = texture;	
		this.breakSoundFile = breakSound;
		this.placeSoundFile = placeSound;
		this.walkSoundFile = walkSound;
	}

	public String getTexture() {
		return texture;
	}

	public String getBreakSoundFile() {
		return breakSoundFile;
	}

	public void setBreakSoundFile(String breakSound) {
		this.breakSoundFile = breakSound;
		if(breakSoundFile != null) {
			setBreakSound(AudioManager.loadSound(breakSound, "block"));
		}
	}

	public String getPlaceSoundFile() {
		return placeSoundFile;
	}

	public void setPlaceSoundFile(String placeSound) {
		this.placeSoundFile = placeSound;
		if(placeSoundFile != null) {
			setPlaceSound(AudioManager.loadSound(placeSound, "block"));
		}
	}

	public String getWalkSoundFile() {
		return walkSoundFile;
	}

	public void setWalkSoundFile(String walkSound) {
		this.walkSoundFile = walkSound;
		if(walkSoundFile != null) {
			setWalkSound(AudioManager.loadSound(walkSound, "block"));
		}
	}

	public void setTexture(String texture) {
		this.texture = texture;
		getBlockModel().getModel().setTexture(Loader.loadTexture(texture, "block"));
	}
	
	

}
