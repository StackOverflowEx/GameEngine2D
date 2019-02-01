package block;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL13;

import models.RenderObject;
import models.TexturedModel;
import rendering.DisplayManager;
import rendering.Loader;
import rendering.MasterRenderer;
import rendering.Renderer;
import tools.Camera;

public class BlockRenderer extends Renderer{
	
	private BlockShader shader;
	private HashMap<TexturedModel, ArrayList<RenderObject>> objects;
	
	public BlockRenderer() {
		shader = new BlockShader("src/block/blockV.glsl", "src/block/blockF.glsl");
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		shader.stop();
		objects = new HashMap<TexturedModel, ArrayList<RenderObject>>();
	}
	
	public void addRenderBlock(RenderObject renderObj) {
		if(objects.containsKey(renderObj.getModel())) {
			objects.get(renderObj.getModel()).add(renderObj);
		}else {
			ArrayList<RenderObject> tmp = new ArrayList<RenderObject>();
			tmp.add(renderObj);
			objects.put(renderObj.getModel(), tmp);
		}
	}

	public void render() {
		shader.start();
		shader.loadViewMatrix(Camera.getViewMatrix());
		if(DisplayManager.isResized()) {
			shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		}
		bindModel(Loader.getQuad(), new int[] {0});
		for(TexturedModel tm : objects.keySet()) {
			bindTexture(tm.getTexture(), GL13.GL_TEXTURE0);
			for(RenderObject ro : objects.get(tm)) {
				Block b = (Block) ro;
				if(b.isFakeBlock()) {
					shader.loadTransformationMatrix(ro.getFakeTransformationMatrix());
				}else {
					shader.loadTransformationMatrix(ro.getTransformationMatrix());
				}
				drawSTRIP(Loader.getQuad());
			}
		}
		unbind(new int[] {0});
		shader.stop();
	}

	public void cleanUP() {
		shader.cleanUP();
	}
	

}
