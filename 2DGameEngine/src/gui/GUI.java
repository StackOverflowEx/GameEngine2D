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
	
	private final float CORNER_SCALE = 0.1f;
		
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
		Vector2f display = DisplayManager.getWindowSize();
		
		Vector2f worldPos = Mouse.getWorldPos(displayPos);
		Vector2f worldScale = new Vector2f(displaySize.x / (display.x / 2.0f), displaySize.y / (display.y / 2));

		float x, y;
		x = worldPos.x;
		y = worldPos.y - CORNER_SCALE;
		//y is calculated correct. Moving the mouse to the same y value as calculated shows that everything is correctly calculated
		System.out.println(y + " | " + Mouse.getWorldPos().y);
		transforms[0] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE));
	}

}
