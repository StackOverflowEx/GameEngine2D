package de.Luca.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.GUIs.PopUp;
import de.Luca.GameLogic.GameState;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Sound.AudioManager;

public class WorldLoader {
	
	private static HashMap<String, BlockData> blockData = new HashMap<String, BlockData>();
	
	public static void clearBlockData() {
		Loader.deleteTextures("block");
		AudioManager.deleteSounds("block");
		blockData.clear();
	}
	
	public static void loadMap(String mapFolder, String name) {
		File map = new File(mapFolder);
		if(!map.exists()) {
			new PopUp("Die Map wurde nicht gefunden und kann nicht geladen werden.", new Vector4f(1, 0, 0, 1));
			return;
		}
		clearBlockData();
		loadBlockData(map, name);
		loadBlocks(map, name);
		laodBackground(map, name);
	}
	
	private static void laodBackground(File map, String name) {
		File background = new File(map.getPath() + "/mapdata/background.png");
		if(background.exists()) {
			MasterRenderer.switchBackground(Loader.loadTexture(background.getPath(), "background"), 1000);
		}
	}
	
	private static void loadBlocks(File map, String name) {
		File mapdata = new File(map.getPath() + "/mapdata");
		File blocks = new File(mapdata.getPath() + "/blocks.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(blocks));
			String line;
			while((line = br.readLine()) != null) {
				if(!line.isEmpty()) {
					String[] tmp = line.split(";");
					String blockName = tmp[0];
					int x = Integer.parseInt(tmp[1]);
					int y = Integer.parseInt(tmp[2]);
					if(blockData.containsKey(blockName)) {
						Block b = new Block(blockData.get(blockName), new Vector2f(x, y));
						BlockManager.addBlock(b);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			new PopUp("Ein Fehler ist beim Laden der Map " + name + " aufgetreten.", new Vector4f(1, 0, 0, 1));
			clearBlockData();
			return;
		}
	}
	
	private static void loadBlockData(File map, String name) {
		File blockdata = new File(map.getPath() + "/blockdata");
		for(File block : blockdata.listFiles()) {
			File data = new File(block.getPath() + "/data.properties");
			String texture = block.getPath() + "/texture.png";
			String breakSound = block.getPath() + "/break.ogg";
			String placeSound = block.getPath() + "/place.ogg";
			String walkSound = block.getPath() + "/walk.ogg";
			if(!new File(breakSound).exists()) {
				breakSound = null;
			}
			if(!new File(placeSound).exists()) {
				placeSound = null;
			}
			if(!new File(walkSound).exists()) {
				walkSound = null;
			}
			
			String bname = "defualt";
			float value = 1.0f;
			float hardness = 1.0f;
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(data));
				bname = br.readLine().replace("name=", "");
				value = Float.parseFloat(br.readLine().replace("value=", ""));
				hardness = Float.parseFloat(br.readLine().replace("hardness=", ""));
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				new PopUp("Ein Fehler ist beim Laden der Map " + name + " aufgetreten.", new Vector4f(1, 0, 0, 1));
				clearBlockData();
				return;
			}
			BlockData bd = null;
			if(SkyFightClient.gameState == GameState.WORLDEDITOR) {
				bd = new BlockDataPre(value, hardness, bname, texture, breakSound, placeSound, walkSound);
			}else {
				bd = new BlockData(value, hardness, bname, Loader.loadTexture(texture, "block"), breakSound, placeSound, walkSound);
			}
			blockData.put(bname, bd);
		}
	}

}
