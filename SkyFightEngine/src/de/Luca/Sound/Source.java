package de.Luca.Sound;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

public class Source {
	
	//Sound source
	
	//openAL -ID
	private int id;
	private float maxAudibleDistance;
	private boolean loop;
	private boolean isDeleted;
	
	public Source(int id) {
		this.id = id;
		maxAudibleDistance = 40;
		loop = false;
		isDeleted = false;
		
		AL10.alSourcef(id, AL10.AL_ROLLOFF_FACTOR, 1);
		AL10.alSourcef(id, AL10.AL_REFERENCE_DISTANCE, 1);
		AL10.alSourcef(id, AL10.AL_MAX_DISTANCE, 50);
	}
	
	public float getMaxAudibleDistance() {
		return maxAudibleDistance;
	}
	
	public void setMaxAudibleDistanace(float distance) {
		this.maxAudibleDistance = distance;
	}
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public boolean isLoop() {
		return loop;
	}
	
	public int getID() {
		return id;
	}
	
	public float getDistanceToListener() {
		float[] x = new float[1];
		float[] y = new float[1];
		float[] z = new float[1];
		AL10.alGetListener3f(AL10.AL_POSITION, x, y, z);
		Vector3f p = new  Vector3f(x[0], y[0], z[0]);
		float distance = Math.abs(distanceTo(p));
		return distance;
	}
	
	private float distanceTo(Vector3f vec) {
		float[] x = new float[1];
		float[] y = new float[1];
		float[] z = new float[1];
		AL10.alGetSource3f(id, AL10.AL_POSITION, x, y, z);
	    return (float) Math.sqrt(Math.pow(x[0] - vec.x, 2) + Math.pow(y[0] - vec.y, 2) + Math.pow(z[0] - vec.z, 2));
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void delete() {
		AL10.alDeleteSources(id);
		isDeleted = true;
	}
	
	public void setPosition(Vector2f worldPos) {
		AL10.alSourcei(id, AL10.AL_SOURCE_RELATIVE, AL10.AL_FALSE);
		AL10.alSourcei(id, AL10.AL_SOURCE_ABSOLUTE, AL10.AL_TRUE);
		AL10.alSource3f(id, AL10.AL_POSITION, worldPos.x, worldPos.y, 0);
	}
	
	public void setVolume(float volume) {
		AL10.alSourcef(id, AL10.AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(id, AL10.AL_PITCH, pitch);
	}
	
	public void loadSound(SoundData sd) {
		AL10.alSourcei(id, AL10.AL_BUFFER, sd.getID());
	}
	
	public void playSound(SoundData sd) {
		AL10.alSourcei(id, AL10.AL_BUFFER, sd.getID());
		AL10.alSourcePlay(id);
	}

	public void continuePlaying() {
		AL10.alSourcePlay(id);
	}
	
	public void pause() {
		AL10.alSourcePause(id);
	}
	
	public void stop() {
		AL10.alSourceStop(id);
	}
	
	public boolean isPlaying() {
		return getState() == AL10.AL_PLAYING;
	}
	
	public int getState() {
		return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE);
	}
	
}
