package block;

import org.lwjgl.util.vector.Vector2f;

import models.RenderObject;
import tools.Mouse;

public class Block extends RenderObject{
	
	private static final float SCALE = 0.04f;
	private float breakpoints;
	private float value;
	private Vector2f gridPos;
	private boolean fakeBlock;
	
	public Block(BlockModel model, int gridX, int gridY, boolean fakeBlock) {
		super(model, toWorldPos(new Vector2f(gridX, gridY)), SCALE);
		gridPos = new Vector2f(gridX, gridY);
		breakpoints = model.getHardness();
		value = model.getValue();
		this.fakeBlock = fakeBlock;
	}
	
	public static Vector2f toWorldPos(Vector2f gridPos) {
		return new Vector2f(gridPos.x * SCALE, gridPos.y * SCALE);
	}
	
	public boolean isFakeBlock() {
		return fakeBlock;
	}

	public float getValue() {
		return value;
	}
	
	public float getBreakPoints() {
		return breakpoints;
	}
	
	public void removebreakPoints(float amount) {
		breakpoints -= amount;
	}
	
	public Vector2f getGridPos() {
		return gridPos;
	}
	
	public static float getSCALE() {
		return SCALE;
	}
	
}
