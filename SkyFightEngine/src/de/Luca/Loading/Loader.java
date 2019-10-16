package de.Luca.Loading;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
	
	//eine statische Klasse, die für das Laden von Texturen und Buffern verantwortlich ist

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

	//laden einer Textur
	public static int loadTexture(Texture tex, String textureType) {

		if(tex.getTextureID() != -1) {
			return tex.getTextureID();
		}
		
		//textur wird erstellt.
		int textureID = GL11.glGenTextures();

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

	//Lädt zwei Buffer in ein VBO und bindet diese VBOs an ein VAO
	public static int loadToVAO(float[] verticies, float[] textureCoords) {
		int vaoID = createVAO();
		int vboID1 = storeDataInAttributeList(0, verticies);
		int vboID2 = storeDataInAttributeList(1, textureCoords);
		vbos.put(vaoID, new int[] { vboID1, vboID2 });
		unbindVAO();
		return vaoID;
	}

	//speichert übergebene Indicies
	public static int storeIndicies(int[] indices) {
		int vboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		return vboID;
	}

	//speichert einen Array in einem Buffer
	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	//löscht ein bestimmtes VAO
	public static void cleanVAO(int vaoID) {
		for (int i : vbos.get(vaoID)) {
			GL30.glDeleteBuffers(i);
		}
		GL30.glDeleteVertexArrays(vaoID);
		vbos.remove(vaoID);
	}

	//Lädt ein Bild in einen ByteBuffer und übergibt die Textur dem Render-Thread zur Erstellung
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
			return texture;
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

	//Lädt ein BufferedImage in einen Bytebuffer und übergibt die Textur dem Render-Thread zur Erstellung
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

	//lädt ein Quadrat
	public static int loadQuadVAO() {
		int vaoID = createVAO();
		int vboID = storeDataInAttributeList(0, new float[] { 0, 0, 0, 1, 1, 0, 1, 1 });
		vbos.put(vaoID, new int[] { vboID });
		unbindVAO();
		return vaoID;
	}

//	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
//		ByteBuffer buffer;
//
//		Path path = Paths.get(resource);
//		if (Files.isReadable(path)) {
//			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
//				buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
//				while (fc.read(buffer) != -1) {
//					;
//				}
//			}
//		} else {
//			try (InputStream source = Loader.class.getClassLoader().getResourceAsStream(resource);
//					ReadableByteChannel rbc = Channels.newChannel(source)) {
//				buffer = ByteBuffer.allocate(bufferSize);
//
//				while (true) {
//					int bytes = rbc.read(buffer);
//					if (bytes == -1) {
//						break;
//					}
//					if (buffer.remaining() == 0) {
//						buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
//					}
//				}
//			}
//		}
//
//		buffer.flip();
//		return buffer;
//	}

//	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
//		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
//		buffer.flip();
//		newBuffer.put(buffer);
//		return newBuffer;
//	}

	//Speichert einen Floatarray in einem VBO und bindet das VBO an eine entsprechende Stelle im VAO
	public static int storeDataInAttributeList(int attributeNumber, float[] data) {
		int vboID = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	//Speichert einen Array in einem Buffer
	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	//unbindet ein VAO
	public static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	//erstellt ein VAO
	public static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	//zerstört eine Textur, falls diese nicht mehr gebraucht wird.
	public static void destroyTexture(Texture texture) {
		GL11.glDeleteTextures(texture.getTextureID());
		textures.get(texture.getTextureType()).remove(texture.getTextureID());
		texture.setTextureID(-1);
	}

	//Schaut, ob ein PNG schon geladen wurde und gibt gegebenenfalls die Textur zurück um das Mehrfachladen zu verhindern
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
	
	//Löscht alle Texturen eines bestimmten Typens
	public static void deleteTextures(String type) {
		if (textures.containsKey(type)) {
			for (Texture texture : textures.get(type)) {
				deleteNext.add(texture);
			}
			textures.remove(type);
		}
	}
	
	//Löscht alle Texturen, die zum Löschen markiert sind (wird vom Render-Thread ausgeführt)
	public static void deleteQueued() {
		for(Texture tex : deleteNext) {
			GL11.glDeleteTextures(tex.getTextureID());
		}
		deleteNext.clear();
	}

	//Bereinigt den Loader
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
