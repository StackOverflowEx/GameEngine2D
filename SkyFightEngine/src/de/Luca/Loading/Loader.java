package de.Luca.Loading;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.Luca.Models.Texture;
import de.Luca.Rendering.MasterRenderer;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {

	public static HashMap<String, ArrayList<Texture>> textures = new HashMap<String, ArrayList<Texture>>();
	public static HashMap<Integer, int[]> vbos = new HashMap<Integer, int[]>();
	public static ArrayList<Texture> deleteNext = new ArrayList<Texture>();
	public static int QuadVAO = -1;

	public static int getQuadVAO() {
		if (QuadVAO == -1) {
			QuadVAO = loadQuadVAO();
		}
		return QuadVAO;
	}

	public static int loadTexture(Texture tex, String textureType) {

		if(tex.getTextureID() != -1) {
			return tex.getTextureID();
		}
		
		int textureID = GL11.glGenTextures();
//		if(!textures.containsKey(textureType)) {
//			ArrayList<Integer> a = new ArrayList<Integer>();
//			textures.put(textureType, a);
//		}
//		textures.get(textureType).add(textureID);

		if (tex.getBuffer() == null) {
			return textureID;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tex.getWidth(), tex.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
				tex.getBuffer());
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		return textureID;

	}

	// LB, LT, RB, RT
	public static int loadToVAO(float[] verticies, float[] textureCoords) {
		int vaoID = createVAO();
//		System.out.println("VAO " + vaoID);
		int vboID1 = storeDataInAttributeList(0, verticies);
		int vboID2 = storeDataInAttributeList(1, textureCoords);
		vbos.put(vaoID, new int[] { vboID1, vboID2 });
		unbindVAO();
		return vaoID;
	}

	public static int storeIndicies(int[] indices) {
		int vboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		return vboID;
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static void cleanVAO(int vaoID) {
		for (int i : vbos.get(vaoID)) {
			GL30.glDeleteBuffers(i);
		}
		GL30.glDeleteVertexArrays(vaoID);
		vbos.remove(vaoID);
	}

	public static Texture loadTexture(String file, String textureType) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		
		if(file != null) {
			Texture loaded = getLoaded(file, textureType);
			if(loaded != null) {
				return loaded;
			}
		}

		if (file == null || !new File(file).exists()) {
			Texture texture = new Texture(buffer, width, height, textureType, file);
			MasterRenderer.queueTexture(texture);
		}

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

		Texture texture = new Texture(buffer, width, height, textureType, file);

		if (!textures.containsKey(textureType)) {
			ArrayList<Texture> a = new ArrayList<Texture>();
			textures.put(textureType, a);
		}
		textures.get(textureType).add(texture);

		MasterRenderer.queueTexture(texture);

		return texture;
	}

	public static Texture loadTexture(BufferedImage image, String textureType) {
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
			}
		}

		buffer.flip();

		Texture texture = new Texture(buffer, image.getWidth(), image.getHeight(), textureType, UUID.randomUUID().toString());
		
		if (!textures.containsKey(textureType)) {
			ArrayList<Texture> a = new ArrayList<Texture>();
			textures.put(textureType, a);
		}
		textures.get(textureType).add(texture);
		
		MasterRenderer.queueTexture(texture);

		return texture;
	}

	public static int loadQuadVAO() {
		int vaoID = createVAO();
		int vboID = storeDataInAttributeList(0, new float[] { 0, 0, 0, 1, 1, 0, 1, 1 });
		vbos.put(vaoID, new int[] { vboID });
		unbindVAO();
		return vaoID;
	}

	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		Path path = Paths.get(resource);
		if (Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
				while (fc.read(buffer) != -1) {
					;
				}
			}
		} else {
			try (InputStream source = Loader.class.getClassLoader().getResourceAsStream(resource);
					ReadableByteChannel rbc = Channels.newChannel(source)) {
				buffer = ByteBuffer.allocate(bufferSize);

				while (true) {
					int bytes = rbc.read(buffer);
					if (bytes == -1) {
						break;
					}
					if (buffer.remaining() == 0) {
						buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
					}
				}
			}
		}

		buffer.flip();
		return buffer;
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	public static int storeDataInAttributeList(int attributeNumber, float[] data) {
		int vboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	public static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	public static void destroyTexture(Texture texture) {
		GL11.glDeleteTextures(texture.getTextureID());
		textures.get(texture.getTextureType()).remove(texture.getTextureID());
		texture.setTextureID(-1);
	}

	private static Texture getLoaded(String file, String textureType) {
		if(textures.containsKey(textureType)) {
			for(Texture tex : textures.get(textureType)) {
				if(tex.getFile().equals(file)) {
					return tex;
				}
			}
		}
		return null;
	}
	
	public static void deleteTextures(String type) {
		if (textures.containsKey(type)) {
			for (Texture texture : textures.get(type)) {
				deleteNext.add(texture);
			}
			textures.remove(type);
		}
	}
	
	public static void deleteQueued() {
		for(Texture tex : deleteNext) {
			GL11.glDeleteTextures(tex.getTextureID());
		}
		deleteNext.clear();
	}

	public static void cleanUP() {
		for (String type : textures.keySet()) {
			for (Texture texture : textures.get(type)) {
				GL11.glDeleteTextures(texture.getTextureID());
			}
		}
		textures.clear();
		for (int vaos : vbos.keySet()) {
			for (int vbos : vbos.get(vaos)) {
				GL30.glDeleteBuffers(vbos);
			}
			GL30.glDeleteVertexArrays(vaos);
		}
		vbos.clear();
	}

}
