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
import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Shader.TextShader;
import de.Luca.Utils.WorldPosition;
import de.Luca.Window.Window;
import de.t0b1.freetype_wrapper.classes.Font;
import de.t0b1.freetype_wrapper.classes.FontAtlas;
import de.t0b1.freetype_wrapper.classes.FontGlyph;

public class TextManager {

	//eine statische Klasse, die den Text verwaltet
	

	private static ConcurrentHashMap<String, Long> fonts;
	private static CopyOnWriteArrayList<Text> texts;
	private static TextShader shader;
	private static Texture tex;
	private static ConcurrentHashMap<Long, HashMap<Character, FontGlyph>> glyphs;
	private static float[][] buffer;
	private static float bufferTextAmount;
	private static boolean changed;
	private static Matrix4f projection;
	
	public static void init() {
		if (fonts != null) {
			return;
		}
		fonts = new ConcurrentHashMap<String, Long>();
		glyphs = new ConcurrentHashMap<Long, HashMap<Character, FontGlyph>>();
		texts = new CopyOnWriteArrayList<Text>();
		shader = new TextShader();
		buffer = new float[2][];
		changed = true;
		bufferTextAmount = 0;
	}

	//lädt eine Schriftart mit Hilfe des FreeTypeWrappers
	public static void generateFont(String file, float fontSize, String name, boolean italic, boolean bold) {
		if(fonts.containsKey(name))
			return;
		long font = FontAtlas.addFont(file, fontSize, italic, bold);
		FontAtlas.hasUpdated();
		FontAtlas.build(0);
		//lädt die Textur in einen ByteBuffer
		ByteBuffer buffer = BufferUtils.createByteBuffer(FontAtlas.getTexDataAsRGBA32().data.length);
		buffer.put(FontAtlas.getTexDataAsRGBA32().data);
		buffer.flip();
		//löscht alle alten Text-Textureatlases
		Loader.deleteTextures("text");
		//erstellt einen Texturatlas mit allen Schriftareten
		tex = new Texture(buffer, FontAtlas.getTexDataAsRGBA32().width, FontAtlas.getTexDataAsRGBA32().height, "text", "text");
		MasterRenderer.queueTexture(tex);

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

	//Aktualisiert die einzelnen Glyphs der Fonts
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
		texts.add(text);
		changed = true;
	}
	
	public static void setChanged() {
		changed = true;
	}

	public static void addParagraph(Paragraph p) {
		for (Text t : p.getTexts()) {
			addText(t);
		}
	}

