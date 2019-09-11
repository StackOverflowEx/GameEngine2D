package de.Luca.Sound;

public class SoundData {
	
	private String file;
	private int id;
	
	public SoundData(int id, String file) {
		this.id = id;
		this.file = file;
	}
	
	public int getID() {
		return id;
	}
	
	public String getFile() {
		return file;
	}

}
