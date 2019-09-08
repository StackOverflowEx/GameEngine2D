package de.Luca.Connection;

public class DemonInfo {

	private int ping;
	private int freeRamMB;
	private float load;
	
	public DemonInfo(int ping, int freeRamMB, float load) {
		super();
		this.ping = ping;
		this.freeRamMB = freeRamMB;
		this.load = load;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public int getFreeRamMB() {
		return freeRamMB;
	}

	public void setFreeRamMB(int freeRamMB) {
		this.freeRamMB = freeRamMB;
	}

	public float getLoad() {
		return load;
	}

	public void setLoad(float load) {
		this.load = load;
	}
	
}
