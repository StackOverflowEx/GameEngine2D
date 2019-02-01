package gui;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import rendering.DisplayManager;
import tools.Maths;
import tools.Mouse;

public class GUI {
	
	private Vector2f position;
	private Vector2f size;
	private GUITexture texture;
	private GUIType type;
	private Matrix4f[] transforms;
	
	private final float CORNER_SCALE = 0.02f;
		
	public GUI(Vector2f position, Vector2f size, GUITexture texture, GUIType type) {
		super();
		this.position = position;
		this.size = size;
		this.texture = texture;
		this.type = type;
		transforms = new Matrix4f[9];
		loadTransformationMatricies();
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getSize() {
		return size;
	}

	public GUITexture getTexture() {
		return texture;
	}

	public GUIType getType() {
		return type;
	}
	
	public void setPosition(Vector2f pos) {
		this.position = pos;
		loadTransformationMatricies();
	}
	
	public void move(Vector2f add) {
		position.x += add.x;
		position.y += add.y;
		loadTransformationMatricies();
	}
	
	public void setSize(Vector2f scale) {
		this.size = scale;
		loadTransformationMatricies();
	}
	
	public void resize(Vector2f add) {
		size.x = add.x;
		size.y = add.y;
		loadTransformationMatricies();
	}
	
	public Matrix4f[] getTransformations() {
		return transforms;
	}
	
	private Vector2f getDisplayCoords(Vector2f percentage) {
		Vector2f v = DisplayManager.getWindowSize();
		return new Vector2f(v.x * percentage.x, v.y * percentage.y);	
	}
	
	public void loadTransformationMatricies() {	
		Vector2f displaySize = getDisplayCoords(size);
		Vector2f displayPos = getDisplayCoords(position);
		
		Vector2f worldPos = Mouse.getWorldPos(displayPos);
		Vector2f worldScale = Mouse.getWorldPos(displaySize);
		worldScale.x = Math.abs(worldScale.x * 2.0f);
		worldScale.y = Math.abs(worldScale.y * 2.0f);

		float x, y, scaleX, scaleY;
		//top left
		x = worldPos.x;
		y = worldPos.y - CORNER_SCALE;
		transforms[0] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE));
		//top right
		x = worldPos.x + worldScale.x - CORNER_SCALE;
		y = worldPos.y - CORNER_SCALE;
		transforms[1] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE));
		//bottom right
		x = worldPos.x + worldScale.x - CORNER_SCALE;
		y = worldPos.y -worldScale.y;
		transforms[2] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE));
		//bototm left
		x = worldPos.x;
		y = worldPos.y - worldScale.y;
		transforms[3] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE));
		//top line
		x = worldPos.x + CORNER_SCALE;
		y = worldPos.y - CORNER_SCALE;
		scaleX = worldScale.x - 2*CORNER_SCALE;
		scaleY = CORNER_SCALE;
		transforms[4] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
		//right line
		x = worldPos.x + worldScale.x - CORNER_SCALE;
		y = worldPos.y - worldScale.y + CORNER_SCALE;
		scaleX = CORNER_SCALE;
		scaleY = worldScale.y - 2*CORNER_SCALE;
		transforms[5] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
		//bottom line
		x = worldPos.x + CORNER_SCALE;
		y = worldPos.y - worldScale.y;
		scaleX = worldScale.x - 2*CORNER_SCALE;
		scaleY = CORNER_SCALE;
		transforms[6] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
		//left line
		x = worldPos.x;
		y = worldPos.y - worldScale.y + CORNER_SCALE;
		scaleX = CORNER_SCALE;
		scaleY = worldScale.y - 2*CORNER_SCALE;
		transforms[7] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
		//body
		x = worldPos.x + CORNER_SCALE;
		y = worldPos.y - worldScale.y + CORNER_SCALE;
		scaleX = worldScale.x - 2*CORNER_SCALE;
		scaleY = worldScale.y - 2*CORNER_SCALE;
		transforms[8] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
	}

}
