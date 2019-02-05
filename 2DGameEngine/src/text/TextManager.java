package text;

import java.awt.Toolkit;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;

import com.pvporbit.freetype.Face;
import com.pvporbit.freetype.FreeType;
import com.pvporbit.freetype.FreeTypeConstants;
import com.pvporbit.freetype.FreeTypeConstants.FT_Render_Mode;
import com.pvporbit.freetype.Library;

import rendering.Loader;
import tools.Mouse;

public class TextManager {

	private static HashMap<String, HashMap<java.lang.Character, Character>> chars;

	public static void init() {

		chars = new HashMap<String, HashMap<java.lang.Character, Character>>();
	}

	public static HashMap<java.lang.Character, Character> getCharacters(String fontName) {
		return chars.get(fontName);
	}

	public static void loadFont(String file) {

		if (chars == null) {
			init();
		}
		Library lib = new Library(FreeType.FT_Init_FreeType());

		String[] tmp = file.split("/");
		String name = tmp[tmp.length - 1].replace(".ttf", "");
		System.out.println("Loaded Font: " + name);
		HashMap<java.lang.Character, Character> cc = new HashMap<java.lang.Character, Character>();

		Face face = lib.newFace(file, 0);
		//pixel_size = point_size * resolution / 72 https://www.freetype.org/freetype2/docs/glyphs/glyphs-2.html#section-1
		int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		face.setPixelSizes(0, 16 * dpi / 72);
		for (char c = 0; c < 128; c++) {
			face.loadChar(c, FreeTypeConstants.FT_LOAD_DEFAULT);
			face.getGlyphSlot().renderGlyph(FT_Render_Mode.FT_RENDER_MODE_NORMAL);
			int tex = Loader.loadGlyphTexture(face);
			Vector2f bearing = new Vector2f(face.getGlyphSlot().getBitmapLeft(), face.getGlyphSlot().getBitmapTop());
			Vector2f size = new Vector2f(face.getGlyphSlot().getBitmap().getWidth(),
					face.getGlyphSlot().getBitmap().getRows());
			float[] textCoo = new float[] { 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0 };
			Character character = new Character(tex, size, bearing, face.getGlyphSlot().getAdvance().getX(),
					Loader.loadToVAO(getPositions(bearing, size), textCoo));
			cc.put(c, character);
		}
		chars.put(name, cc);
		face.delete();
		lib.delete();
	}

	// Move with transformation +x
	// Scale with transformation
	// Move with transformation y
	private static float[] getPositions(Vector2f bearing, Vector2f size) {
		float xpos = bearing.x;
		float ypos = -(size.y - bearing.y);

		float w = size.x;
		float h = size.y;

		float[] vertices = new float[] { xpos, ypos + h, xpos, ypos, xpos + w, ypos, xpos, ypos + h, xpos + w, ypos,
				xpos + w, ypos + h };
		
		for(int i = 0; i < vertices.length; i += 2) {
//			Vector2f transform = new Vector2f(vertices[i], vertices[i+1]);
//			int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
//			float gridcoord = (transform.x) / ();
		}

		return vertices;
	}

}
