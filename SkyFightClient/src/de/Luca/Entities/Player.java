package de.Luca.Entities;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.BlockData;
import de.Luca.Effects.Effect;
import de.Luca.GIF.Animation;
import de.Luca.GameLogic.GameManager.HOTBARSLOT;
import de.Luca.Main.SkyFightClient;
import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;

public class Player extends Entity{
	
	//repräsentiert den Spieler
	
	//Animaiton des oberen und unteren Teil des Spielers
	private Animation down, up;
	//Textur des oberen und unteren Teil des Spielers
	private Texture defaultDown, defaultUp;
	//Boolean, ob der SPieler fliegt
	private boolean fly;
	//y-Velocity
	private float yVel;
	//Effekt bzw. Anzeige, welches Item der Spieler in der Hand hält
	private Effect e;
	//ausgewählte Hotbarslot
	private HOTBARSLOT selected;
		
	public Player(Texture up, Texture down, Vector2f worldPos) {
		super(worldPos, 2, 1);
		this.defaultDown = down;
		this.defaultUp = up;
		this.yVel = 0;
		this.fly = false;
		e = new Effect(SkyFightClient.IngameOverlayHotbarSword, new Vector2f(worldPos).add(new Vector2f(0f, 1f)), new Vector2f(BlockData.BLOCK_SCALE, BlockData.BLOCK_SCALE));
		if(!isVisible()) {
			e.stop();
		}
		this.renderModel[1] = new RenderModel(new Vector2f(0, 0), new Model(up, new Vector2f(BlockData.BLOCK_SCALE, BlockData.BLOCK_SCALE)), 0);
		this.renderModel[0] = new RenderModel(new Vector2f(0, 0), new Model(down, new Vector2f(BlockData.BLOCK_SCALE, BlockData.BLOCK_SCALE)), 0);
		calcOpenGLPos();
	}
	
	public float getyVel() {
		return yVel;
	}
	
	//Setzt den Hotbarslot
	public void setSelected(HOTBARSLOT s) {
		if(isVisible()) {
			if(!s.equals(selected)) {
				if(s.equals(HOTBARSLOT.SWORD)) {
					e.changeTexture(SkyFightClient.IngameOverlayHotbarSword);
				}else if(s.equals(HOTBARSLOT.BOW)) {
					e.changeTexture(SkyFightClient.IngameOverlayHotbarBow);
				}else if(s.equals(HOTBARSLOT.PICKAXE)) {
					e.changeTexture(SkyFightClient.IngameOverlayHotbarPickaxe);
				}else if(s.equals(HOTBARSLOT.BLOCK)) {
					e.changeTexture(SkyFightClient.IngameOverlayHotbarBlockStone);
				}
			}
			e.play();
		}
		selected = s;
	}
	
	public void setyVel(float vel) {
		this.yVel = vel;
	}
	
	//Berechnet die OpenGL-Koordinaten
	protected void calcOpenGLPos() {
		e.setWorldPos(new Vector2f(worldPos).add(new Vector2f(0f, 1f)));
		float x = worldPos.x * BlockData.BLOCK_SCALE;
		float y = worldPos.y * BlockData.BLOCK_SCALE;
		Vector2f openGL = new Vector2f(x, y);
		renderModel[0].setLocation(new Vector2f(openGL.x, openGL.y));
		renderModel[1].setLocation(new Vector2f(openGL.x, openGL.y + BlockData.BLOCK_SCALE - 0.001f));
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


	// 0 = UNTEN
	//1 = OBEN
	
	//updatet die Animation
	@Override
	public void updateAnimation() {
		try {
			if(down != null) {
				if(down.isRunning()) {
					this.renderModel[0].getModel().setTexture(down.getFrame());
				}else {
					stopAnimation(0);
					down = null;
				}
			}
			if(up != null) {
				if(up.isRunning()) {
					this.renderModel[1].getModel().setTexture(up.getFrame());
				}else {
					stopAnimation(1);
					up = null;
				}
			}
		}catch (NullPointerException e) {}
	}
	
	//gibt einen Boolean zurück, ob eine Animation läuft
	public boolean isAnimationRunning(int i) {
		if(i == 0) {
			if(down != null) {
				return down.isRunning();
			}
		}else {
			if(up != null) {
				return up.isRunning();
			}
		}
		return false;
	}
	
	public String getAnimationTitle(int i) {
		if(i == 0) {
			if(down != null && down.isRunning()) {
				return down.getTitle();
			}
		}else {
			if(up != null && up.isRunning()) {
				return up.getTitle();
			}
		}
		return "";
	}
	
	//Stopt die Animaiton
	public void stopAnimation(int i) {
		try {
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
		}catch (NullPointerException e) {}
	}

	//started eine Animaiton
	@Override
	public void startAnimation(int i, Animation a) {
		Animation copy = a.copy();
		copy.start(false);
		stopAnimation(i);
		if(i == 0) {
			down = copy;
		}else {
			up = copy;
		}
	}

	@Override
	public void visibleUpdate() {
		e.stop();
	}

}
