package rendering;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import models.Texture;

public class Loader {

	private static ArrayList<Integer> vaos = new ArrayList<Integer>();
	private static ArrayList<Integer> vbos = new ArrayList<Integer>();
	private static ArrayList<Integer> textures = new ArrayList<Integer>();

	private static RawModel quad;

	public static RawModel getQuad() {
		if (quad == null) {
//			float[] positions = new float[] {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
			float[] positions = new float[] { 0, 1, 0, 0, 1, 1, 1, 0 };
			quad = Loader.loadToVAO(positions);
		}
		return quad;
	}

	public static RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 2);
	}

	public static RawModel loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		storeIndicies(indices);
		storeDataInAttributeList(0, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 2);
	}

	public static RawModel loadToVAO(float[] positions, int[] indices, float[] texureCoords) {
		int vaoID = createVAO();
		storeIndicies(indices);
		storeDataInAttributeList(0, positions);
		storeDataInAttributeList(1, texureCoords);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 2);
	}

	public static RawModel loadToVAO(float[] positions, float[] texureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, positions);
		storeDataInAttributeList(1, texureCoords);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 2);
	}

	public static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	public static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	public static void storeIndicies(int[] indices) {
		int vboID = GL30.glGenBuffers();
		vbos.add(vboID);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
	}

	public static void storeDataInAttributeList(int attributeNumber, float[] data) {
		int vboID = GL30.glGenBuffers();
		vbos.add(vboID);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static Texture loadTexture(String file) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(file);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to load texture " + file);
			System.exit(-1);
		}

		int textureID = GL11.glGenTextures();
		textures.add(textureID);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				buffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		return new Texture(textureID);
	}
	
	public static Texture loadTexture(de.t0b1.freetype_wrapper.classes.Texture tex) {
		int width = tex.width;
		int height = tex.height;
		
		
		System.out.println(width + " | " + height);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(tex.data.length);
		buffer.put(tex.data);
		buffer.flip();
		
		int textureID = GL11.glGenTextures();
		textures.add(textureID);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		return new Texture(textureID);
	}

	
	public static int createVBO(int attribute) {
		int vbo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.nglBufferData(vbo, 12l, MemoryUtil.NULL, GL30.GL_DYNAMIC_DRAW);
		GL30.glVertexAttribPointer(attribute, 2, GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		return vbo;
	}

	public static void cleanUP() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL30.glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}

}
