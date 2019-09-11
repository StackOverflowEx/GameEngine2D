package de.Luca.GameLogic;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Vector4f;

public class Blocks {
	
	private static ConcurrentHashMap<Integer, HashMap<Integer, String>> blocks = new ConcurrentHashMap<Integer, HashMap<Integer, String>>();
	private static ConcurrentHashMap<String, BlockData> data = new ConcurrentHashMap<String, BlockData>();
	
	public static void addBlock(String name, int x, int y) {
		if(!blocks.contains(x)) {
			HashMap<Integer, String> tmp = new HashMap<Integer, String>();
			blocks.put(x, tmp);
		}
		blocks.get(x).put(y, name);
	}
	
	public static void addBlockData(BlockData bd) {
		data.put(bd.getName(), bd);
	}
	
	public static BlockData getBlockData(int x, int y) {
		if(!blocks.contains(x)) {
			return null;
		}
		if(!blocks.get(x).containsKey(y)) {
			return null;
		}
		return data.get(blocks.get(x).get(y));
	}
	
	public static boolean doseBlockExist(int x, int y) {
		if(blocks.containsKey((int)x)) {
			return blocks.get((int)x).containsKey((int)y);
		}
		return false;
	}
	
	public static boolean isCollidingWithBlock(float x, float y) {
		Vector4f hitBox = new Vector4f(x + 0.05f, y , x + 1f - 0.05f, y + 2f - 0.01f);
		float blockX = (float) Math.floor(hitBox.x());
		float blockY = (float) Math.floor(hitBox.y());
		float block1X = (float) Math.floor(hitBox.z());
		float block1Y = (float) Math.floor(hitBox.w());
				
						 
		for(float i = blockX; i <= block1X; i++) {
			if(!blocks.containsKey((int)i)) {
				continue;
			}
			for(float n = blockY; n <= block1Y; n++) {
				if(!blocks.get((int)i).containsKey((int)n)) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

}
