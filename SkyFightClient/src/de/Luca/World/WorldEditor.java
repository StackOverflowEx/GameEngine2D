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
	
	//eine statische Klasse, die das Bearbeiten der Welt verwaltet
	
	//Listener
	private static WorldEditorListener listener;
	//enthält blocknamen
	private static ArrayList<String> names;
	//Boolean ob die WElt gerade gespeichert wird
	private static boolean saving;
	//Hingergrund datei
	private static String background;
	//Spawnpunkte
	private static Vector2f spawn1, spawn2;
	//boolean, ob neue map
	private static boolean newMap;
	//boolean ob Spiegel-Modus aktiviert
	private static boolean mirroring;
	//Spawn, der gesetzt werden soll
	private static int settingSpawn;
	
	//startet den Worldeditor, mit einer schon vorhandenen Karte
	public static void start(String mapFolder) {
		SkyFightClient.gameState = GameState.WORLDEDITOR;
		listener = new WorldEditorListener();
		EventManager.registerEvent(listener);
		saving = false;
		mirroring = false;
		if(names == null) {
			names = new ArrayList<String>();
		}
		newMap = false;
		background = null;
		names.clear();
		
		PopUp p = new PopUp("Die Welt wird geladen...", new Vector4f(1, 0.7f, 0, 1));
		WorldLoader.loadMap(mapFolder);
		if(new File(mapFolder + "/mapdata/background.png").exists()) {
			SkyFightClient.worldEditorSettings.setup(WorldLoader.getMapName(), mapFolder + "/mapdata/background.png");
		}else {
			SkyFightClient.worldEditorSettings.setup(WorldLoader.getMapName());
		}
		
		p.destroy();
		
		SkyFightClient.p.setPosition(new Vector2f(0, 0));
		SkyFightClient.p.setVisible(true);
		SkyFightClient.p.setCollisionWithBlocks(false);
		SkyFightClient.p.setFlying(true);
		SkyFightClient.pother.setVisible(false);
		
		SkyFightClient.worldEditorAuswahl.init();
		SkyFightClient.worldEditorOverlay.setVisible(true);
	}
	
	
	//startet den WOrldeditor mit einer leeren Welt
	public static void start() {
		
		
		MasterRenderer.switchBackground(MasterRenderer.getDefaultBackgroundTexture(), 0);
		WorldLoader.clearBlockData();
		SkyFightClient.worldEditorAuswahl.init();

		BlockManager.removeAllBlocks();
		SkyFightClient.gameState = GameState.WORLDEDITOR;
		listener = new WorldEditorListener();
		EventManager.registerEvent(listener);
		saving = false;
		mirroring = false;
		spawn1 = null;
		spawn2 = null;
		if(names == null) {
			names = new ArrayList<String>();
		}
		newMap = true;
		background = null;
		names.clear();
		
		SkyFightClient.p.setPosition(new Vector2f(0, 0));
		SkyFightClient.p.setVisible(true);
		SkyFightClient.p.setCollisionWithBlocks(false);
		SkyFightClient.p.setFlying(true);
		SkyFightClient.pother.setVisible(false);
		
		SkyFightClient.worldEditorOverlay.setVisible(true);
	}
	
	public static boolean isMirroring() {
		return mirroring;
	}
	
	public static void setMirroring(boolean mirroring) {
		WorldEditor.mirroring = mirroring;
	}
	
	public static void setSettingSpawn(int i) {
		settingSpawn = i;
	}
	
	public static int getSettingSpawn() {
		return settingSpawn;
	}
	
	public static void setNewMap(boolean newMap) {
		WorldEditor.newMap = newMap;
	}
	
	public static boolean isNewMap() {
		return newMap;
	}
	
	public static void setSpawn1(Vector2f location) {
		spawn1 = location;
	}
	
	public static void setSpawn2(Vector2f location) {
		spawn2 = location;
	}
	
	public static Vector2f getSpawn1() {
		return spawn1;
	}
	
	public static Vector2f getSpawn2() {
		return spawn2;
	}
	
	//beendet den Welteditor
	public static void stop() {
		SkyFightClient.gameState = GameState.MENUE;
		EventManager.removeListener(listener);
		names.clear();
		WorldLoader.clearBlockData();
		BlockManager.removeAllBlocks();
		SkyFightClient.worldEditorOverlay.setVisible(false);
		SkyFightClient.worldEditorAuswahl.setVisible(false);
		SkyFightClient.worldEditorSettings.setVisible(false);
		SkyFightClient.worldEditorErstellen.setVisible(false);
		SkyFightClient.mainGUI.setVisible(true);
		MasterRenderer.setBackground(MasterRenderer.getDefaultBackgroundTexture());
	}
	
	public static boolean validBlockName(String name) {
		
		if(names == null) {
			names = new ArrayList<String>();
		}
		
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
		
	
	//Speichert die Map
	public static void save(String mapName) {
//		PopUp p = new PopUp("Die Map wird gespeichert...", new Vector4f(1, 0.7f, 0, 1), true);
		WorldLoader.mapName = mapName;
		//es wird geprüft ob der Name schon existiert
		File maps = new File(SkyFightClient.root + "/maps/own/" + WorldLoader.mapName);
		if(maps.exists() && isNewMap()) {
			new PopUp("Dieser Name ist bereits besetzt", new Vector4f(1, 0, 0, 1));
			return;
		}
		//es wird geprüft ob der Speichervorgang läuft
		if(saving) {
			new PopUp("Bitte warte bis der Speichervorgang abgeschlossen ist.", new Vector4f(1, 0, 0, 1));
			return;
		}
		
		//Wird eine alte Map bearbeitet wird die Map im TMP-Ordner gespeichert
		String screenshotFolder = maps.getPath();
		if(!isNewMap()) {
			maps = new File(SkyFightClient.root + "/maps/tmp/" + WorldLoader.mapName);
		}
		maps.mkdirs();
		
		saving = true;		
		
		ArrayList<BlockData> saved = new ArrayList<BlockData>();
		ArrayList<String> bs = new ArrayList<String>();
		//es werden alle Blöcke gespeichert
		for(int x : BlockManager.getBlocks().keySet()) {
			for(int y : BlockManager.getBlocks().get(x).keySet()) {
				Block b = BlockManager.getBlock(new Vector2f(x, y));
				BlockData data = b.getBlockData();
				//Wurde die Blockdata noch nicht gespeichert, wird diese gespeichert
				if(!saved.contains(data)) {
					if(!saveBlockData(data, maps.getPath())) {
						return;
					}
				}
				String blocks = data.getName() + ";" + (int)b.getWorldPos().x + ";" + (int)b.getWorldPos().y;
				bs.add(blocks);
			}
		}
		if(!saveBlocksAndMapName(bs, mapName, maps.getPath())) {
			return;
		}
		if(!saveBackground(maps.getPath())) {
			return;
		}
		
		//wurde eine alte Map bearbeitet, wird die gespeicherte Map aus dem TMP-Ordner in den richtigen verschoben
		if(!newMap) {
			File old = new File(SkyFightClient.root + "/maps/own/" + WorldLoader.mapName);
			try {
				delete(old);
				Files.move(maps.toPath(), old.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				new PopUp("Ein Fehler ist beim Speichern der Map aufgetreten. Bitte verschiebe die erstellte Map aus den Ornder \"tmp\" in den Ornder \"own\"", new Vector4f(1, 0, 0, 1));
				e.printStackTrace();
				return;
			}
		}
		MasterRenderer.queueScreenshot(new File(screenshotFolder + "/preview.png"));
//		p.destroy();
		new PopUp("Die Map wurde gespeichert.", new Vector4f(0, 1, 0, 1));
		SkyFightClient.worldSelctGUI.init();
		
	}
	
	//es wird der Hintergrund gespeichert
	private static boolean saveBackground(String map) {
		if(background == null) {
			return true;
		}
		File maps = new File(map);
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
	
	//Es werden die Blöcke und weitere Mapdaten gespeicehrt
	private static boolean saveBlocksAndMapName(ArrayList<String> blocks, String mapName, String map) {
		File maps = new File(map);
		File mapdata = new File(maps.getPath() + "/mapdata");
		if(!mapdata.exists()) {
			mapdata.mkdirs();
		}
		File d = new File(mapdata.getPath() + "/blocks.txt");
		try {
			PrintWriter pw = new PrintWriter(d);
			pw.println("name=" + mapName);
			pw.println("spawn1=" + spawn1.x + ";" + spawn1.y);
			pw.println("spawn2=" + spawn2.x + ";" + spawn2.y);
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
	
	//Die Blockdata wird gespeichert
	private static boolean saveBlockData(BlockData data, String map) {
		File maps = new File(map);
		File blockdata = new File(maps.getPath() + "/blockdata/" + data.getName());
		if(!blockdata.exists()) {
			blockdata.mkdirs();
		}
		
		File d = new File(blockdata.getPath() + "/data.properties");
		try {
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
		
		File file = new File(data.getBlockModel().getModel().getTexture().getFile());
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
			if(data.getBreakSound() != null) {
				file = new File(data.getBreakSound().getFile());
				dest = new File(blockdata.getPath() + "/break.ogg");
				Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			if(data.getPlaceSound() != null) {
				file = new File(data.getPlaceSound().getFile());
				dest = new File(blockdata.getPath() + "/place.ogg");
				Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			if(data.getWalkSound() != null) {
				file = new File(data.getWalkSound().getFile());
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
