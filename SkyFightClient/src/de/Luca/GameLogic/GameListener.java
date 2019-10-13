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
import de.Luca.Events.KeyEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.GameLogic.GameManager.HOTBARSLOT;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.ServerTicker;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Utils.WorldPosition;

public class GameListener implements Listener {

	//listener für die Funktion des Games
	
	//Block, der gerade abgebaut wird
	private Block breaking;
	//Zeit in ms, zu der der letzte Schlag stattgefunden hat
	private long lastHit;
	//Zeit, wann der Bogen gespannt wurde
	private long startedBow;
	
	public boolean isShooting() {
		return startedBow != -1;
	}
	
	@EventHandler
	public void onKey(KeyEvent e) {
		if(e.getAction() == GLFW.GLFW_PRESS) {
			if(e.getKey() == GLFW.GLFW_KEY_1) {
				SkyFightClient.ingameOverlay.setSelectedSlot(HOTBARSLOT.SWORD);
			}else if(e.getKey() == GLFW.GLFW_KEY_2) {
				SkyFightClient.ingameOverlay.setSelectedSlot(HOTBARSLOT.BOW);
			}else if(e.getKey() == GLFW.GLFW_KEY_3) {
				SkyFightClient.ingameOverlay.setSelectedSlot(HOTBARSLOT.PICKAXE);
			}else if(e.getKey() == GLFW.GLFW_KEY_4) {
				SkyFightClient.ingameOverlay.setSelectedSlot(HOTBARSLOT.BLOCK);
			}
		}
	}
	
	@EventHandler
	public void onBow(MouseButtonEvent e) {

		//Wird abgebrochen, wenn der Bogen nicht ausgewählt wird
		if(!SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.BOW) || e.isCancelled()) {
			startedBow = -1;
			return;
		}
		
		//Wird der linke Mausbutton gedrückt, wird der Bogen angespannt. Wird der rechte Button gedrückt wird das Anspannen abgebrochen
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
		
		//Es wird geschaut, ob der Spieler genug Coins für einen Pfeil hat
		if(GameManager.getValue() < 2) {
			startedBow = -1;
			return;
		}
		
		//Es wird der Winkel berechnet in welchem der Pfeil los fliegt
		Vector2f mousePos = WorldPosition.getMouseExactWorldPos();
		Vector2f tra = new Vector2f(mousePos);
		tra = tra.sub(SkyFightClient.p.getWorldPos());
		tra = tra.normalize();
		Vector2f xAxis = new Vector2f(1, 0);
		float angle = -tra.angle(xAxis);
		angle = (float) Math.toDegrees(angle);
		System.out.println(angle);
		
		//Es wird die x- und y-Velocity gesetzt, die von der Zeit abhängt, die der Bogen gespannt ist und vom Abschusswinkel abhängt
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
		
