package de.Luca.Entities;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockManager;
import de.Luca.GIF.Animation;
import de.Luca.Models.RenderModel;

public abstract class Entity {
	
	protected Vector2f worldPos, nextPos, groundPos;
	protected RenderModel[] renderModel;
	private boolean visible;
	private boolean onGround;
	private ArrayList<Block> colliding;
	
	public Entity(Vector2f worldPos, int yHeight, int xWidth) {
		this.renderModel = new RenderModel[yHeight * xWidth];
		this.worldPos = worldPos;
		this.visible = true;
		this.onGround = true;
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
//		System.out.println(nextPos.x + 0.05f + " " + nextPos.y);
		if(nextPos.y != worldPos.y || nextPos.x != worldPos.x) {
			worldPos = nextPos;
			calcOpenGLPos();
		}
	}
	
	protected abstract void calcOpenGLPos();
	
}
