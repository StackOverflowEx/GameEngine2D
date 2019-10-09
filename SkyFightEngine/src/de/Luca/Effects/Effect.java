package de.Luca.Effects;

import org.joml.Vector2f;

import de.Luca.Blocks.BlockData;
import de.Luca.GIF.Animation;
import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;

public class Effect {
	
	private Animation a;
	private RenderModel model;
	private Vector2f worldPos;
	private Texture tex;
	private boolean stopped = false;
	
	public Effect(Animation effect, Vector2f worldPos, Vector2f size) {
		a = effect.copy();
		this.worldPos = worldPos;
		model = new RenderModel(new Vector2f(0, 0), new Model(size), 0);
	}
	
	public Effect(Texture effect, Vector2f worldPos, Vector2f size) {
		tex = effect;
		this.worldPos = worldPos;
		model = new RenderModel(new Vector2f(0, 0), new Model(size), 0);
	}
	
	protected void calcOpenGLPos() {
		float x = worldPos.x * BlockData.BLOCK_SCALE;
		float y = worldPos.y * BlockData.BLOCK_SCALE;
		Vector2f openGL = new Vector2f(x, y);
		model.setLocation(new Vector2f(openGL.x, openGL.y));
		model.setLocation(new Vector2f(openGL.x, openGL.y + BlockData.BLOCK_SCALE));
	}
	
	public void setWorldPos(Vector2f worldPos) {
		this.worldPos = worldPos;
		calcOpenGLPos();
	}
	
	public void changeTexture(Texture tex) {
		this.tex = tex;
		update();
	}
	
	public RenderModel getRenderModel() {
		return model;
	}
	
	public void update() {
		if(a != null) {
			Texture tex = a.getFrame();
			model.getModel().setTexture(tex);
		}else {
			model.getModel().setTexture(tex);
		}
	}
	
	public boolean isPlaying() {
		if(a != null) {
			return a.isRunning();
		}
		return !stopped;
	}
	
	public void play() {
		if(a != null) {
			a.start(false);
		}
		stopped = false;
		EffectManager.addEffect(this);
	}
	
	public void stop() {
		if(a != null) {
			a.stop();
		}
		stopped = true;
		EffectManager.removeEffect(this);
	}

}
