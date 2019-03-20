package text;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import de.t0b1.freetype_wrapper.classes.FontAtlas;
import de.t0b1.freetype_wrapper.classes.Texture;
import gui.GUIElement;
import gui.GUITexture;
import gui.GUIType;
import rendering.Loader;

public class TextManager {
	
	public static ArrayList<FontType> fonts = new ArrayList<FontType>(); 
	
	public static GUIElement loadFont(String name, String file, boolean bold, boolean italic) {
				
		long font = FontAtlas.addFont(file, 100.0f, italic, bold);
		fonts.add(new FontType(font, name, bold, italic));
		build();
		
		FontType type = fonts.get(0);
		Text text = new Text("TestText", type);
		text.generateModel();
		
		Texture rgbTex = FontAtlas.getTexDataAsRGBA32();
		
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
