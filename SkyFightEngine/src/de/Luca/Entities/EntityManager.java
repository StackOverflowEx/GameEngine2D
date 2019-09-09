package de.Luca.Entities;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.Calc;
import de.Luca.Models.RenderModel;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Shader.EntityShader;

public class EntityManager {
	
	private static CopyOnWriteArrayList<Entity> entities;
	private static EntityShader shader;
	private static boolean firstRun = true;
	
	public static void init() {
		entities = new CopyOnWriteArrayList<Entity>();;
		shader = new EntityShader();
	}
	
	public static void addEntity(Entity e) {
		entities.add(e);
	}
	
	public static void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	public static void render() {
		shader.start();
		if(MasterRenderer.hasZoomProjectionChanged() || firstRun) {
			shader.loadProjectionMatrix(MasterRenderer.getZoomProjection());
		}
		if(MasterRenderer.hasViewChanged() || firstRun) {
			shader.loadViewMatrix(MasterRenderer.getView());
			firstRun = false;
		}
		
		for(Entity e : entities) {
			if(!e.isVisible()) {
				continue;
			}
			e.updateAnimation();
			e.updateSound();
			for(RenderModel model : e.getModels()) {
				if(model.getModel().getTexture() != null && model.getModel().getTexture().getTextureID() != -1) {
					MasterRenderer.bindTexture(model.getModel().getTexture().getTextureID());
					Matrix4f transformation = Calc.getTransformationMatrix(model.getLocation(), model.getModel().getScale(), model.getRoll());
					shader.loadTransformationMatrix(transformation);
					GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 8, 4);
				}
			}
		}
		
		shader.stop();
	}

}
