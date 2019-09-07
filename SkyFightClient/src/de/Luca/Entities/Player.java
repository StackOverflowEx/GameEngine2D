package de.Luca.Entities;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.BlockData;
import de.Luca.GIF.Animation;
import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;

public class Player extends Entity{
	
	private Animation down, up;
	private Texture defaultDown, defaultUp;
	private boolean fly;
		
	public Player(Texture up, Texture down, Vector2f worldPos) {
		super(worldPos, 2, 1);
		this.defaultDown = down;
		this.defaultUp = up;
		this.fly = false;
		this.renderModel[1] = new RenderModel(new Vector2f(0, 0), new Model(up, new Vector2f(BlockData.BLOCK_SCALE, BlockData.BLOCK_SCALE)), 0);
		this.renderModel[0] = new RenderModel(new Vector2f(0, 0), new Model(down, new Vector2f(BlockData.BLOCK_SCALE, BlockData.BLOCK_SCALE)), 0);
		calcOpenGLPos();
	}
	
	
	protected void calcOpenGLPos() {
		float x = worldPos.x * BlockData.BLOCK_SCALE;
		float y = worldPos.y * BlockData.BLOCK_SCALE;
		Vector2f openGL = new Vector2f(x, y);
		renderModel[0].setLocation(new Vector2f(openGL.x, openGL.y));
		renderModel[1].setLocation(new Vector2f(openGL.x, openGL.y + BlockData.BLOCK_SCALE));
	}

	public void setFlying(boolean flying) {
		this.fly = flying;
	}
	
	public boolean isFlying() {
		return fly;
	}
	
	public Vector4f getHitBox(Vector2f position) {
		return new Vector4f(position.x + 0.05f, position.y , position.x + 1f - 0.05f, position.y + 2f - 0.01f);
	}


	@Override
	public void updateAnimation() {
		if(down != null) {
			if(down.isRunning()) {
				this.renderModel[0].getModel().setTexture(down.getFrame());
			}else {
				stopAnimation(0);
			}
		}
		if(up != null) {
			if(up.isRunning()) {
				this.renderModel[1].getModel().setTexture(up.getFrame());
			}else {
				stopAnimation(1);
			}
		}
	}
	
	public void stopAnimation(int i) {
		if(i == 0) {
			if(down != null) {
				down.stop();
			}
			this.renderModel[0].getModel().setTexture(defaultDown);
		}else {
			if(up != null) {
				up.stop();
			}
			this.renderModel[1].getModel().setTexture(defaultUp);
		}
	}


	@Override
	public void startAnimation(int i, Animation a) {
		Animation copy = a.copy();
		copy.start(false);
		if(i == 0) {
			down = copy;
		}else {
			up = copy;
		}
	}

}
