package de.Luca.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgramm {
	
	private int vertexShader;
	private int fragmentShader;
	private int programmID;
	
	public ShaderProgramm(InputStream vertexFile, InputStream fragmentFile) {
		vertexShader = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShader = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programmID = GL20.glCreateProgram();
		GL20.glAttachShader(programmID, vertexShader);
		GL20.glAttachShader(programmID, fragmentShader);
		bindAttributes();
		GL20.glLinkProgram(programmID);
		GL20.glValidateProgram(programmID);
		getAllUniformLocations();
	}
	
	protected void bindAttribute(int attribute, String name) {
		GL20.glBindAttribLocation(programmID, attribute, name);
	}
	
	protected int getUniformLocation(String uniform) {
		return GL20.glGetUniformLocation(programmID, uniform);
	}
	
	protected abstract void bindAttributes();
	
	protected abstract void getAllUniformLocations();
	
	public void start() {
		long nano = System.nanoTime();
		GL20.glUseProgram(programmID);
		if((System.nanoTime() - nano) / 1000000f > 1)
			System.out.println("Bound Shader: " + ((System.nanoTime() - nano) / 1000000f) + "ms");
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void load4DVector(int location, Vector4f vector) {
		long nano = System.nanoTime();
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
		if((System.nanoTime() - nano) / 1000000f > 1)
			System.out.println("Loaded 4D Vektor: " + ((System.nanoTime() - nano) / 1000000f) + "ms");
	}
	
	protected void load2DVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if(value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	public void cleanUP() {
		stop();
		GL20.glDetachShader(programmID, vertexShader);
		GL20.glDetachShader(programmID, fragmentShader);
		GL20.glDeleteShader(vertexShader);
		GL20.glDeleteShader(fragmentShader);
		GL20.glDeleteProgram(programmID);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		long nano = System.nanoTime();
		float[] matrixBuffer = new float[16];
		if((System.nanoTime() - nano) / 1000000f > 1)
			System.out.println("Loaded Matrix (Alloc Buffer): " + ((System.nanoTime() - nano) / 1000000f) + "ms");
		matrix.get(matrixBuffer);
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
		
	}
	
	private static int loadShader(InputStream file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));
			String line;
			while((line = reader.readLine()) != null){
				shaderSource.append(line).append("\n");
			}
		}catch (IOException e){
			System.err.println("Could not read file");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.print("Could not compile shader.");
			System.exit(-1);
		}
		return shaderID;
	}

}
