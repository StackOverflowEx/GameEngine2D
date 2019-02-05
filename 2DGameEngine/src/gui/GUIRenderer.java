package gui;

import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;
import rendering.Renderer;

public class GUIRenderer extends Renderer{

	private GUIShader shader;
	
	public GUIRenderer() {
		shader = new GUIShader("src/gui/guiV.glsl", "src/gui/guiF.glsl");
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		shader.stop();
	}
	
	public void cleanUP() {
		shader.cleanUP();
	}

	@Override
	protected void render() {
		MasterRenderer.enableAlpha();
		shader.start();
		if(DisplayManager.isResized()) {
			shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		}
		bindModel(Loader.getQuad(), new int[] {0});
		for(GUIElement gui : GUIHandler.getAllGuiElements(true)) {
			if(DisplayManager.isResized()) {
				gui.loadTransformationMatricies();
			}
			Matrix4f[] transforms = gui.getTransformations();
			if(transforms[1] != null) {
				//corners
				bindTexture(gui.getTexture().getTopLeftCorner(), GL13.GL_TEXTURE0);
				for(int i = 1; i < 5; i++) {
					shader.loadTransformationMatrix(transforms[i]);
					drawSTRIP(Loader.getQuad());
				}
				//borders
				bindTexture(gui.getTexture().getTopLine(), GL13.GL_TEXTURE0);
				for(int i = 5; i < 9; i++) {
					shader.loadTransformationMatrix(transforms[i]);
					drawSTRIP(Loader.getQuad());
				}
			}
			bindTexture(gui.getTexture().getBackground(), GL13.GL_TEXTURE0);
			shader.loadTransformationMatrix(transforms[0]);
			drawSTRIP(Loader.getQuad());
		}
		unbind(new int[] {0});
		shader.stop();
		MasterRenderer.disableAlpha();
	}
}
