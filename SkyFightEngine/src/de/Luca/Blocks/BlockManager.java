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
	
	//Der Blockmanager ist eine statische Klasse und verwalltet alle Blöcke
	
	//Hier sind alle Blöcke enthalten, der Key ist die x-Koordinate und der Key der zweiten ConcurrentHashMap die y-Kooridnate
	private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Block>> blocks;
	//Der Blockshader, der benötigt wird um die Blöcke darzustellen
	private static BlockShader shader;
	//Ist beim initalisieren true und nach dem ersten Renderdurchlauf false
	private static boolean firstRun;
	
	//Initalisiert den BlockManager
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
	
	//Es wird ein Block entsprechend des Aufbaus der ConcurrentHashMap hinzugefügt.
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
	
	//Gibt einen Boolean zurück, ob sich an dieser Position ein Block befindet.
	public static boolean doseBlockExist(Vector2f worldPos) {
		float x = (float) Math.ceil(worldPos.x);
		float y = (float) Math.ceil(worldPos.y);
		if(blocks.containsKey((int)x)) {
			return blocks.get((int)x).containsKey((int)y);
		}
		return false;
	}
	
	//Liefert eine ArrayList mit Blöcken, mit denen der Spieler kollidiert.
	public static ArrayList<Block> isCollidingWithBlock(Vector4f hitBox) {
		float blockX = (float) Math.floor(hitBox.x());
		float blockY = (float) Math.floor(hitBox.y());
		float block1X = (float) Math.floor(hitBox.z());
		float block1Y = (float) Math.floor(hitBox.w());
				
		ArrayList<Block> ret = new ArrayList<Block>();
			
		//Es wird für jede x-Koordinate getestet, die die Hitbox umschließt.
		for(float i = blockX; i <= block1X; i++) {
			if(!blocks.containsKey((int)i)) {
				continue;
			}
			//Es wird für jede y-Koordinate getestet, die die Hitbox umschließt.
			for(float n = blockY; n <= block1Y; n++) {
				if(!blocks.get((int)i).containsKey((int)n)) {
					continue;
				}
				ret.add(blocks.get((int)i).get((int)n));
			}
		}
		return ret;
	}
	
	//Gibt den Block an der Position zurück
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
	
	
	//Zeichnet alle Blöcke
	public static void render() {
		
		//Der Shader wird gestartet und die Matrizen geladen, wenn diese sich geändert haben oder die Blöcke das erste mal gerendert werden.
		shader.start();
		if(MasterRenderer.hasZoomProjectionChanged() || firstRun) {
			shader.loadProjectionMatrix(MasterRenderer.getZoomProjection());
		}
		if(MasterRenderer.hasViewChanged() || firstRun) {
			shader.loadViewMatrix(MasterRenderer.getView());
			firstRun = false;
		}
		
		for(int i : blocks.keySet()) {
			try {
				for(Block b : blocks.get(i).values()) {
					RenderModel model = b.getBlockData().getBlockModel();
					if(model.getModel().getTexture().getTextureID() != -1) {
						//Rendercode, für den jeweilgien Block
						//Textur wird geladen
						MasterRenderer.bindTexture(model.getModel().getTexture().getTextureID());
						//Transformation wird berechnet und geladen.
						Matrix4f transformation = Calc.getTransformationMatrix(b.getOpenGLPos(), model.getModel().getScale(), model.getRoll());
						shader.loadTransformationMatrix(transformation);
						//Der Alpha-Wert wird berechnet und geladen
						float per = 1f - b.getBreakPercentage();
						if(per < 0) {
							per = 0;
						}
						shader.loadAlpha(per);
						//Zeichnen des Blocks
						GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
					}
				}
			}catch (NullPointerException e) {
				continue;
			}
		}
		
		//Shader wird beendet
		shader.stop();
		
	}

}
