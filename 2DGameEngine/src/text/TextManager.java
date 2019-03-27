package text;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import de.t0b1.freetype_wrapper.classes.FontAtlas;
import de.t0b1.freetype_wrapper.classes.FontGlyph;
import de.t0b1.freetype_wrapper.classes.Texture;
import gui.GUIElement;
import gui.GUITexture;
import gui.GUIType;
import rendering.Loader;

public class TextManager {
	
	public static ArrayList<FontType> fonts = new ArrayList<FontType>(); 
	
	public static GUIElement loadFont(String name, String file, boolean bold, boolean italic) {
				
		long font = FontAtlas.addFont(file, 100.0f, italic, bold);
		FontType ft = new FontType(font, name, bold, italic, null);
		fonts.add(ft);
		build();
		Texture rgbTex = FontAtlas.getTexDataAsRGBA32();
		ft.setTexture(rgbTex);
				
		GUITexture gt = new GUITexture(null, null, Loader.loadTexture(rgbTex));
		GUIElement ge = new GUIElement(new Vector2f(0, 0), new Vector2f(1, 1), gt, GUIType.BACKGROUND);
		
		
		return ge;
				
	}
	
	public static void build() {
		FontAtlas.build(0);
		for(FontType f : fonts) {
			f.buildLookUpTable();
		}
	}

}
