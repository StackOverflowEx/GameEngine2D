package de.Luca.Effects;

import org.joml.Vector2f;

import de.Luca.Blocks.BlockData;
import de.Luca.GIF.Animation;
import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;

public class Effect {
	
	//Ein Objekt der Klasse Effekt repräsentiert einen Effekt. Ein Effekt kann entweder statisch aus einer Textur bestehen oder aus einer Animation aus einem GIF
	
	//Animation, wenn animierter Effekt
	private Animation a;
	private RenderModel model;
	private Vector2f worldPos;
	//Textur, wenn statischer Effekt
	private Texture tex;
	//boolean ob der Effekt abgespielt wird oder beendet ist
	
	private boolean stopped = false;
	
	//Konstruktor für einen animierten Effekt
	public Effect(Animation effect, Vector2f worldPos, Vector2f size) {
		a = effect.copy();
		this.worldPos = worldPos;
		model = new RenderModel(new Vector2f(0, 0), new Model(size), 0);
	}
	
	//Konstruktor für einen statischen Effekt
	public Effect(Texture effect, Vector2f worldPos, Vector2f size) {
		tex = effect;
		this.worldPos = worldPos;
		model = new RenderModel(new Vector2f(0, 0), new Model(size), 0);
	}
	
	//Berechnung der OpenGL-Koordinaten
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
	
	//Der Effekt wird aktualisiert.
	public void update() {
		if(a != null) {
			//Ist der Effekt animiert, wird die Animation aktualisiert, indem ein neuer Frame gesetzt wird.
			Texture tex = a.getFrame();
			model.getModel().setTexture(tex);
		}else {
			model.getModel().setTexture(tex);
		}
	}
	
	//Gibt an, ob ein animierter Effekt abgespielt wird oder nicht
	public boolean isPlaying() {
		if(a != null) {
			return a.isRunning();
		}
		return !stopped;
	}
	
	//Spielt einen Animierten Effekt ab
	public void play() {
		if(a != null) {
			a.start(false);
		}
		stopped = false;
		EffectManager.addEffect(this);
	}
	
	//Stopt einen Effekt
	public void stop() {
		if(a != null) {
			a.stop();
		}
		stopped = true;
		EffectManager.removeEffect(this);
	}

}
