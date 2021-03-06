package de.Luca.Entities;

import java.util.UUID;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.BlockData;
import de.Luca.GIF.Animation;
import de.Luca.Models.Model;
import de.Luca.Models.RenderModel;
import de.Luca.Models.Texture;

public class Arrow extends Entity{
	
	//Repr�sentiert einen Pfeil

	//x- und y-Geschwindigkeit
	private float yVel, xVel;
	//Sch�tze
	private Player shooter;
	//Zeit in ms wann der Pfeil erstellt wurde
	private long created;
	//ID des Pfeils
	private UUID id;
	
	public Arrow(Vector2f worldPos, Texture tex, float yVel, float xVel, Player shooter) {
		super(worldPos, 1, 1);
		this.xVel = xVel;
		id = UUID.randomUUID();
		created = System.currentTimeMillis();
		this.shooter = shooter;
		this.yVel = yVel;
		this.renderModel[0] = new RenderModel(new Vector2f(0, 0), new Model(tex, new Vector2f(BlockData.BLOCK_SCALE, BlockData.BLOCK_SCALE)), 0);
		calcOpenGLPos();
	}
	
	public void setUUID(String uuid) {
		this.id = UUID.fromString(uuid);
	}

	public UUID getUUID() {
		return id;
	}
	
	//Berechnet die OpenGL-Position
	@Override
	protected void calcOpenGLPos() {
		float x = worldPos.x * BlockData.BLOCK_SCALE;
		float y = worldPos.y * BlockData.BLOCK_SCALE;
		Vector2f openGL = new Vector2f(x, y);
		renderModel[0].setLocation(new Vector2f(openGL.x, openGL.y));
	}
	
	public long getCreated() {
		return created;
	}
	
	public Player getShooter() {
		return shooter;
	}
	
	public float getyVel() {
		return yVel;
	}
	
	public float getxVel() {
		return xVel;
	}
	
	public void setyVel(float yVel) {
		this.yVel = yVel;
	}
	
	public void setxVel(float xVel) {
		this.xVel = xVel;
	}

	//Hitbox f�r den Pfeil
	@Override
	public Vector4f getHitBox(Vector2f position) {
		Vector4f hitbox = new Vector4f(position.x + 0.1f, position.y + 0.1f, position.x + 1f - 0.1f, position.y + 1f - 0.1f);		
		return hitbox;
	}

	@Override
	public void startAnimation(int i, Animation a) {}

	@Override
	public void updateAnimation() {}

	@Override
	public void visibleUpdate() {
		// TODO Auto-generated method stub
		
	}
}
