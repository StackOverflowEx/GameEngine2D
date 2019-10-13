package de.Luca.Calculation;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import de.Luca.Window.Window;

public class Calc {
	
	//Eine statische Klasse zur Berechnung der Matrizen
	
	public static Matrix4f projectionMatrix;
	public static Matrix4f ZoomProjectionMatrix;
	public static Matrix4f viewMatrix;
	private static float zoom = 1.0f;;
	
	public static void setZoom(float zoom) {
		Calc.zoom = zoom;
		ZoomProjectionMatrix = null;
	}
	
	public static float getZoom() {
		return zoom;
	}
	
	
	//Berechnung der Projectionmatrix (https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/orthographic-projection-matrix)
	public static void calcProjectionMatrix() {
		

		Vector2f windowSize = Window.getWindowSize();
		
		float aspectRatio = windowSize.x / windowSize.y;
		float halfWidth = 1.0f;
		float halfHeight = halfWidth / aspectRatio;
		
		float left = -halfWidth;
		float right = halfWidth;
		float bottom = -halfHeight;
		float top = halfHeight;
		float far = -1f;
		float near = 1f;

		Matrix4f matrix = new Matrix4f();

		matrix.identity();

		matrix._m00(2f / (right - left));
		matrix._m11(2f / (top - bottom));
		matrix._m22(-2f / (far - near));
		matrix._m32((far + near) / (far - near));
		matrix._m30((right + left) / (right - left));
		matrix._m31((top + bottom) / (top - bottom));
		
		
		projectionMatrix = matrix;
	}
	
	//Berechnung der Projectionmatrix mit Zoom
	public static void calcZoomProjectionMatrix() {
		

		Vector2f windowSize = Window.getWindowSize();

		float aspectRatio = windowSize.x / windowSize.y;
		float halfWidth = 1.0f;
		float halfHeight = halfWidth / aspectRatio;
		
		float left = -halfWidth * zoom;
		float right = halfWidth * zoom;
		float bottom = -halfHeight * zoom;
		float top = halfHeight * zoom;
		float far = -1f;
		float near = 1f;

		Matrix4f matrix = new Matrix4f();

		matrix.identity();

		matrix._m00(2f / (right - left));
		matrix._m11(2f / (top - bottom));
		matrix._m22(-2f / (far - near));
		matrix._m32((far + near) / (far - near));
		matrix._m30((right + left) / (right - left));
		matrix._m31((top + bottom) / (top - bottom));
		
		
		ZoomProjectionMatrix = matrix;
	}
	
	public static Matrix4f getViewMatrix() {
		if(viewMatrix == null) {
			calcViewMatrix();
		}
		return viewMatrix;
	}
	
	//Berechnung der Viewmatrix (Kamerasimulation)
	public static void calcViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.identity();
		viewMatrix = viewMatrix.rotate((float) Math.toRadians(Camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-Camera.getPosition().x, -Camera.getPosition().y, 1);
		viewMatrix = viewMatrix.translate(negativeCameraPos, viewMatrix);
		Calc.viewMatrix = viewMatrix;
	}
	
	//Berechnung der Transformationmatrix für ein Block/Entity/GUI-Element/Charakter/...
	public static Matrix4f getTransformationMatrix(Vector2f pos, Vector2f scale, float rotationZ) {
		Matrix4f matrix = new Matrix4f();
		matrix = matrix.identity();
		matrix = matrix.translate(new Vector3f(pos.x, pos.y, 0), matrix);
		matrix = matrix.scale(new Vector3f(scale.x, scale.y, 1f), matrix);
		matrix = matrix.rotate((float) Math.toRadians(rotationZ), new Vector3f(0, 0, 1), matrix);
		return matrix;
	}
	
	public static Matrix4f getProjectionMatrix() {
		if(projectionMatrix == null) {
			calcProjectionMatrix();
		}
		return projectionMatrix;
	}
	
	public static Matrix4f getZommProjectionMatrix() {
		if(ZoomProjectionMatrix == null) {
			calcZoomProjectionMatrix();
		}
		return ZoomProjectionMatrix;
	}

}
