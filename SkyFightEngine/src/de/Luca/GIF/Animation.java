package de.Luca.GIF;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;

public class Animation {

	private LinkedHashMap<Texture, Integer> textures;
	private boolean running;
	private String gif;
	private long start;
	private boolean loop;

	public Animation(String gif) {
		running = false;
		start = 0;
		this.gif = gif;
		textures = new LinkedHashMap<Texture, Integer>();
		
		try {
			getTextures();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Animation(String gif, boolean running, long start, boolean loop, LinkedHashMap<Texture, Integer> textures) {
		this.gif = gif;
		this.running = running;
		this.start = start;
		this.loop = loop;
		this.textures = textures;
	}
	
	public Animation copy() {
		return new Animation(gif, running, start, loop, textures);
	}

	private void getTextures() throws IOException {
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		File input = new File(gif);
		ImageInputStream stream = ImageIO.createImageInputStream(input);
		reader.setInput(stream);
		ImageFrame[] frames = GIFReader.readGIF(reader);

		for (ImageFrame frame : frames) {
			BufferedImage img = frame.getImage();
			Texture tex = Loader.loadTexture(img, "animation");
			System.out.println(frame.getDelay());
			textures.put(tex, frame.getDelay());
		}
	}

	public void start(boolean loop) {
		if (!running) {
			running = true;
			start = System.currentTimeMillis();
			this.loop = loop;
		}
	}

	public Texture getFrame() {
		if(!running) {
			return null;
		}
		float time = 0;
		float timeRunning = (System.currentTimeMillis() - start) / 1000f;
		for(Texture tex : textures.keySet()) {
			int delay = textures.get(tex);
			time += delay / 100f;
			if(time >= timeRunning) {
				return tex;
			}
		}
		if(loop) {
			start = System.currentTimeMillis();
		}else {
			running = false;
		}
		for(Texture tex : textures.keySet()) {
			return tex;
		}
		return null;
	}
	
	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}

}
