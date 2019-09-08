package de.Luca.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.EventManager.EventManager;
import de.Luca.GUIs.PopUp;
import de.Luca.GameLogic.GameState;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Rendering.MasterRenderer;

public class WorldEditor {
	
	private static WorldEditorListener listener;
	private static ArrayList<String> names;
	private static boolean saving;
	private static String background;
	
	public static void start() {
		SkyFightClient.gameState = GameState.WORLDEDITOR;
		listener = new WorldEditorListener();
		EventManager.registerEvent(listener);
		saving = false;
		if(names == null) {
			names = new ArrayList<String>();
		}
		background = null;
		//Show Worldeditor GUI
	}
	
	public static void stop() {
		SkyFightClient.gameState = GameState.MENUE;
		EventManager.removeListener(listener);
		names.clear();
		WorldLoader.clearBlockData();
		BlockManager.removeAllBlocks();
		//Show mainmenue GUI
	}
	
	public static boolean validBlockName(String name) {
		if(!names.contains(name)) {
			names.add(name);
			return true;
		}
		return false;
	}
	
	public static void setBackground(String texture) {
		MasterRenderer.switchBackground(Loader.loadTexture(texture, "background"), 1000);
		background = texture;
	}
		
	public static void save(String mapName) {
		WorldLoader.mapName = mapName;
		File maps = new File(SkyFightClient.root + "/maps/own/" + WorldLoader.mapName);
		if(maps.exists()) {
			new PopUp("Dieser Name ist bereits besetzt", new Vector4f(1, 0, 0, 1));
			return;
		}
		if(saving) {
			new PopUp("Bitte warte bis der Speichervorgang abgeschlossen ist.", new Vector4f(1, 0, 0, 1));
			return;
		}
		saving = true;
		ArrayList<BlockData> saved = new ArrayList<BlockData>();
		ArrayList<String> bs = new ArrayList<String>();
		for(int x : BlockManager.getBlocks().keySet()) {
			for(int y : BlockManager.getBlocks().get(x).keySet()) {
				Block b = BlockManager.getBlock(new Vector2f(x, y));
				BlockData data = b.getBlockData();
				if(!saved.contains(data)) {
					if(!saveBlockData((BlockDataPre) data)) {
						return;
					}
				}
				String blocks = data.getName() + ";" + (int)b.getWorldPos().x + ";" + (int)b.getWorldPos().y;
				bs.add(blocks);
			}
		}
		if(!saveBlocksAndMapName(bs, mapName)) {
			return;
		}
		if(!saveBackground()) {
			return;
		}
		
	}
	
	
	
	private static boolean saveBackground() {
		if(background == null) {
			return true;
		}
		File maps = new File(SkyFightClient.root + "/maps/own/" + WorldLoader.mapName);
		File mapdata = new File(maps.getPath() + "/mapdata");
		File file = new File(background);
		File dest = new File(mapdata.getPath() + "/background.png");
		try {
			Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			delete(maps);
			new PopUp("Ein Fehler ist beim Kopieren der Hintergrundtexture aufgetreten.", new Vector4f(1, 0, 0, 1));
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static boolean saveBlocksAndMapName(ArrayList<String> blocks, String mapName) {
		File maps = new File(SkyFightClient.root + "/maps/own/" + WorldLoader.mapName);
		File mapdata = new File(maps.getPath() + "/mapdata");
		if(!mapdata.exists()) {
			mapdata.mkdirs();
		}
		File d = new File(mapdata.getPath() + "/blocks.txt");
		try {
			PrintWriter pw = new PrintWriter(d);
			pw.println("name=" + mapName);
			for(String s : blocks) {
				pw.println(s);
			}
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			delete(maps);
			new PopUp("Ein Fehler ist beim Speichern der Map aufgetreten.", new Vector4f(1, 0, 0, 1));
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static boolean saveBlockData(BlockDataPre data) {
		File maps = new File(SkyFightClient.root + "/maps/own/" + WorldLoader.mapName);
		File blockdata = new File(maps.getPath() + "/blockdata/" + data.getName());
		if(!blockdata.exists()) {
			blockdata.mkdirs();
		}
		
		File d = new File(blockdata.getPath() + "/data.properties");
		try {
			System.out.println(d.getPath());
			d.createNewFile();
			PrintWriter pw = new PrintWriter(d);
			pw.println("name=" + data.getName());
			pw.println("value=" + data.getValue());
			pw.println("hardness=" + data.getHardness());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			delete(maps);
			new PopUp("Ein Fehler ist beim Speichern der Map aufgetreten.", new Vector4f(1, 0, 0, 1));
			e.printStackTrace();
			return false;
		}
		
		File file = new File(data.getTexture());
		File dest = new File(blockdata.getPath() + "/texture.png");
		try {
			Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			delete(maps);
			new PopUp("Ein Fehler ist beim Kopieren der " + data.getName() + " Textur aufgetreten.", new Vector4f(1, 0, 0, 1));
			e.printStackTrace();
			return false;
		}
		
		try {
			if(data.getBreakSoundFile() != null) {
				file = new File(data.getBreakSoundFile());
				dest = new File(blockdata.getPath() + "/break.ogg");
				Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			if(data.getPlaceSoundFile() != null) {
				file = new File(data.getPlaceSoundFile());
				dest = new File(blockdata.getPath() + "/place.ogg");
				Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			if(data.getWalkSoundFile() != null) {
				file = new File(data.getWalkSoundFile());
				dest = new File(blockdata.getPath() + "/walk.ogg");
				Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}catch (IOException e) {
			delete(maps);
			new PopUp("Ein Fehler ist beim Kopieren einer Sounddatei des Blocks " + data.getName() + " aufgetreten.", new Vector4f(1, 0, 0, 1));
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void delete(File dir) {
		if(dir.isDirectory()) {
			for(File f : dir.listFiles()) {
				delete(f);
			}
		}
		dir.delete();
	}

}
