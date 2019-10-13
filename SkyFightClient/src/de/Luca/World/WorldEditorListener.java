package de.Luca.World;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Sound.SoundData;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Utils.WorldPosition;

public class WorldEditorListener implements Listener{
	
	public static BlockData selectedBlock;
	
	@EventHandler
	public void onClick(MouseButtonEvent e) {
		if(!e.isCancelled()) {
			if(e.getAction() == GLFW.GLFW_RELEASE && e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				//Schaut, ob der Spawn-Setz-Modus aktiv ist
				if(WorldEditor.getSettingSpawn() != -1) {
					//Setzt den entsprechenden Spawn
					if(WorldEditor.getSettingSpawn() == 1) {
						WorldEditor.setSpawn1(WorldPosition.getMouseWorldPos());
					}
					if(WorldEditor.getSettingSpawn() == 2) {
						WorldEditor.setSpawn2(WorldPosition.getMouseWorldPos());
					}
					WorldEditor.setSettingSpawn(-1);
					return;
				//Schaut ob der Spiegelmodus aktiv ist
				}else if(WorldEditor.isMirroring()) {
					//Spiegelt alle Blöcke an der y-Koordinate der Maus
					int mouseX = (int) WorldPosition.getMouseWorldPos().x;
					ArrayList<Block> add = new ArrayList<Block>();
					for(int x : BlockManager.getBlocks().keySet()) {
						int delta = mouseX - x;
						int newX = x + 2*delta;
						for(int y : BlockManager.getBlocks().get(x).keySet()) {
							Block b = BlockManager.getBlock(new Vector2f(x, y));
							Block newBlock = new Block(b.getBlockData(), new Vector2f(newX, y));
							add.add(newBlock);
						}
					}
					for(Block b : add) {
						BlockManager.addBlock(b);
						if(b.getBlockData().getPlaceSound() != null) {
							playSound(b.getBlockData().getPlaceSound(), b);
						}
					}
					WorldEditor.setMirroring(false);
					return;
				}
			}
			
			if(WorldEditor.getSettingSpawn() != -1) {
				return;
			}
			
			//Ist der Spiegelmodus und der Spawn-Setz-Modus nicht aktiv wird bei einem Linksklick ein Block gesetzt
			if((e.getAction() == GLFW.GLFW_PRESS || e.getAction() == GLFW.GLFW_REPEAT) && e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				Block block = BlockManager.getBlock(WorldPosition.getMouseWorldPos());
				if(block != null) {
					if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
						selectedBlock = block.getBlockData();
					}
					return;
				}
				if(selectedBlock != null) {
					Block b = new Block(selectedBlock, WorldPosition.getMouseWorldPos());
					BlockManager.addBlock(b);
					if(b.getBlockData().getPlaceSound() != null) {
						playSound(b.getBlockData().getPlaceSound(), b);
					}
				}
				return;
			//und bei einem Rechtsklick ein Block entfernt
			}else if((e.getAction() == GLFW.GLFW_PRESS || e.getAction() == GLFW.GLFW_REPEAT) && e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				Block b = BlockManager.getBlock(WorldPosition.getMouseWorldPos());
				if(b != null) {
					BlockManager.removeBlock(b);
					if(b.getBlockData().getBreakSound() != null) {
						playSound(b.getBlockData().getBreakSound(), b);
					}
				}
			}
			
		}
	}
	
	public void playSound(SoundData sd, Block b) {
		Random r = new Random();
		float ran = r.nextFloat() - 0.5f;
		b.playSound(sd, 1 + ran, 1);
	}

}
