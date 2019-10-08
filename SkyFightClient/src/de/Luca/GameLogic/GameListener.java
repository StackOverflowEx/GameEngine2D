package de.Luca.GameLogic;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.Entities.Arrow;
import de.Luca.Entities.EntityManager;
import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.CursorPositionEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.GameLogic.GameManager.HOTBARSLOT;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.ServerTicker;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Utils.WorldPosition;

public class GameListener implements Listener {

	private Block breaking;
	private long lastHit;
	private long startedBow;
	
	public boolean isShooting() {
		return startedBow != -1;
	}
	
	@EventHandler
	public void onBow(MouseButtonEvent e) {

		if(!SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.BOW) || e.isCancelled()) {
			startedBow = -1;
			return;
		}
		
		if(e.getAction() != GLFW.GLFW_RELEASE || e.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(e.getAction() == GLFW.GLFW_PRESS && e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				startedBow = System.currentTimeMillis();
			}
			if(e.getAction() == GLFW.GLFW_PRESS && e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				startedBow = -1;
			}
			return;
		}
		
		if(startedBow == -1) {
			return;
		}
		
		if(GameManager.getValue() < 4) {
			return;
		}
		
		Vector2f mousePos = WorldPosition.getMouseExactWorldPos();
		Vector2f tra = new Vector2f(mousePos);
		tra = tra.sub(SkyFightClient.p.getWorldPos());
		tra = tra.normalize();
		Vector2f xAxis = new Vector2f(1, 0);
		float angle = -tra.angle(xAxis);
		angle = (float) Math.toDegrees(angle);
		System.out.println(angle);
		
		float xVel, yVel;
		float mul = (System.currentTimeMillis() - startedBow) / 50;
		System.out.println(mul);
		if(mul > 20) {
			mul = 20;
		}
		if(mul < 5) {
			mul = 5;
		}
		xVel = tra.x * mul;
		yVel = tra.y * mul;
		
		Arrow a = new Arrow(new Vector2f(SkyFightClient.p.getWorldPos()), Loader.loadTexture("D:\\Test.png", "bow"), yVel, xVel, SkyFightClient.p);
		EntityManager.addEntity(a);
		ServerTicker.addArrowChange(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y, xVel, yVel, a.getUUID(), true);
		startedBow = -1;
	}

	@EventHandler
	public void onAttach(MouseButtonEvent e) {

		if(e.getAction() != GLFW.GLFW_PRESS || e.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			return;
		}
		
		float distance = SkyFightClient.p.getWorldPos().distance(SkyFightClient.pother.getWorldPos());
		if (distance < 3) {
			if (Math.abs(SkyFightClient.pother.getWorldPos().y - SkyFightClient.p.getWorldPos().y) < 1) {
				if ((SkyFightClient.p.isFacingRight()
						&& SkyFightClient.pother.getWorldPos().x > SkyFightClient.p.getWorldPos().x)
						|| (!SkyFightClient.p.isFacingRight()
								&& SkyFightClient.pother.getWorldPos().x < SkyFightClient.p.getWorldPos().x)) {
					if((System.currentTimeMillis() - lastHit) > 50) {
						lastHit = System.currentTimeMillis();
						float dmg = 5;
						if(!SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.SWORD)) {
							dmg = 1;
						}
						ServerTicker.addDmgDelt(dmg);
					}
				}
			}
		}

	}

	@EventHandler
	public void onMouse(MouseButtonEvent e) {
		
		if(SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.BOW)) {
			return;
		}

		if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			Vector2f mouse = WorldPosition.getMouseWorldPos();
			if (e.getAction() == GLFW.GLFW_PRESS) {		
				Vector2f pPos = new Vector2f(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y);
				pPos.x += 0.5f;
				pPos.y += 1;
				if (mouse.distance(pPos) > 3) {
					return;
				}
				if (BlockManager.doseBlockExist(mouse)) {
					breaking = BlockManager.getBlock(mouse);
				}
			} else if (e.getAction() == GLFW.GLFW_RELEASE) {
				Block b = BlockManager.getBlock(mouse);
				if(b != null) {
					ServerTicker.addBlockChange((int)mouse.x, (int)mouse.y, b.getBlockData().getName(), 0f);
				}
				reset();
			}
		} else if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			if (e.getAction() == GLFW.GLFW_PRESS) {
				Vector2f mouse = WorldPosition.getMouseWorldPos();
				Vector2f pPos = new Vector2f(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y);
				pPos.x += 0.5f;
				pPos.y += 1;
				if (mouse.distance(pPos) > 3) {
					return;
				}
				if (BlockManager.doseBlockExist(mouse)) {
					return;
				}
				
				if(SkyFightClient.ingameOverlay.getSelectedSlot() == HOTBARSLOT.BLOCK) {
					BlockData bd = SkyFightClient.ingameOverlay.getSelectedBlock();
					if(bd != null) {
						if(bd.getValue() > GameManager.getValue()) {
							System.out.println(GameManager.getValue());
							return;
						}
						Block b = new Block(bd, mouse);
						BlockManager.addBlock(b);
						if(!BlockManager.isCollidingWithBlock(SkyFightClient.p.getHitBox(SkyFightClient.p.getWorldPos())).contains(b)){
							if(!BlockManager.isCollidingWithBlock(SkyFightClient.pother.getHitBox(SkyFightClient.p.getWorldPos())).contains(b)){
								GameManager.setValue(GameManager.getValue() - bd.getValue());
								ServerTicker.addBlockChange((int)b.getWorldPos().x, (int)b.getWorldPos().y, b.getBlockData().getName(), 0f);
								return;
							}
						}
						BlockManager.removeBlock(b);
					}
				}
				
			}
		}
	}

	@EventHandler
	public void onMove(CursorPositionEvent e) {
		if (DefaultKeyListener.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			Block newBlock = BlockManager.getBlock(WorldPosition.getMouseWorldPos());
			if (newBlock != null && breaking != null && !newBlock.equals(breaking)) {
				if(breaking != null) {
					ServerTicker.addBlockChange((int)breaking.getWorldPos().x, (int)breaking.getWorldPos().y, breaking.getBlockData().getName(), 0f);
				}
				reset();
			}
			if (newBlock != null) {
				breaking = newBlock;
			}
		}
	}

	public void reset() {
		if (breaking != null) {
			breaking.setBreakPercentage(0);
		}
		breaking = null;
	}

	public Block getBreaking() {
		return breaking;
	}

}
