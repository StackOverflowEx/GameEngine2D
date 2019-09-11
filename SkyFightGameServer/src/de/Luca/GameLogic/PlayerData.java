package de.Luca.GameLogic;

public class PlayerData {
	
	private float x, y, health, hunger;
	private boolean breaking;
	private float xBlockBreak, yBlockBreak;
	private long lastHit, lastMove, breakStart;
	private boolean facingRight;
	
	public PlayerData() {
		this.x = 0;
		this.y = 0;
		this.health = 100;
		this.hunger = 100;
		this.breaking = false;
		this.lastHit = 0;
		this.facingRight = true;
		this.lastMove = 0;
		this.breakStart = 0;
	}
	
	
	public long getLastMove() {
		return lastMove;
	}



	public void setLastMove(long lastMove) {
		this.lastMove = lastMove;
	}



	public long getBreakStart() {
		return breakStart;
	}



	public void setBreakStart(long breakStart) {
		this.breakStart = breakStart;
	}



	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getHunger() {
		return hunger;
	}

	public void setHunger(float hunger) {
		this.hunger = hunger;
	}

	public boolean isBreaking() {
		return breaking;
	}

	public void setBreaking(boolean breaking) {
		this.breaking = breaking;
	}

	public float getxBlockBreak() {
		return xBlockBreak;
	}

	public void setxBlockBreak(float xBlockBreak) {
		this.xBlockBreak = xBlockBreak;
	}

	public float getyBlockBreak() {
		return yBlockBreak;
	}

	public void setyBlockBreak(float yBlockBreak) {
		this.yBlockBreak = yBlockBreak;
	}

	public long getLastHit() {
		return lastHit;
	}

	public void setLastHit(long lastHit) {
		this.lastHit = lastHit;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}
	
	

}
