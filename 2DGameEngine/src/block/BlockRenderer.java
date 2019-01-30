package block;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL13;

import models.RawModel;
import models.RenderObject;
import models.TexturedModel;
import rendering.Camera;
import rendering.MasterRenderer;
import rendering.Renderer;

public class BlockRenderer extends Renderer{
	
	private BlockShader shader;
	private HashMap<TexturedModel, ArrayList<RenderObject>> objects;
	private RawModel quad;
	
	public BlockRenderer(RawModel quad) {
		shader = new BlockShader("src/block/blockV.glsl", "src/block/blockF.glsl");
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
		shader.stop();
		objects = new HashMap<TexturedModel, ArrayList<RenderObject>>();
		
		this.quad = quad;
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
		bindModel(quad, new int[] {0});
		for(TexturedModel tm : objects.keySet()) {
			bindTexture(tm.getTexture(), GL13.GL_TEXTURE0);
			for(RenderObject ro : objects.get(tm)) {
				Block b = (Block) ro;
				if(b.isFakeBlock()) {
					shader.loadTransformationMatrix(ro.getFakeTransformationMatrix());
				}else {
					shader.loadTransformationMatrix(ro.getTransformationMatrix());
				}
				drawSTRIP(quad);
			}
		}
		unbind(new int[] {0});
		shader.stop();
	}

	public void cleanUP() {
		shader.cleanUP();
	}
	

}
