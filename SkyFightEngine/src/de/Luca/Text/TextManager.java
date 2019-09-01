package de.Luca.Text;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.Calc;
import de.Luca.Entities.Texture;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Shader.GUIShader;
import de.Luca.Utils.WorldPosition;
import de.Luca.Window.Window;
import de.t0b1.freetype_wrapper.classes.Font;
import de.t0b1.freetype_wrapper.classes.FontAtlas;
import de.t0b1.freetype_wrapper.classes.FontGlyph;

public class TextManager {
	
	public static TextManager manager;
	
	private HashMap<String, Long> fonts;
	private ArrayList<Text> texts;
	private GUIShader shader;
	private Texture tex;
	private HashMap<Long, HashMap<Character, FontGlyph>> glyphs;
	private float[][] buffer;
	private boolean changed;
	private Matrix4f projection;
	
	public TextManager() {
		if(manager == null) {
			fonts = new HashMap<String, Long>();
			glyphs = new HashMap<Long, HashMap<Character, FontGlyph>>();
			texts = new ArrayList<Text>();
			shader = new GUIShader();
			buffer = new float[2][];
			changed = true;
			manager = this;
		}else {
			throw new IllegalStateException("TextManager already created");
		}
	}
	
	public void generateFont(String file, float fontSize, String name) {
		long font = FontAtlas.addFont(file, fontSize, false, false);
		FontAtlas.hasUpdated();
		FontAtlas.build(0);
		ByteBuffer buffer = BufferUtils.createByteBuffer(FontAtlas.getTexDataAsRGBA32().data.length);
		buffer.put(FontAtlas.getTexDataAsRGBA32().data);
		buffer.flip();
		tex = new Texture(buffer, FontAtlas.getTexDataAsRGBA32().width, FontAtlas.getTexDataAsRGBA32().height);
		MasterRenderer.masterRenderer.queueTexture(tex);
		
		udpateFontGlyphs();
		glyphs.put(font, sortFontGlyphs(Font.getAllGlyphs(font)));
		fonts.put(name, font);
	}
	
	private HashMap<Character, FontGlyph> sortFontGlyphs(FontGlyph[] glyphs){
		HashMap<Character, FontGlyph> t = new HashMap<Character, FontGlyph>();
		for(FontGlyph g : glyphs) {
			t.put(g.codepoint, g);
		}
		return t;
	}
	
	private void udpateFontGlyphs() {
		for(long font : glyphs.keySet()) {
			glyphs.put(font, sortFontGlyphs(Font.getAllGlyphs(font)));
		}
	}
	
	
	public boolean hasChanged() {
		return changed;
	}
	
	public long getFont(String name) {
		if(fonts.containsKey(name)) {
			return fonts.get(name);
		}else {
			return -1;
		}
	}
	
	public void addText(Text text) {
		synchronized (texts) {
			texts.add(text);
		}
		changed = true;
	}
	
	public Texture getTexture() {
		return tex;
	}
	
	public float[][] getBuffer() {
		if(!changed && buffer != null) {
			return buffer;
		}
		changed = false;
		buffer[0] = genVertexBuffer();
		buffer[1] = genTextureBuffer();
		return buffer;
	}
	
	public FontGlyph getGlyph(long font, char c) {
		return glyphs.get(font).get(c);
//		return Font.findChar(font, c);
	}
	
	private float[] genTextureBuffer() {
		int size = 0;
		for(Text t : texts) {
			size += t.getText().toCharArray().length * 8;
		}
		float[] textureCoords = new float[size];
		int pointer = 0;
		for(Text t : texts) {
			for(char c : t.getText().toCharArray()) {
				System.out.println(pointer + 8);
				FontGlyph glyph = getGlyph(t.getFont(), c);//glyphs[c];
				textureCoords[pointer] = glyph.u0;
				textureCoords[pointer + 1] = glyph.v1;
				textureCoords[pointer + 2] = glyph.u0;
				textureCoords[pointer + 3] = glyph.v0;
				textureCoords[pointer + 4] = glyph.u1;
				textureCoords[pointer + 5] = glyph.v1;
				textureCoords[pointer + 6] = glyph.u1;
				textureCoords[pointer + 7] = glyph.v0;
				System.out.println("GLYPH: " + glyph.codepoint);
//				for(int i = 0; i < 8; i++) {
//					System.out.println(textureCoords[pointer + i]);
//				}
				pointer += 8;
				
			}
		}
		return textureCoords;
	}
	
	private float[] genVertexBuffer() {
		int size = 0;
		for(Text t : texts) {
			size += t.getText().toCharArray().length * 8;
		}
		float[] verticies = new float[size];
		int pointer = 0;
		for(Text t : texts) {
			for(int i = 0; i < t.getText().toCharArray().length; i++) {
				verticies[pointer] = 0;
				verticies[pointer + 1] = 0;
				verticies[pointer + 2] = 0;
				verticies[pointer + 3] = 1;
				verticies[pointer + 4] = 1;
				verticies[pointer + 5] = 0;
				verticies[pointer + 6] = 1;
				verticies[pointer + 7] = 1;
				pointer += 8;
			}
		}
		return verticies;
	}
	
	private void processProjectionMatrix() {
		boolean load = true;
		if(projection == null) {
			projection = Calc.getProjectionMatrix();
		}else {
			if(projection == Calc.getProjectionMatrix()) {
				load = false;
			}else {
				projection = Calc.getProjectionMatrix();
			}
		}
		if(load) {
			shader.loadProjectionMatrix(projection);	
		}
	}
	
	public void render() {
		Vector2f windowSize = Window.window.getWindowSize();
		if(tex == null) {
			throw new IllegalStateException("No Font initialized");
		}
		if(tex.getTextureID() == -1) {
			return;
		}
		shader.start();
//		shader.loadProjectionMatrix(Calc.getProjectionMatrix());
		processProjectionMatrix();
		MasterRenderer.masterRenderer.bindTexture(tex.getTextureID());
		synchronized (texts) {
			int offset = 0;
			for(Text t : texts) {
				shader.loadColor(t.getColor());
				Vector2f wc = WorldPosition.toOpenGLCoords(WorldPosition.getAbsCursorPos());
				float x = wc.x();
				float y = wc.y();
				for(char c : t.getText().toCharArray()) {
					FontGlyph glyph = getGlyph(t.getFont(), c);

					float width = glyph.x1 - glyph.x0;
					float height = glyph.y1 - glyph.y0;	
		 		
					Vector2f quadScale = WorldPosition.toOpenGLCoords(new Vector2f(width + (windowSize.x() / 2f), (windowSize.y() / 2f) - height));
										
					renderGlyph(glyph, x, y, quadScale, offset * 4);
					offset += 1;
					x += glyph.advanceX / (windowSize.x/2f);
				}
			}
		}
		shader.stop();
	}
	
	private void renderGlyph(FontGlyph glyph, float x, float y, Vector2f quadScale, int offset) {		
		
		Matrix4f transformation = Calc.getTransformationMatrix(new Vector2f(x, y), quadScale, 0);
		shader.loadTransformationMatrix(transformation);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4 + offset, 4);
	}

}
