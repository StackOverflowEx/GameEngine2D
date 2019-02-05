package gui;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import events.EventHandler;
import events.InputListener;
import rendering.DisplayManager;
import tools.Maths;
import tools.Mouse;

public class GUIElement {
	
	private static long counter = 0;
	private Vector2f position;
	private Vector2f size;
	private GUITexture texture;
	private GUIType type;
	private Matrix4f[] transforms;
	private long id;
	private long parent = -1;
	
	private final float CORNER_SCALE = 0.02f;
	
	public GUIElement(Vector2f position, Vector2f size, GUITexture texture, GUIType type) {
		super();
		this.position = position;
		this.size = size;
		this.texture = texture;
		this.type = type;
		this.id = counter;
		counter++;
		transforms = new Matrix4f[9];
		loadTransformationMatricies();
	}
	
	void setParent(long parent) {
		this.parent = parent;
	}
	
	public long getParent() {
		return parent;
	}
	
	public long getID() {
		return id;
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
	
	public static Vector2f getDisplayCoords(Vector2f percentage) {
		Vector2f v = DisplayManager.getWindowSize();
		return new Vector2f(v.x * percentage.x, v.y * percentage.y);	
	}
	
	public static Vector2f getPercentageCoords(Vector2f displayCoords) {
		Vector2f v = DisplayManager.getWindowSize();
		return new Vector2f(displayCoords.x / v.x, displayCoords.y / v.y);	
	}
	
	public void addInputListener(InputListener il) {
		EventHandler.registerListener(il);
	}
	
	public void loadTransformationMatricies() {	
		Vector2f displayPos = getDisplayCoords(position);
		Vector2f nn = Mouse.getWorldPosWithoutCamera(new Vector2f(0, 0));;
		
		Vector2f worldPos = Mouse.getWorldPosWithoutCamera(displayPos);
		Vector2f worldScale = new Vector2f(size.x *2, size.y * 2);
		worldScale.y = Math.abs(nn.y * worldScale.y);
		worldScale.x = Math.abs(nn.x * worldScale.x);

		if(texture.getTopLeftCorner() != null && texture.getTopLine() != null) {
			float x, y, scaleX, scaleY;
			//top left
			x = worldPos.x;
			y = worldPos.y - CORNER_SCALE;
			transforms[1] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE));
			//top right
			x = worldPos.x + worldScale.x - CORNER_SCALE;
			y = worldPos.y ;//- CORNER_SCALE;
			transforms[2] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE), 270);
			//bottom right
			x = worldPos.x + worldScale.x;// - CORNER_SCALE;
			y = worldPos.y - worldScale.y + CORNER_SCALE;
			transforms[3] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE), 180);
			//bototm left
			x = worldPos.x + CORNER_SCALE;
			y = worldPos.y - worldScale.y;
			transforms[4] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(CORNER_SCALE, CORNER_SCALE), 90);
			//top line
			x = worldPos.x + CORNER_SCALE;
			y = worldPos.y - CORNER_SCALE;
			scaleX = worldScale.x - 2*CORNER_SCALE;
			scaleY = CORNER_SCALE;
			transforms[5] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
			//right line
			x = worldPos.x + worldScale.x - CORNER_SCALE;
			y = worldPos.y - worldScale.y + CORNER_SCALE;
			scaleX = CORNER_SCALE;
			scaleY = worldScale.y - 2*CORNER_SCALE;
			transforms[6] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
			//bottom line
			x = worldPos.x + CORNER_SCALE;
			y = worldPos.y - worldScale.y;
			scaleX = worldScale.x - 2*CORNER_SCALE;
			scaleY = CORNER_SCALE;
			transforms[7] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
			//left line
			x = worldPos.x;
			y = worldPos.y - worldScale.y + CORNER_SCALE;
			scaleX = CORNER_SCALE;
			scaleY = worldScale.y - 2*CORNER_SCALE;
			transforms[8] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
			//body
			x = worldPos.x + CORNER_SCALE;
			y = worldPos.y - worldScale.y + CORNER_SCALE;
			scaleX = worldScale.x - 2*CORNER_SCALE;
			scaleY = worldScale.y - 2*CORNER_SCALE;
			transforms[0] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(scaleX, scaleY));
		}else {
			//bototm left
			float x, y;
			x = worldPos.x;
			y = worldPos.y - worldScale.y;
			transforms[0] = Maths.getTransformationMatrix(new Vector2f(x, y), new Vector2f(worldScale.x, worldScale.y));
		}
	}

}
