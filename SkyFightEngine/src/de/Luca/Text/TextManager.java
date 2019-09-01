package de.Luca.Text;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

//	public static TextManager manager;

	private static ConcurrentHashMap<String, Long> fonts;
	private static CopyOnWriteArrayList<Text> texts;
	private static GUIShader shader;
	private static Texture tex;
	private static ConcurrentHashMap<Long, HashMap<Character, FontGlyph>> glyphs;
	private static float[][] buffer;
	private static boolean changed;
	private static Matrix4f projection;

	public static void init() {
		if (fonts != null) {
			return;
		}
		fonts = new ConcurrentHashMap<String, Long>();
		glyphs = new ConcurrentHashMap<Long, HashMap<Character, FontGlyph>>();
		texts = new CopyOnWriteArrayList<Text>();
		shader = new GUIShader();
		buffer = new float[2][];
		changed = true;
	}

	public static void generateFont(String file, float fontSize, String name, boolean italic, boolean bold) {
		long font = FontAtlas.addFont(file, fontSize, italic, bold);
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

	private static HashMap<Character, FontGlyph> sortFontGlyphs(FontGlyph[] glyphs) {
		HashMap<Character, FontGlyph> t = new HashMap<Character, FontGlyph>();
		for (FontGlyph g : glyphs) {
			t.put(g.codepoint, g);
		}
		return t;
	}

	private static void udpateFontGlyphs() {
		for (long font : glyphs.keySet()) {
			glyphs.put(font, sortFontGlyphs(Font.getAllGlyphs(font)));
		}
	}

	public static boolean hasChanged() {
		return changed;
	}

	public static long getFont(String name) {
		if (fonts.containsKey(name)) {
			return fonts.get(name);
		} else {
			return -1;
		}
	}

	public static void addText(Text text) {
//		synchronized (texts) {
		texts.add(text);
//		}
		changed = true;
	}

	public static void addParagraph(Paragraph p) {
		for (Text t : p.getTexts()) {
			addText(t);
		}
	}

	public static void removeText(Text text) {
//		synchronized (texts) {
		texts.remove(text);
//		}
		changed = true;
	}

	public static void removeParagraph(Paragraph p) {
		for (Text t : p.getTexts()) {
			removeText(t);
		}
	}

	public static Texture getTexture() {
		return tex;
	}

	public static float[][] getBuffer() {
		if (!changed && buffer != null) {
			return buffer;
		}
		changed = false;
		buffer[0] = genTexAndVerBuffer();
//		buffer[0] = genVertexBuffer();
		buffer[1] = genTextureBuffer();
		return buffer;
	}

	public static FontGlyph getGlyph(long font, char c) {
		return glyphs.get(font).get(c);
//		return Font.findChar(font, c);
	}

	private static float[] genTexAndVerBuffer() {
		int size = 0;
//		synchronized (texts) {
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			size += t.getText().toCharArray().length * 16;
		}
//		}
		float[] textureCoords = new float[size];
		int pointer = 0;
//		synchronized (texts) {
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			for (char c : t.getText().toCharArray()) {
				System.out.println(pointer + 8);
				FontGlyph glyph = getGlyph(t.getFont(), c);// glyphs[c];
				textureCoords[pointer] = 0;
				textureCoords[pointer + 1] = 0;
				textureCoords[pointer + 2] = 0;
				textureCoords[pointer + 3] = 1;
				textureCoords[pointer + 4] = 1;
				textureCoords[pointer + 5] = 0;
				textureCoords[pointer + 6] = 1;
				textureCoords[pointer + 7] = 1;
				textureCoords[pointer + 8] = glyph.u0;
				textureCoords[pointer + 9] = glyph.v1;
				textureCoords[pointer + 10] = glyph.u0;
				textureCoords[pointer + 11] = glyph.v0;
				textureCoords[pointer + 12] = glyph.u1;
				textureCoords[pointer + 13] = glyph.v1;
				textureCoords[pointer + 14] = glyph.u1;
				textureCoords[pointer + 15] = glyph.v0;
				System.out.println("GLYPH: " + glyph.codepoint);
//					for(int i = 0; i < 8; i++) {
//						System.out.println(textureCoords[pointer + i]);
//					}
				pointer += 16;

			}
		}
//		}
		return textureCoords;
	}
	
	private static float[] genTextureBuffer() {
		int size = 0;
//		synchronized (texts) {
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			size += t.getText().toCharArray().length * 8;
		}
//		}
		float[] textureCoords = new float[size];
		int pointer = 0;
//		synchronized (texts) {
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			for (char c : t.getText().toCharArray()) {
				System.out.println(pointer + 8);
				FontGlyph glyph = getGlyph(t.getFont(), c);// glyphs[c];
				textureCoords[pointer] = glyph.u0;
				textureCoords[pointer + 1] = glyph.v1;
				textureCoords[pointer + 2] = glyph.u0;
				textureCoords[pointer + 3] = glyph.v0;
				textureCoords[pointer + 4] = glyph.u1;
				textureCoords[pointer + 5] = glyph.v1;
				textureCoords[pointer + 6] = glyph.u1;
				textureCoords[pointer + 7] = glyph.v0;
				System.out.println("GLYPH: " + glyph.codepoint);
//					for(int i = 0; i < 8; i++) {
//						System.out.println(textureCoords[pointer + i]);
//					}
				pointer += 8;

			}
		}
