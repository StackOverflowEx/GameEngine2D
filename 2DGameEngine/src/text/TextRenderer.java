package text;

import rendering.MasterRenderer;
import rendering.Renderer;

public class TextRenderer extends Renderer {

	private TextShader shader;
//	private ArrayList<>

	public TextRenderer() {
		shader = new TextShader("src/text/textV.glsl", "src/text/textF.glsl");
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		shader.stop();
	}


	@Override
	protected void render() {
		MasterRenderer.enableAlpha();
		shader.start();
		
		shader.stop();
		MasterRenderer.disableAlpha();
	}

	@Override
	protected void cleanUP() {
		// TODO Auto-generated method stub

	}

}
