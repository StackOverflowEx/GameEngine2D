package de.Luca.Events;

public class Event {
	
	private long timeStamp;
	private boolean cancelled;
	
	public Event(long timeStamp) {
		this.timeStamp = timeStamp;
		this.cancelled = false;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean c) {
		this.cancelled = c;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

}
