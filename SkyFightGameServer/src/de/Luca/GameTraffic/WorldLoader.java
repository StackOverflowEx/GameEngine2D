package de.Luca.GameTraffic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.joml.Vector2f;


public class WorldLoader {
		
	public static Vector2f spawn1, spawn2;

	public static void load(File map) {
		File mapdata = new File(map.getPath() + "/mapdata");
		File blocks = new File(mapdata.getPath() + "/blocks.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(blocks));
			String line;
			while((line = br.readLine()) != null) {
				if(!line.isEmpty()) {
					if(line.startsWith("spawn1=")) {
						line = line.replace("spawn1=", "");
						float x = Float.parseFloat(line.split(";")[0]);
						float y = Float.parseFloat(line.split(";")[1]);
						spawn1 = new Vector2f(x, y);
						continue;
					}
					if(line.startsWith("spawn2=")) {
						line = line.replace("spawn2=", "");
						float x = Float.parseFloat(line.split(";")[0]);
						float y = Float.parseFloat(line.split(";")[1]);
						spawn2 = new Vector2f(x, y);
						continue;
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}
	}
	
}