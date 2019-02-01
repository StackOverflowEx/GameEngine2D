package gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;
import rendering.Renderer;

public class GUIRenderer extends Renderer{

	private GUIShader shader;
	private ArrayList<GUI> guis = new ArrayList<GUI>();
	
	public GUIRenderer() {
		shader = new GUIShader("src/gui/guiV.glsl", "src/gui/guiF.glsl");
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		shader.stop();
	}
	
	public void addGUI(GUI gui) {
		guis.add(gui);
	}
	
	public void cleanUP() {
		shader.cleanUP();
	}

	@Override
	protected void render() {
		shader.start();
		if(DisplayManager.isResized()) {
			shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		}
		bindModel(Loader.getQuad(), new int[] {0});
		for(GUI gui : guis) {
			if(DisplayManager.isResized()) {
				gui.loadTransformationMatricies();
			}
			bindTexture(gui.getTexture().getTopLeftCorner(), GL13.GL_TEXTURE0);
			Matrix4f[] transforms = gui.getTransformations();
			for(int i = 0; i < 4; i++) {
				shader.loadTransformationMatrix(transforms[i]);
				drawSTRIP(Loader.getQuad());
			}
			bindTexture(gui.getTexture().getTopLine(), GL13.GL_TEXTURE0);
			for(int i = 4; i < 8; i++) {
				shader.loadTransformationMatrix(transforms[i]);
				drawSTRIP(Loader.getQuad());
			}
			bindTexture(gui.getTexture().getBackground(), GL13.GL_TEXTURE0);
			shader.loadTransformationMatrix(transforms[8]);
			drawSTRIP(Loader.getQuad());
		}
		unbind(new int[] {0});
		shader.stop();
	}
}
