package de.Luca.Entities;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockManager;
import de.Luca.GIF.Animation;
import de.Luca.Models.RenderModel;
import de.Luca.Sound.AudioManager;
import de.Luca.Sound.SoundData;
import de.Luca.Sound.Source;

public abstract class Entity {
	
	protected Vector2f worldPos, nextPos, groundPos;
	protected RenderModel[] renderModel;
	private boolean visible;
	private boolean onGround;
	private ArrayList<Block> colliding;
	private boolean collisionWithBlocks;
	private ConcurrentHashMap<SoundData, Source> audioSources;
	
	public Entity(Vector2f worldPos, int yHeight, int xWidth) {
		this.renderModel = new RenderModel[yHeight * xWidth];
		audioSources = new ConcurrentHashMap<SoundData, Source>();
		this.worldPos = worldPos;
		this.visible = true;
		this.onGround = true;
		collisionWithBlocks = true;
		EntityManager.addEntity(this);
	}
	
	public void setPosition(Vector2f pos) {
		this.worldPos = pos;
		calcOpenGLPos();
	}
	
	public void setCollisionWithBlocks(boolean b) {
		this.collisionWithBlocks = b;
	}
	
	public boolean canCollideWithBlocks() {
		return collisionWithBlocks;
	}
	
	public Block getBlockBelow() {
		if(onGround) {
			Vector4f hitbox = getHitBox(worldPos);
			float x = hitbox.x + (hitbox.z - hitbox.x) / 2f;
			float y = hitbox.y - 0.1f;
			Block b = BlockManager.getBlock(new Vector2f(x, y));
			return b;
		}
		return null;
	}
	
	public boolean isPlaying(SoundData d) {
		return audioSources.containsKey(d);
	}
	
	public void playSound(SoundData d, float maxAudibleDistance, boolean loop) {
		Source source = AudioManager.genSource();
		audioSources.put(d, source);
		source.setMaxAudibleDistanace(maxAudibleDistance);
		source.setLoop(loop);
		source.playSound(d);
	}
	
	public void stopSound(SoundData d) {
		Source source = audioSources.get(d);
		if(source != null) {
			source.delete();
			audioSources.remove(d);
		}
	}
	
	public void updateSound() {
		for(SoundData sd : audioSources.keySet()) {
			Source source = audioSources.get(sd);
			if(!source.isPlaying()) {
				if(!source.isLoop()) {
					source.delete();
					audioSources.remove(sd);
					continue;
				}else {
					source.continuePlaying();
				}
			}
			float distance = source.getDistanceToListener();
			if(distance > source.getMaxAudibleDistance()) {
				source.delete();
				audioSources.remove(sd);
			}
		}
	}
	
	public RenderModel[] getModels() {
		return renderModel;
	}
	
	public abstract void updateAnimation();
	
	public abstract void startAnimation(int i, Animation a);
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isOnGround() {
		return onGround;
	}
	
	public Vector2f getWorldPos() {
		return worldPos;
	}
	
	public abstract Vector4f getHitBox(Vector2f position);

	public void move(Vector2f addPos) {
		nextPos = new Vector2f(worldPos.x + addPos.x, worldPos.y + addPos.y);
		if(canCollideWithBlocks()) {
			float y = nextPos.y;
			if(y > 0) {
				y = (int)Math.ceil(y);
			}else {
				y = (int)Math.floor(y);
			}
			colliding = BlockManager.isCollidingWithBlock(getHitBox(new Vector2f(worldPos.x, y - 0.000001f)));
			if(colliding.size() > 0) {
				onGround = true;
				nextPos.y = (int) y;
			}else {
				onGround = false;
			}
			colliding = BlockManager.isCollidingWithBlock(getHitBox(new Vector2f(nextPos.x, worldPos.y)));
			if(colliding.size() > 0) {
				nextPos.x = worldPos.x;
			}
		}
		if(nextPos.y != worldPos.y || nextPos.x != worldPos.x) {
			worldPos = nextPos;
			calcOpenGLPos();
		}
	}
	
	protected abstract void calcOpenGLPos();
	
}
