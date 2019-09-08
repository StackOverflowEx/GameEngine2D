package de.Luca.Effects;

import java.util.concurrent.CopyOnWriteArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import de.Luca.Calculation.Calc;
import de.Luca.Models.RenderModel;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Shader.EntityShader;

public class EffectManager {
	
	private static CopyOnWriteArrayList<Effect> effects;
	private static EntityShader shader;
	private static boolean firstRun;
	
	public static void init() {
		effects = new CopyOnWriteArrayList<Effect>();
		shader = new EntityShader();
		firstRun = true;
	}
	
	public static void addEffect(Effect e) {
		effects.add(e);
	}
	
	public static void removeEffect(Effect e) {
		effects.remove(e);
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
		
		for(Effect e : effects) {
			if(!e.isPlaying()) {
				removeEffect(e);
				continue;
			}
			e.update();
			RenderModel model = e.getRenderModel();
			if(model.getModel().getTexture() != null && model.getModel().getTexture().getTextureID() != -1) {
				MasterRenderer.bindTexture(model.getModel().getTexture().getTextureID());
				Matrix4f transformation = Calc.getTransformationMatrix(model.getLocation(), model.getModel().getScale(), model.getRoll());
				shader.loadTransformationMatrix(transformation);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			}
		}
		
		shader.stop();
	}
	
}
