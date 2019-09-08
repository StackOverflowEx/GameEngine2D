package de.Luca.Connection;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

public class DemonInfo {

	private int freeRamMB;
	private float load;
	
	public DemonInfo(int freeRamMB, float load) {
		this.freeRamMB = freeRamMB;
		this.load = load;
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
	
	public static DemonInfo getDemonInfo() {
		OperatingSystemMXBean sys = ManagementFactory.getOperatingSystemMXBean();
		
		float load = (float) (sys.getSystemLoadAverage() / (float)sys.getAvailableProcessors());
		SystemInfo info = new SystemInfo();
		HardwareAbstractionLayer hal = info.getHardware();
		int freeRamMB = (int) (hal.getMemory().getAvailable() / 1024f / 1024f);
		return new DemonInfo(freeRamMB, load);
	}
	
}
