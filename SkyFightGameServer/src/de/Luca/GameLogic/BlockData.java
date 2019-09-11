package de.Luca.GameLogic;

public class BlockData {
	
	private String name;
	private float vlaue;
	private float hardness;
	
	public BlockData(String name, float vlaue, float hardness) {
		super();
		this.name = name;
		this.vlaue = vlaue;
		this.hardness = hardness;
	}

	public String getName() {
		return name;
	}

	public float getVlaue() {
		return vlaue;
	}

	public float getHardness() {
		return hardness;
	}
	
	

}
