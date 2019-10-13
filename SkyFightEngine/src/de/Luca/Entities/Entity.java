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
	
	//Ein Objekt der Klasse Entity repräsentiert ein Entity
	
	//Position des Entities, die nächste Position (nur für die Kollisionsberechnung nötig), Groundpos = letzte Position als der Spieler am Boden war.
	protected Vector2f worldPos, nextPos, groundPos;
	protected RenderModel[] renderModel;
	//Boolean, ob ein Entity sichtbar ist
	private boolean visible;
	//Boolean, ob sich das Entity auf dem Boden befindet.
	private boolean onGround;
	//Für die Kollisionserkennung (List der Blöcke, mit denen der Spieler kollidiert
	private ArrayList<Block> colliding;
	//Boolean, ob ein Entity mit Blöcken kollidieren kann.
	private boolean collisionWithBlocks;
	//Audiosources
	private ConcurrentHashMap<SoundData, Source> audioSources;
	//Boolean ob der Spieler nach rechts schaut
	private boolean facingRight;
	
	public Entity(Vector2f worldPos, int yHeight, int xWidth) {
		this.renderModel = new RenderModel[yHeight * xWidth];
		audioSources = new ConcurrentHashMap<SoundData, Source>();
		this.worldPos = worldPos;
		this.visible = true;
		this.onGround = true;
		facingRight = true;
		collisionWithBlocks = true;
		EntityManager.addEntity(this);
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
	
	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
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
	
	//Gibt den Block under dem Entity, wenn der Spieler auf dem Boden steht
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
	
	public abstract void visibleUpdate();
	
	public boolean isPlaying(SoundData d) {
		return audioSources.containsKey(d);
	}
	
	//Spielt einen Sound ab
	public void playSound(SoundData d, float maxAudibleDistance, boolean loop) {
		Source source = AudioManager.genSource();
		audioSources.put(d, source);
		source.setPosition(worldPos);
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
	
	//Updated den Sound
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
		visibleUpdate();
	}
	
	public boolean isOnGround() {
		return onGround;
	}
	
	public Vector2f getWorldPos() {
		return worldPos;
	}
	
	public abstract Vector4f getHitBox(Vector2f position);

	//Bewegt das Entity, ohne zu prüfen, ob eine Kollision mit einem Block vorliegt
	public void moveWithoutCollisionCheck(Vector2f addPos) {
		nextPos = new Vector2f(worldPos.x + addPos.x, worldPos.y + addPos.y);
		worldPos = nextPos;
		calcOpenGLPos();
	}
	
	//Bewegt ein Entity
	public void move(Vector2f addPos) {
		nextPos = new Vector2f(worldPos.x + addPos.x, worldPos.y + addPos.y);
		if(canCollideWithBlocks()) {
			//Zuerst wird für eine Blockkollision auf der y-Achse geprüft
			float y = nextPos.y;
			y = (int)Math.ceil(y);
			colliding = BlockManager.isCollidingWithBlock(getHitBox(new Vector2f(worldPos.x, y - 0.000001f)));
			if(colliding.size() > 0) {
				//Kollidiert ein Spieler wird er auf den Block gesetzt
				onGround = true;
				nextPos.y = (int) y;
				if(addPos.y > 0) {
					//Hat sich der Spieler nach oben bewegt und ist kollidiert, so war er im Sprung und wird nach unten gesetzt
					nextPos.y -= 1f;
				}
			}else {
				//Kollidiert der Spieler mit keinem Block, ist er nicht auf dem Boden
				onGround = false;
			}
			//Als nächstes wird für eine Kollision in x-Achse geprüft
			colliding = BlockManager.isCollidingWithBlock(getHitBox(new Vector2f(nextPos.x, worldPos.y)));
			if(colliding.size() > 0) {
				nextPos.x = worldPos.x;
			}
		}
		//Die Position des Spielers wird aktualisiert
		if(nextPos.y != worldPos.y || nextPos.x != worldPos.x) {
			worldPos = nextPos;
			calcOpenGLPos();
		}
	}
	
	protected abstract void calcOpenGLPos();
	
}