//		}
		return textureCoords;
	}

	private static float[] genVertexBuffer() {
		int size = 0;
//		synchronized (texts) {
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			size += t.getText().toCharArray().length * 8;
		}
//		}
		float[] verticies = new float[size];
		int pointer = 0;
//		synchronized (texts) {
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			for (int i = 0; i < t.getText().toCharArray().length; i++) {
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
//		}
		return verticies;
	}
	
	private static boolean updateProjectionMatrix() {
		if (projection == null) {
			projection = Calc.getProjectionMatrix();
			return true;
		} else {
			if (projection == Calc.getProjectionMatrix()) {
				return false;
			} else {
				projection = Calc.getProjectionMatrix();
				return true;
			}
		}
	}

	private static void processProjectionMatrix() {
		boolean load = updateProjectionMatrix();
		if (load) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			float[] m = new float[16];
			projection.get(m);
			GL11.glLoadMatrixf(m);
//			shader.loadProjectionMatrix(projection);
		}
	}
//
//	private static float[] getCalculatedVertexBuffer() {
//		int size = 0;
//		for (Text t : texts) {
//			if (!t.isVisible()) {
//				continue;
//			}
//			size += t.getText().toCharArray().length * 8;
//		}
//		
//		float[] verticies = new float[size];
//		int pointer = 0;
//		
//		Vector2f windowSize = Window.window.getWindowSize();
//		updateProjectionMatrix();
//		float[] quad = new float[] {0, 0, 0, 1, 1, 0, 1, 1};
//		for(Text t : texts) {
//			if (!t.isVisible()) {
//				continue;
//			}
//			
//			Vector2f wc = t.getOpenGLPos();
//			float x = wc.x();
//			float y = wc.y();
//			for (char c : t.getText().toCharArray()) {
//				FontGlyph glyph = getGlyph(t.getFont(), c);
//				
//				float width = glyph.x1 - glyph.x0;
//				float height = glyph.y1 - glyph.y0;
//
//				Vector2f quadScale = WorldPosition
//						.toOpenGLCoords(new Vector2f(width + (windowSize.x() / 2f), (windowSize.y() / 2f) - height));
//
//				Matrix4f transformation = Calc.getTransformationMatrix(new Vector2f(x, y), quadScale, 0);
//
//				Matrix4f matrix = new Matrix4f(Calc.getProjectionMatrix());
//				System.out.println(matrix);
//				matrix = matrix.mul(transformation);
//				Vector4f v1 = new Vector4f(quad[0], quad[1], 0f, 1f);
//				v1 = v1.mul(matrix);
//				Vector4f v2 = new Vector4f(quad[2], quad[3], 0f, 1f);
//				v2 = v2.mul(matrix);
//				Vector4f v3 = new Vector4f(quad[4], quad[5], 0f, 1f);
//				v3 = v3.mul(matrix);
//				Vector4f v4 = new Vector4f(quad[6], quad[7], 0f, 1f);
//				v4 = v4.mul(matrix);
//								
//				verticies[pointer] = v1.x;
//				verticies[pointer + 1] = v1.y;
//				verticies[pointer + 2] = v2.x;
//				verticies[pointer + 3] = v2.y;
//				verticies[pointer + 4] = v3.x;
//				verticies[pointer + 5] = v3.y;
//				verticies[pointer + 6] = v4.x;
//				verticies[pointer + 7] = v4.y;
//				pointer += 8;
//				
//				x += glyph.advanceX / (windowSize.x / 2f);
//			}
//			
//		}
//		
//		return verticies;
//	}

	public static void render() {
		Vector2f windowSize = Window.window.getWindowSize();
		if (tex == null) {
			throw new IllegalStateException("No Font initialized");
		}
		if (tex.getTextureID() == -1) {
			return;
		}
//		shader.start();
		processProjectionMatrix();
		MasterRenderer.masterRenderer.bindTexture(tex.getTextureID());
//		synchronized (texts) {
		int offset = 0;
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			if (t.getColor().x != 1 || t.getColor().y != 1 || t.getColor().z != 1 || t.getColor().w != 1) {
//				shader.loadColor(t.getColor());
			}
			Vector2f wc = t.getOpenGLPos();
			float x = wc.x();
			float y = wc.y();
			for (char c : t.getText().toCharArray()) {
				FontGlyph glyph = getGlyph(t.getFont(), c);

				float width = glyph.x1 - glyph.x0;
				float height = glyph.y1 - glyph.y0;

				Vector2f quadScale = WorldPosition
						.toOpenGLCoords(new Vector2f(width + (windowSize.x() / 2f), (windowSize.y() / 2f) - height));

				renderGlyph(glyph, x, y, quadScale, offset * 4);
				offset += 1;
				x += glyph.advanceX / (windowSize.x / 2f);
			}
		}
//		}
//		shader.stop();
	}

	private static void renderGlyph(FontGlyph glyph, float x, float y, Vector2f quadScale, int offset) {

		Matrix4f transformation = Calc.getTransformationMatrix(new Vector2f(x, y), quadScale, 0);
		float[] m = new float[16];
		transformation.get(m);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glLoadMatrixf(m);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 8*4);
//		shader.loadTransformationMatrix(transformation);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4 + offset, 4);
	}

}
