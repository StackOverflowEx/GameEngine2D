package text;

import org.lwjgl.util.vector.Vector2f;

import de.t0b1.freetype_wrapper.classes.FontAtlas;
import de.t0b1.freetype_wrapper.classes.Texture;
import gui.GUIElement;
import gui.GUITexture;
import gui.GUIType;
import rendering.Loader;

public class TextManager {
	
	public static GUIElement loadFont(String file) {
		
		System.load("F:\\Repositorys\\GameEngine2D\\2DGameEngine\\lib\\freetype.dll");
		System.out.println("asdfsadf");
		
		System.out.println(FontAtlas.hasUpdated());
		long font = FontAtlas.addFont(file, 100.0f, false, false);
		FontAtlas.build(0);
		
		Texture rgbTex = FontAtlas.getTexDataAsRGBA32();
		
		GUITexture gt = new GUITexture(null, null, Loader.loadTexture(rgbTex));
		GUIElement ge = new GUIElement(new Vector2f(0, 0), new Vector2f(1, 1), gt, GUIType.BACKGROUND);
		
		return ge;
				
	}

}
