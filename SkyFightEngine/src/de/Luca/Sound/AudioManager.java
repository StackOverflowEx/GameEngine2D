package de.Luca.Sound;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
	
	//eine statische Klasse, die den Sound verwaltet
	
	//für OpenAL benötigte Pointer & Objekte
	private static long context;
	private static long device;
	private static ALCCapabilities alcc;
	//Sounddata (geladene OGG-Soundfiles)
	private static ConcurrentHashMap<String, ArrayList<SoundData>> buffers;
	//Alle Soundquellen
	private static CopyOnWriteArrayList<Source> sources;
	//Soundquellen, die gelöscht werden sollen
	private static CopyOnWriteArrayList<Source> deleteFinishd;
	//Position des Listeners
	private static Vector2f listenerPos;

	public static Vector2f getListenerPos() {
		return listenerPos;
	}
	
	//initalisierung von OpenAL
	public static void init() {
		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		device = ALC10.alcOpenDevice(defaultDeviceName);
		
		int[] attributes = {0};
		context = ALC10.alcCreateContext(device, attributes);
		ALC10.alcMakeContextCurrent(context);
		alcc = ALC.createCapabilities(device);
		
		AL.createCapabilities(alcc);
		buffers = new ConcurrentHashMap<String, ArrayList<SoundData>>();
		deleteFinishd = new CopyOnWriteArrayList<Source>();
		sources = new CopyOnWriteArrayList<Source>();
		
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
		
	}
	
	public static void deleteWhenFinished(Source s) {
		deleteFinishd.add(s);
	}
	
	//lädt eine OGG-File in einen Buffer und gibt ein Objekt der Klasse SoundDate zurück
	public static SoundData loadSound(String file, String soundType) {
		
		//Verhindert das doppelt laden eines Sounds
		SoundData loaded = getLoaded(file, soundType);
		if(loaded != null) {
			return loaded;
		}
		
		//erstellt einen Buffer
		int buffer = AL10.alGenBuffers();
		if(!buffers.containsKey(soundType)) {
			ArrayList<SoundData> a = new ArrayList<SoundData>();
			buffers.put(soundType, a);
		}

		//lädt die Audiofile in den Buffer
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

		//Erstellt ein Objekt der Klasse Soundata mit dem Buffer
		SoundData data = new SoundData(buffer, file);
		buffers.get(soundType).add(data);
		return data;
	}
	
	//sucht, ob eine Sounddatei bereits geladen wurde und gibt gegebenenfalls die SoundData zurück
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
	
	//Erstellt eine Soundsource, die einen Sound abspielen kann
	public static Source genSource() {
		int source = AL10.alGenSources();
		Source s = new Source(source);
		sources.add(s);
		return s;
	}
	
	//Updatet den AudioManager
	public static void update() {
		//Setzt die Position des Listeners neu
		listenerPos = WorldPosition.getExactWorldPos(Camera.getPosition());
		AL10.alListener3f(AL10.AL_POSITION, listenerPos.x, listenerPos.y, -1f);
		//Die Velocity wird auf 0 gesetzt, da der Doppler-Effekt bei geringen Geschwindigkeiten nicht genötigt wird
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
			
		//Loop alle Sources, die geloop werden sollen
		for(Source s : sources) {
			if(s.isLoop() && !s.isPlaying()) {
				s.continuePlaying();
			}
		}
		
		//löscht alle Sources, die zum Löschen markiert sind
		for(Source s : deleteFinishd) {
			if(!s.isPlaying()) {
				s.delete();
				sources.remove(s);
			}
		}
		deleteFinishd.clear();
		
//		for(Source s : sources) {
//			s.checkForDistance();
//		}
	}
	
	//Löscht alle Soundbuffer eines bestimmtes Typs
	public static void deleteSounds(String type) {
		if(buffers.containsKey(type)) {
			for(SoundData data : buffers.get(type)) {
				AL10.alDeleteBuffers(data.getID());
			}
		}
	}
	
	//Reinigt den AudioManager
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
