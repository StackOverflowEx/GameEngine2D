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
	private String title;

	public Animation(String gif, String title) {
		running = false;
		this.title = title;
		start = 0;
		this.gif = gif;
		textures = new LinkedHashMap<Texture, Integer>();
		
		try {
			getTextures();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public Animation(String gif, boolean running, long start, boolean loop, LinkedHashMap<Texture, Integer> textures, String title) {
		this.gif = gif;
		this.running = running;
		this.start = start;
		this.loop = loop;
		this.textures = textures;
		this.title = title;
	}
	
	public Animation copy() {
		return new Animation(gif, running, start, loop, textures, title);
	}

	private void getTextures() throws IOException {
		System.out.println("FRAME1 " + title);
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		System.out.println("FRAME2 " + title);
		File input = new File(gif);
		System.out.println("FRAME3 " + title);
		ImageInputStream stream = ImageIO.createImageInputStream(input);
		System.out.println("FRAME4 " + title);
		reader.setInput(stream);
		System.out.println("FRAME5 " + title);
		ImageFrame[] frames = GIFReader.readGIF(reader);
		System.out.println("FRAME6 " + title);

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
