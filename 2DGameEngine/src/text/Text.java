package text;

import de.t0b1.freetype_wrapper.classes.FontGlyph;
import models.RawModel;

public class Text {
	
	private String text;
	private RawModel model;
	private FontType fontType;
	
	public Text(String text, FontType fontType) {
		this.text = text;
		this.fontType = fontType;
		//model = Loader.loadToVAO(new float[]{});
	}
	
	public RawModel generateModel() {
		int glyphCount = text.length();
		float pos[] = new float[glyphCount*6*2];
		float uv[] = new float[glyphCount*6*2];
		
		float curX = 0;
		for(int i = 0; i < text.length(); i++) {
			FontGlyph glyph = fontType.getGlyph((char)text.codePointAt(i));
			
			int idx = i * 6 * 2;
			
			
		}
		return null;
	}

}
