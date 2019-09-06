package de.Luca.Sound;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import de.Luca.Calculation.Camera;

public class AudioManager {
	
	private static long context;
	private static long device;
	private static ALCCapabilities alcc;
	private static ArrayList<Integer> buffers;
	private static ArrayList<Source> sources;
	
	public static void init() {
		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = ALC10.alcOpenDevice(defaultDeviceName);
		
		int[] attributes = {0};
		context = ALC10.alcCreateContext(device, attributes);
		ALC10.alcMakeContextCurrent(context);
		alcc = ALC.createCapabilities(device);
		
		AL.createCapabilities(alcc);
		buffers = new ArrayList<Integer>();
		sources = new ArrayList<Source>();
		
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
		
	}
	
	public static SoundData loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);

		MemoryStack.stackPush();
		IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
		MemoryStack.stackPush();
		IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

		ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(file, channelsBuffer, sampleRateBuffer);

		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();
		MemoryStack.stackPop();
		MemoryStack.stackPop();
		
		int format = -1;
		if(channels == 1) {
		    format = AL10.AL_FORMAT_MONO16;
		} else if(channels == 2) {
		    format = AL10.AL_FORMAT_STEREO16;
		}

		AL10.alBufferData(buffer, format, rawAudioBuffer, sampleRate);

		return new SoundData(buffer);
	}
	
	public static Source genSource() {
		int source = AL10.alGenSources();
		Source s = new Source(source);
		sources.add(s);
		return s;
	}
	
	public static void update() {
		AL10.alListener3f(AL10.AL_POSITION, Camera.getPosition().x, Camera.getPosition().y, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
		
//		for(Source s : sources) {
//			s.checkForDistance();
//		}
	}
	
	public static void cleanUP() {
		for(int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		for(Source source : sources) {
			source.delete();
		}
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
}
