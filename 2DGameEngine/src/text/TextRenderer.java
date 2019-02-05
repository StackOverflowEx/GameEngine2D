package text;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import rendering.DisplayManager;
import rendering.MasterRenderer;
import rendering.Renderer;
import tools.Maths;

public class TextRenderer extends Renderer {

	private TextShader shader;
	private ArrayList<Text> texts = new ArrayList<Text>();

	public TextRenderer() {
		shader = new TextShader("src/text/textV.glsl", "src/text/textF.glsl");
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		shader.stop();
	}

	public void addText(Text t) {
		texts.add(t);
	}

	public void removeText(Text t) {
		texts.remove(t);
	}

	@Override
	protected void render() {
		MasterRenderer.enableAlpha();
		shader.start();
		if(DisplayManager.isResized()) {
			shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		}
		for(Text t : texts) {
			float x = t.getPosition().getX();
			HashMap<java.lang.Character, Character> chars = TextManager.getCharacters(t.getFontName());
			shader.loadColor(t.getColor());
			for(int i = 0; i < t.getText().length(); i++) {
				char c = t.getText().charAt(i);
				Character character = chars.get(c);				
				bindModel(character.getModel(), new int[] {0, 1});
				bindTexture(character.getTextureID(), GL20.GL_TEXTURE0);
				
				Matrix4f transformation = getTransformationMatrix(x, t);
				shader.loadTransformationMatrix(transformation);
				
				drawArrays(character.getModel());
				
				x += (character.getAdvance() >> 6) * t.getScale();
			}

		}
		unbind(new int[] {0});
		shader.stop();
		MasterRenderer.disableAlpha();
	}
	
	private Matrix4f getTransformationMatrix(float x, Text t) {
		Vector2f displaySize = DisplayManager.getWindowSize();
//		System.out.println(new Vector2f(1 / (aspectRatio * 500), 1 / (aspectRatio * 500)));
		Matrix4f ret = Maths.getTransformationMatrix(new Vector2f(x, 0), new Vector2f(1, 1));
		return ret;
	}

	@Override
	protected void cleanUP() {
		// TODO Auto-generated method stub

	}

}
