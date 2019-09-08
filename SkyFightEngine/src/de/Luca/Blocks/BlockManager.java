package de.Luca.Blocks;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.Calc;
import de.Luca.Models.RenderModel;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Shader.BlockShader;


public class BlockManager {
	
	private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Block>> blocks;
	private static BlockShader shader;
	private static boolean firstRun;
	
	public static void init() {
		if(blocks == null) {
			blocks = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Block>>();
			shader = new BlockShader();
			firstRun = true;
		}
	}
	
	public static void removeAllBlocks() {
		blocks.clear();
	}
	
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Block>> getBlocks(){
		return blocks;
	}
	
	public static void addBlock(Block b) {
		if(!blocks.containsKey((int)b.getWorldPos().x)) {
			ConcurrentHashMap<Integer, Block> tmp = new ConcurrentHashMap<Integer, Block>();
			blocks.put((int)b.getWorldPos().x, tmp);
		}
		blocks.get((int)b.getWorldPos().x).put((int)b.getWorldPos().y, b);
	}
	
	public static void removeBlock(Block b) {
		if(!blocks.containsKey((int)b.getWorldPos().x)) {
			return;
		}
		blocks.get((int)b.getWorldPos().x).remove((int)b.getWorldPos().y);
	}
	
	public static boolean doseBlockExist(Vector2f worldPos) {
		float x = (float) Math.ceil(worldPos.x);
		float y = (float) Math.ceil(worldPos.y);
		if(blocks.containsKey((int)x)) {
			return blocks.get((int)x).containsKey((int)y);
		}
		return false;
	}
	
	public static ArrayList<Block> isCollidingWithBlock(Vector4f hitBox) {
		float blockX = (float) Math.floor(hitBox.x());
		float blockY = (float) Math.floor(hitBox.y());
		float block1X = (float) Math.floor(hitBox.z());
		float block1Y = (float) Math.floor(hitBox.w());
				
		ArrayList<Block> ret = new ArrayList<Block>();
						 
		for(float i = blockX; i <= block1X; i++) {
			if(!blocks.containsKey((int)i)) {
				continue;
			}
			for(float n = blockY; n <= block1Y; n++) {
				if(!blocks.get((int)i).containsKey((int)n)) {
					continue;
				}
				ret.add(blocks.get((int)i).get((int)n));
			}
		}
		return ret;
	}
	
	public static Block getBlock(Vector2f pos) {
		float x = (float) Math.floor(pos.x());
		float y = (float) Math.floor(pos.y());
						
		if(blocks.containsKey((int)x)) {
			if(blocks.get((int)x).containsKey((int)y)) {
				return blocks.get((int)x).get((int)y);
			}
		}
		
		return null;
	}
	
	public static void render() {
		
		
		shader.start();
		if(MasterRenderer.hasProjectionChanged() || firstRun) {
			shader.loadProjectionMatrix(MasterRenderer.getProjection());
		}
		if(MasterRenderer.hasViewChanged() || firstRun) {
			shader.loadViewMatrix(MasterRenderer.getView());
			firstRun = false;
		}
		
		for(int i : blocks.keySet()) {
			for(Block b : blocks.get(i).values()) {
				RenderModel model = b.getBlockData().getBlockModel();
				if(model.getModel().getTexture().getTextureID() != -1) {
					MasterRenderer.bindTexture(model.getModel().getTexture().getTextureID());
					Matrix4f transformation = Calc.getTransformationMatrix(b.getOpenGLPos(), model.getModel().getScale(), model.getRoll());
					shader.loadTransformationMatrix(transformation);
					GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 8, 4);
				}
			}
		}
		
		shader.stop();
		
	}

}
