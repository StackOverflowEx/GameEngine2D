package de.Luca.GameLogic;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockManager;
import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.CursorPositionEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Main.SkyFightClient;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Utils.WorldPosition;

public class GameListener implements Listener{
	
	private Block breaking;
	
	@EventHandler
	public void onMouse(MouseButtonEvent e) {
		if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(e.getAction() == GLFW.GLFW_PRESS) {
				Vector2f mouse = WorldPosition.getMouseWorldPos();
				Vector2f pPos = new Vector2f(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y);
				pPos.x += 0.5f;
				pPos.y += 1;
				if(mouse.distance(pPos) > 5) {
					return;
				}
				if(BlockManager.doseBlockExist(mouse)) {
					breaking = BlockManager.getBlock(mouse);
				}
			}else if(e.getAction() == GLFW.GLFW_RELEASE) {
				reset();
			}
		}else if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			if(e.getAction() == GLFW.GLFW_PRESS) {
				Vector2f mouse = WorldPosition.getMouseWorldPos();
				Vector2f pPos = new Vector2f(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y);
				pPos.x += 0.5f;
				pPos.y += 1;
				if(mouse.distance(pPos) > 5) {
					return;
				}
				if(BlockManager.doseBlockExist(mouse)) {
					return;
				}
				//PLACE Block --> Collisioncheck: Addblock with Breakpercent = 1 --> Check for Collision. If Player is colliding with this block, cancle
			}
		}
	}
	
	@EventHandler
	public void onMove(CursorPositionEvent e) {
		if(DefaultKeyListener.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			Block newBlock = BlockManager.getBlock(WorldPosition.getMouseWorldPos());
			if(newBlock != breaking) {
				reset();
			}
			if(newBlock != null) {
				breaking = newBlock;
			}
		}
	}
	
	public void reset() {
		if(breaking != null) {
			breaking.setBreakPercentage(0);
		}
		breaking = null;
	}
	
	public Block getBreaking() {
		return breaking;
	}

}
