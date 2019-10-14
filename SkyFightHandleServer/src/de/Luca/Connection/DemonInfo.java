package de.Luca.Connection;

import de.Luca.MySQL.DatabaseManager;

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
	
	private static long last = -1;
	public static void requestLoopStart() {
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					for(DemonConnectionHandler dch : DemonConnectionHandler.getHandler()) {
						dch.requestDemonInfo();
					}
					if(last == -1) {
						last = System.currentTimeMillis();
					}else {
						if((System.currentTimeMillis() - last) > 1000*60*15) {
							DatabaseManager.keepAlive();
							last = System.currentTimeMillis();
						}
					}
					try {
						Thread.sleep(1000*10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th.start();
	}
	
}