		//es wird ein Pfeil erstellt und ein entsprechender Sound abgespielt
		Arrow a = new Arrow(new Vector2f(SkyFightClient.p.getWorldPos()), SkyFightClient.arrow, yVel, xVel, SkyFightClient.p);
		EntityManager.addEntity(a);
		GameManager.setValue(GameManager.getValue() - 2);
		a.playSound(SkyFightClient.bowShoot, 50, false);
		ServerTicker.addArrowChange(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y, xVel, yVel, a.getUUID(), true, false);
		startedBow = -1;
	}

	@EventHandler
	public void onAttach(MouseButtonEvent e) {

		if(e.getAction() != GLFW.GLFW_PRESS || e.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			return;
		}
		
		//es wird getestet ob nicht der Bogen ausgewählt ist
		if(SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.BOW)) {
			return;
		}
		
		//Es kann nur 5 mal in der Sekunde geschlagen werden
		if((System.currentTimeMillis() - lastHit) <= 200) {
			return;
		}
		lastHit = System.currentTimeMillis();
		
		//Es kann nur 3 Blöcke weit geschlagen werden
		float distance = SkyFightClient.p.getWorldPos().distance(SkyFightClient.pother.getWorldPos());
		if (distance < 3) {
			//Der Gegner muss mit der oberen oder unteren Hälfte vor einem stehen
			if (Math.abs(SkyFightClient.pother.getWorldPos().y - SkyFightClient.p.getWorldPos().y) <= 1) {
				//Der Spieler schlägt in die richtung in die er schaut, es wird getestet ob der Gegner auch in dieser Richtung steht
				if ((SkyFightClient.p.isFacingRight()
						&& SkyFightClient.pother.getWorldPos().x > SkyFightClient.p.getWorldPos().x)
						|| (!SkyFightClient.p.isFacingRight()
								&& SkyFightClient.pother.getWorldPos().x < SkyFightClient.p.getWorldPos().x)) {
					//Schlägt der Spieler nicht mit dem Schwert, macht er nur einen Schaden
					float dmg = 5;
					if(!SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.SWORD)) {
						dmg = 1;
					}
					//Bei einem hit erhält der Gegner schaden und eine Animaiton und ein Sound werden abgespielt
					SkyFightClient.p.playSound(SkyFightClient.hit, 50, false);
					SkyFightClient.p.startAnimation(0, SkyFightClient.punchDown);
					SkyFightClient.p.startAnimation(1, SkyFightClient.punchUp);
					ServerTicker.addHit();
					ServerTicker.addDmgDelt(dmg);
					return;
				}
			}
		}
		
		//hat der Spieler ins leere geschlagen und keinen Blockabgebaut, wird eine Animation und ein Sound abgespielt
		if(SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.PICKAXE) && BlockManager.doseBlockExist(WorldPosition.getMouseWorldPos())) {
			if(WorldPosition.getMouseWorldPos().distance(SkyFightClient.p.getWorldPos()) <= 3.5) {
				return;
			}
		}
		
		ServerTicker.addHit();
		SkyFightClient.p.startAnimation(0, SkyFightClient.punchDown);
		SkyFightClient.p.startAnimation(1, SkyFightClient.punchUp);
		SkyFightClient.p.playSound(SkyFightClient.missHit, 50, false);

	}

	@EventHandler
	public void onMouse(MouseButtonEvent e) {
		
		if(SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.BOW)) {
			breaking = null;
			return;
		}

		//Blockbreak
		if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			Vector2f mouse = WorldPosition.getMouseWorldPos();
			if (e.getAction() == GLFW.GLFW_PRESS) {		
				Vector2f pPos = new Vector2f(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y);
				pPos.x += 0.5f;
				pPos.y += 1;
				//Es kann nur 3,5 Blöcke entfernt ein Block abgebaut werden
				if (mouse.distance(pPos) > 3.5) {
					return;
				}
				//es wird getestet, ob der Spieler nicht ins leere geklickt hat
				if (BlockManager.doseBlockExist(mouse)) {
					breaking = BlockManager.getBlock(mouse);
				}
			} else if (e.getAction() == GLFW.GLFW_RELEASE) {
				//lässt der Spieler die linke Maustaste los, wird das Abbauen abgebrochen
				Block b = BlockManager.getBlock(mouse);
				if(b != null) {
					ServerTicker.addBlockChange((int)mouse.x, (int)mouse.y, b.getBlockData().getName(), 0f);
				}
				reset();
			}
		//Blockplace
		} else if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			if (e.getAction() == GLFW.GLFW_PRESS) {
				Vector2f mouse = WorldPosition.getMouseWorldPos();
				Vector2f pPos = new Vector2f(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y);
				pPos.x += 0.5f;
				pPos.y += 1;
				//Es kann nur 3,5 Blöcke entfernt ein Block gesetzt werden
				if (mouse.distance(pPos) > 3.5) {
					return;
				}
				if (BlockManager.doseBlockExist(mouse)) {
					return;
				}
				
				//Es muss ein Block ausgewählt sein
				if(SkyFightClient.ingameOverlay.getSelectedSlot() == HOTBARSLOT.BLOCK) {
					BlockData bd = SkyFightClient.ingameOverlay.getSelectedBlock();
					if(bd != null) {
						//Der Spieler muss genug Coins haben, um den Block zu setzen
						if(bd.getValue() > GameManager.getValue()) {
							System.out.println(GameManager.getValue());
							return;
						}
						//der Block wird erstellt
						Block b = new Block(bd, mouse);
						BlockManager.addBlock(b);
						//es wird geschaut, ob der Block mit einem Spieler kollidiert, ist das der Fall, wird er wieder entfernt
						if(!BlockManager.isCollidingWithBlock(SkyFightClient.p.getHitBox(SkyFightClient.p.getWorldPos())).contains(b)){
							if(!BlockManager.isCollidingWithBlock(SkyFightClient.pother.getHitBox(SkyFightClient.pother.getWorldPos())).contains(b)){
								//kann der Block gesetzt werden, werden Coins abgezogen, Sounds abgespielt und der Blockchange an den Gegner gesendet
								GameManager.setValue(GameManager.getValue() - Math.abs(bd.getValue()));
								if(bd.getValue() == 0) {
									GameManager.setValue(GameManager.getValue() - 1f);
								}
								if(b.getBlockData().getPlaceSound() != null) {
									b.playSound(b.getBlockData().getPlaceSound(), 1, 1);
								}else {
									b.playSound(SkyFightClient.breakingSound, 1, 1);
								}
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

	//Es reicht den Mauszeiger zu verschieben um einen neuen Block abzubauen
	@EventHandler
	public void onMove(CursorPositionEvent e) {
		
		if(SkyFightClient.ingameOverlay.getSelectedSlot().equals(HOTBARSLOT.BOW)) {
			breaking = null;
			return;
		}
		
		
		Vector2f mouse = WorldPosition.getMouseWorldPos();
		Vector2f pPos = new Vector2f(SkyFightClient.p.getWorldPos().x, SkyFightClient.p.getWorldPos().y);
		pPos.x += 0.5f;
		pPos.y += 1;
		//Es kann nur 3,5 Blöcke entfernt ein Block abgebaut werden. Ist der Block weiter entfernt wird das Abbauen abgebrochen
		if (mouse.distance(pPos) > 3.5f) {
			if(breaking != null) {
				ServerTicker.addBlockChange((int)breaking.getWorldPos().x, (int)breaking.getWorldPos().y, breaking.getBlockData().getName(), 0f);
			}
			reset();
			return;
		}
		
		//Befindet sich der Cursor auf einem neuen Block, wird dieser abgebaut
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
