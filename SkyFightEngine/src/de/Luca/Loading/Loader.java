package de.Luca.Loading;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.Luca.Entities.Texture;
import de.Luca.Rendering.MasterRenderer;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {

	public static ArrayList<Integer> textures = new ArrayList<Integer>();
	public static ArrayList<Integer> vaos = new ArrayList<Integer>();
	public static ArrayList<Integer> vbos = new ArrayList<Integer>();
	public static int QuadVAO = -1;

	public static int getQuadVAO() {
		if (QuadVAO == -1) {
			QuadVAO = loadQuadVAO();
		}
		return QuadVAO;
	}

	public static int loadTexture(ByteBuffer buffer, int width, int height) {
		
		int textureID = GL11.glGenTextures();
		textures.add(textureID);
		
		if(buffer == null) {
			return textureID;
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				buffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		return textureID;

	}

	public static Texture loadTexture(String file) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		
		if(!new File(file).exists()) {
			Texture texture = new Texture(buffer, width, height);
			MasterRenderer.masterRenderer.queueTexture(texture);
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

		Texture texture = new Texture(buffer, width, height);
		MasterRenderer.masterRenderer.queueTexture(texture);

		return texture;
	}

	public static int loadQuadVAO() {
		int vaoID = createVAO();
		storeDataInAttributeList(0, new float[] { -1, 1, -1, -1, 1, 1, 1, -1 });
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

	public static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	public static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	public static void cleanUP() {
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
		textures.clear();
	}

}
