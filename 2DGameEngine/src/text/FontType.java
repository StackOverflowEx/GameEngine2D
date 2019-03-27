package text;

import java.util.HashMap;

import de.t0b1.freetype_wrapper.classes.Font;
import de.t0b1.freetype_wrapper.classes.FontGlyph;
import models.Texture;
import rendering.Loader;

public class FontType {
	
	private long id;
	private String name;
	private boolean bold;
	private boolean italic;
	private Texture tex;
	private FontGlyph[] glyphs;
	private HashMap<Character, Integer> lookUp = new HashMap<Character, Integer>();
	
	public FontType(long id, String name, boolean bold, boolean italic, de.t0b1.freetype_wrapper.classes.Texture tex) {
		super();
		this.id = id;
		this.name = name;
		this.bold = bold;
		this.italic = italic;
		if(tex != null) {
			this.tex = Loader.loadTexture(tex);
		}
	}

	public Texture getTexture() {
		return tex;
	}
	
	public void setTexture(de.t0b1.freetype_wrapper.classes.Texture tex) {
		this.tex = Loader.loadTexture(tex);
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isItalic() {
		return italic;
	}
	
	public void buildLookUpTable() {
		this.glyphs = Font.getAllGlyphs(id);
		lookUp.clear();
		for(int i = 0; i < glyphs.length; i++) {
			FontGlyph fg = glyphs[i];
			lookUp.put(fg.codepoint, i);
		}
	}
	
	FontGlyph getGlyphNoFallback(char c) {
		if(lookUp.containsKey(c))
			return glyphs[lookUp.get(c)];
		return null;
	}
	
	FontGlyph getGlyph(char c) {
		FontGlyph glyph = getGlyphNoFallback(c);
		if(glyph != null)
			return glyph;
		return getGlyphNoFallback('?');
	}
	
	
}