	public static void removeText(Text text) {
		texts.remove(text);
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

	//gibt den Vertex und den Texturbuffer für den Text zurück
	public static float[][] getBuffer() {
		if (!changed && buffer != null && bufferTextAmount == texts.size()) {
			return buffer;
		}
		changed = false;
		bufferTextAmount = texts.size();
		buffer[0] = genVertexBuffer();
		buffer[1] = genTextureBuffer();
		return buffer;
	}
	
	public static void cleanUP() {
		shader.cleanUP();
	}

	public static FontGlyph getGlyph(long font, char c) {
		return glyphs.get(font).get(c);
	}

	//erstellt den TexturBuffer für den Text
	private static float[] genTextureBuffer() {
		int size = 0;
		//Berechnet die Größe des Arrays
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			size += t.getText().toCharArray().length * 8;
		}
		//erstellt ein Array
		float[] textureCoords = new float[size];
		int pointer = 0;
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			//für jeden Text (Zeile), der sichtbar ist, werden die Texturkoordinaten berechnet
			for (char c : t.getText().toCharArray()) {
				if(pointer == size) {
					break;
				}
				FontGlyph glyph = getGlyph(t.getFont(), c);
				textureCoords[pointer] = glyph.u0;
				textureCoords[pointer + 1] = glyph.v1;
				textureCoords[pointer + 2] = glyph.u0;
				textureCoords[pointer + 3] = glyph.v0;
				textureCoords[pointer + 4] = glyph.u1;
				textureCoords[pointer + 5] = glyph.v1;
				textureCoords[pointer + 6] = glyph.u1;
				textureCoords[pointer + 7] = glyph.v0;
				pointer += 8;

			}
		}
		return textureCoords;
	}

	//Berechnet den Vertexbuffer für den Text
	private static float[] genVertexBuffer() {
		int size = 0;
		//Berechnet die Größe des Arrays
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			size += t.getText().toCharArray().length * 8;
		}
		//erstellt ein Array
		float[] verticies = new float[size];
		int pointer = 0;
		//für jeden Text (Zeile), der sichtbar ist, werden die Vertexkoordinaten berechnet
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			for (int i = 0; i < t.getText().toCharArray().length; i++) {
				if(pointer == size) {
					break;
				}
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

	private static boolean processProjectionMatrix() {
		boolean load = updateProjectionMatrix();
		if (load) {
			shader.loadProjectionMatrix(projection);
		}
		return load;
	}

	//rendert den Text
	public static void render() {
		Vector2f windowSize = Window.getWindowSize();
		//Es wird überprüft, ob ein Texturatlas existiert && der Buffer zur Anzahl der Zeilen passt.
		if (tex == null) {
			return;
		}
		if (tex.getTextureID() == -1) {
			return;
		}
		if(bufferTextAmount != texts.size()) {
			return;
		}
		//der Shader wird gestartet und der Texturatlas gebunden
		shader.start();
		boolean b = processProjectionMatrix();
		MasterRenderer.bindTexture(tex.getTextureID());
		int offset = 0;
		for (Text t : texts) {
			if (!t.isVisible()) {
				continue;
			}
			//haben sich die Matritzen geändert, wird die OpenGl- Position neu berechnet
			if(b) {
				t.reCalcOpenGL();
			}
			//es wird die Farbe der Schrift gesetzt
			if (t.getColor().x != 1 || t.getColor().y != 1 || t.getColor().z != 1 || t.getColor().w != 1) {
				shader.loadColor(t.getColor());
			}
			Vector2f wc = t.getOpenGLPos();
			float x = wc.x();
			//Jeder Glyph (Charakter) der Zeilen wird einzeln gerendert
			for (char c : t.getText().toCharArray()) {
				//Der Glyph wird gesucht
				FontGlyph glyph = getGlyph(t.getFont(), c);

				//Die Position und die Scale wird berechnet
				float width = glyph.x1 - glyph.x0;
				width = width * t.getScale();
				float height = glyph.y1 - glyph.y0;
				height = height * t.getScale();
				
				Vector2f yy = WorldPosition.toOpenGLCoords(new Vector2f(0, (windowSize.y() / 2f) - glyph.y1));
				float y = wc.y() - yy.y;
				
				Vector2f quadScale = WorldPosition
						.toOpenGLCoords(new Vector2f(width + (windowSize.x() / 2f), (windowSize.y() / 2f) - height));
				
				//Der Glyph wird gezeichnet
				renderGlyph(glyph, x, y, quadScale, offset * 4);
				//x und offset werden erhöt, damit der nächste Glyph an der entsprechenden Position gerendert wird
				offset += 1;
				x += (glyph.advanceX * t.getScale()) / (windowSize.x / 2f);
			}
		}
		MasterRenderer.bindTexture(0);
		shader.stop();
	}

	
	//eigentlicher Code, zum rendern eines Glyphs
	private static void renderGlyph(FontGlyph glyph, float x, float y, Vector2f quadScale, int offset) {	
		//laden der transformation
		Matrix4f transformation = Calc.getTransformationMatrix(new Vector2f(x, y), quadScale, 0);
		shader.loadTransformationMatrix(transformation);
		//Zeichnen des Glyphs
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 12 + offset, 4);

	}

}
