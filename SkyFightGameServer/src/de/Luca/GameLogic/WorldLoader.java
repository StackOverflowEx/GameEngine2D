package de.Luca.GameLogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import de.Luca.Packets.Packet;

public class WorldLoader {
	
	private static HashMap<String, BlockData> blockData = new HashMap<String, BlockData>();
	public static String mapName = "Default";
	
	public static void clearBlockData() {
		blockData.clear();
	}
	
	public static String getMapName() {
		return mapName;
	}
	
	public static void loadMap(String mapFolder) {
		File map = new File(mapFolder);
		if(!map.exists()) {
			GameLogic.sendGlobalError(Packet.ERROR_SERVER);
			System.exit(-1);
		}
		clearBlockData();
		loadBlockData(map);
		loadBlocks(map);
		System.out.println("Map " + mapName + " has successfully been loaded.");
	}
	
	private static void loadBlocks(File map) {
		File mapdata = new File(map.getPath() + "/mapdata");
		File blocks = new File(mapdata.getPath() + "/blocks.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(blocks));
			String line;
			while((line = br.readLine()) != null) {
				if(!line.isEmpty()) {
					if(line.startsWith("name=")) {
						mapName = line.replace("name=", "");
						continue;
					}
					String[] tmp = line.split(";");
					String blockName = tmp[0];
					int x = Integer.parseInt(tmp[1]);
					int y = Integer.parseInt(tmp[2]);
					if(blockData.containsKey(blockName)) {
						Blocks.addBlock(blockName, x, y);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			GameLogic.sendGlobalError(Packet.ERROR_SERVER);
			System.exit(-1);
			return;
		}
	}
	
	private static void loadBlockData(File map) {
		File blockdata = new File(map.getPath() + "/blockdata");
		for(File block : blockdata.listFiles()) {
			File data = new File(block.getPath() + "/data.properties");
			
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
				GameLogic.sendGlobalError(Packet.ERROR_SERVER);
				System.exit(-1);
				return;
			}
			BlockData bd = new BlockData(bname, value, hardness);
			Blocks.addBlockData(bd);
		}
	}

}
