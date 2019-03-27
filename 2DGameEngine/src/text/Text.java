package text;

import org.lwjgl.util.vector.Vector2f;

import de.t0b1.freetype_wrapper.classes.FontGlyph;
import models.RawModel;
import rendering.DisplayManager;
import rendering.Loader;
import tools.Mouse;

public class Text {
	
	private String text;
	private RawModel model;
	private FontType fontType;
	
	public Text(String text, FontType fontType) {
		this.text = text;
		this.fontType = fontType;
		model = generateModel();
	}
	
	public RawModel getModel() {
		return model;
	}
	
	public FontType getFontType() {
		return fontType;
	}
	
	private RawModel generateModel() {
		int glyphCount = text.length();
		float pos[][] = new float[glyphCount][glyphCount*6*2];
		float uv[][] = new float[glyphCount][glyphCount*6*2];
		
		float add = 0;
		int i = 0;
		Vector2f display = DisplayManager.getWindowSize();
		for(byte b : text.getBytes()) {
			char c = (char) b;
			FontGlyph fb = fontType.getGlyph(c);
			//links oben, links unten, rechts oben
			//rechts oben, links unten, rechts unten
			float ad = add + fb.advanceX;
			float[] verticies = new float[] {fb.x0 + ad, fb.y0, fb.x0 + ad, fb.y1, fb.x1 + ad, fb.y0, fb.x1 + ad, fb.y0, fb.x0 + ad, fb.y1, fb.x1 + ad, fb.y1};
			float[] textures = new float[] {fb.u0, fb.v0, fb.u0, fb.v1, fb.u1, fb.v0, fb.u1, fb.v0, fb.u0, fb.v1, fb.u1, fb.v1};
			
			for(int n = 0; n < verticies.length; n+=2) {
				Vector2f posconv = Mouse.getWorldPosWithoutCamera(new Vector2f(verticies[i] + display.x/2f, verticies[i+1] + display.y /2f));
				Vector2f uvconv = Mouse.getWorldPosWithoutCamera(new Vector2f(textures[i] + display.x/2f, textures[i+1] + display.y / 2f));
				
				verticies[i] = posconv.x;
				verticies[i+1] = posconv.y;
				textures[i] = uvconv.x;
				textures[i+1] = uvconv.y;
				
				System.out.println(verticies[i]);
				
			}
			
			pos[i] = verticies;
			uv[i] = textures;
			
			i++;
		}
		
		float[] posfinal = new float[pos.length*glyphCount*12];
		float[] texfinal = new float[pos.length*glyphCount*12];

		int ab = 0;
		for(int x = 0; x < pos.length; x++) {
			for(int y = 0; y < pos[x].length; y++) {
				posfinal[ab] = pos[x][y];
				texfinal[ab] = uv[x][y];
				ab++;
			}
		}
		
		return Loader.loadToVAO(posfinal, texfinal);
		
	}

}
