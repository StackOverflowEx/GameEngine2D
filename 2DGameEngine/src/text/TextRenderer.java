package text;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;

import rendering.DisplayManager;
import rendering.MasterRenderer;
import rendering.Renderer;

public class TextRenderer extends Renderer {

	private TextShader shader;
	private HashMap<FontType, ArrayList<Text>> texts = new HashMap<FontType, ArrayList<Text>>();

	public TextRenderer() {
		shader = new TextShader("src/text/textV.glsl", "src/text/textF.glsl");
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		shader.stop();
	}

	public void addText(Text t) {
		if(texts.containsKey(t.getFontType())) {
			texts.get(t.getFontType()).add(t);
		}else {
			ArrayList<Text> ts = new ArrayList<Text>();
			ts.add(t);
			texts.put(t.getFontType(), ts);
		}
	}

	@Override
	protected void render() {
		MasterRenderer.enableAlpha();
		shader.start();
		if(DisplayManager.isResized()) {
			shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		}
		for(FontType ft : texts.keySet()) {
			bindTexture(ft.getTexture(), GL20.GL_TEXTURE0);
			for(Text t : texts.get(ft)) {
				bindModel(t.getModel(), new int[] {0, 1});
				drawArrays(t.getModel());
				unbind(new int[] {0, 1});
			}
		}
		shader.stop();
		MasterRenderer.disableAlpha();
	}

	@Override
	protected void cleanUP() {
		// TODO Auto-generated method stub

	}

}
