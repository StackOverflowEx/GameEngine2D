package de.Luca.Sound;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector2f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import de.Luca.Calculation.Camera;
import de.Luca.Utils.WorldPosition;

public class AudioManager {
	
	private static long context;
	private static long device;
	private static ALCCapabilities alcc;
	private static ConcurrentHashMap<String, ArrayList<SoundData>> buffers;
	private static ArrayList<Source> sources;
	private static ArrayList<Source> deleteFinishd;
	private static Vector2f listenerPos;

	public static Vector2f getListenerPos() {
		return listenerPos;
	}
	
	public static void init() {
		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = ALC10.alcOpenDevice(defaultDeviceName);
		
		int[] attributes = {0};
		context = ALC10.alcCreateContext(device, attributes);
		ALC10.alcMakeContextCurrent(context);
		alcc = ALC.createCapabilities(device);
		
		AL.createCapabilities(alcc);
		buffers = new ConcurrentHashMap<String, ArrayList<SoundData>>();
		deleteFinishd = new ArrayList<Source>();
		sources = new ArrayList<Source>();
		
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
		
	}
	
	public static void deleteWhenFinished(Source s) {
		deleteFinishd.add(s);
	}
	
	public static SoundData loadSound(String file, String soundType) {
		
		SoundData loaded = getLoaded(file, soundType);
		if(loaded != null) {
			return loaded;
		}
		
		int buffer = AL10.alGenBuffers();
		if(!buffers.containsKey(soundType)) {
			ArrayList<SoundData> a = new ArrayList<SoundData>();
			buffers.put(soundType, a);
		}

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

		SoundData data = new SoundData(buffer, file);
		buffers.get(soundType).add(data);
		return data;
	}
	
	private static SoundData getLoaded(String file, String soundType) {
		if(buffers.containsKey(soundType)) {
			for(SoundData data : buffers.get(soundType)) {
				if(data.getFile().equals(file)) {
					return data;
				}
			}
		}
		return null;
	}
	
	public static Source genSource() {
		int source = AL10.alGenSources();
		Source s = new Source(source);
		sources.add(s);
		return s;
	}
	
	public static void update() {
		listenerPos = WorldPosition.getExactWorldPos(Camera.getPosition());
		AL10.alListener3f(AL10.AL_POSITION, listenerPos.x, listenerPos.y, -1f);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
				
		for(Source s : deleteFinishd) {
			if(!s.isPlaying()) {
				s.delete();
			}
		}
//		for(Source s : sources) {
//			s.checkForDistance();
//		}
	}
	
	public static void deleteSounds(String type) {
		if(buffers.containsKey(type)) {
			for(SoundData data : buffers.get(type)) {
				AL10.alDeleteBuffers(data.getID());
			}
		}
	}
	
	public static void cleanUP() {
		for(String type : buffers.keySet()) {
			for(SoundData data : buffers.get(type)) {
				AL10.alDeleteBuffers(data.getID());
			}
		}
		for(Source source : sources) {
			source.delete();
		}
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
}
