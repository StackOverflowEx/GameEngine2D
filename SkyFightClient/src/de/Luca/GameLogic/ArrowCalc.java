package de.Luca.GameLogic;

import org.joml.Vector2f;
import org.joml.Vector4f;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockManager;
import de.Luca.Entities.Arrow;
import de.Luca.Entities.Entity;
import de.Luca.Entities.EntityManager;
import de.Luca.Main.SkyFightClient;
import de.Luca.Networking.ServerTicker;

public class ArrowCalc {
	
	//eine statische Klasse, die die Position der Pfeile berechnet

	private static long lastCalc;
	//Gravity und Xbreak (um wie viel die Geschwindigkeit in x-Richtung pro Sekunde reduziert wird)
	private static final float GRAVITY = 9.81f;
	private static final float XBREAK = 1f;

	public static void init() {
		lastCalc = -1;
	}

	//Berechnet die Pfeile
	public static void calc() {

		float sec = (System.currentTimeMillis() - lastCalc) / 1000f;
		if (lastCalc == -1) {
			lastCalc = System.currentTimeMillis();
			return;
		}

		//Berechnung für jeden Pfeil
		for (Entity e : EntityManager.getEntities()) {
			if (e instanceof Arrow) {
				Arrow a = (Arrow) e;

				//Pfeile, die älter als 15 Sekunden sind werden entfernt (Haben nichts getroffen)
				if ((System.currentTimeMillis() - a.getCreated()) > 15 * 1000) {
					a.setVisible(false);
					EntityManager.removeEntity(a);
					continue;
				}

				//es wird der Pfeil geupdated
				calcArrow(a, sec);
				//es wird überprüft ob ein Block getroffen wurde
				calcHit(a);
				//es wird überprüft ob ein Spieler getroffen wurde
				calcPlayerHit(a);

			}
		}
		lastCalc = System.currentTimeMillis();
	}

	//boolean, ob zwei Hitboxen kollidieren
	public static boolean collides(Vector4f a, Vector4f b) {
		Vector2f aSize = new Vector2f(a.z - a.x, a.w - a.y);
		Vector2f bSize = new Vector2f(b.z - b.x, b.w - b.y);
		if (Math.abs(a.x - b.x) < aSize.x + bSize.x) {
			if (Math.abs(a.y - b.y) < aSize.y + bSize.y) {
				return true;
			}
		}

		return false;
	}

	//boolean ob eine Hitbox in einer anderen ist
	public static boolean inside(Vector4f a, Vector4f b) {
		Vector2f aSize = new Vector2f(a.z - a.x, a.w - a.y);
		if (Math.abs(a.x - b.x) < aSize.x) {
			if (Math.abs(a.y - b.y) < aSize.y) {
				return true;
			}
		}
		return false;
	}

	private static void calcPlayerHit(Arrow a) {
		if (a.isVisible()) {
			if (a.getShooter().equals(SkyFightClient.p)) {
				//Der Spieler testet immer seinen eigenen Pfeil und schaut, ob er den Gegner getroffen hat
				//Hitboxen werden in einer Variable gespeichert
				Vector4f phit = SkyFightClient.pother.getHitBox(SkyFightClient.pother.getWorldPos());
				Vector4f ahit = a.getHitBox(a.getWorldPos());

				//es wird überprüft ob eine Kollision vorliegt
				if(collides(ahit, phit) || inside(ahit, phit)) {
					//trifft der Pfeil, verliert der Gegner Leben und der Pfeil wird entfernt
					a.setVisible(false);
					ServerTicker.addArrowChange(0, 0, 0, 0, a.getUUID(), false, true);
					EntityManager.removeEntity(a);
					ServerTicker.addDmgDelt(8);
					a.playSound(SkyFightClient.arrowHit, 50, false);
				}
			}
		}
	}

	private static void calcHit(Arrow a) {
		//es wird geschaut, mit welchen Blöcken der Pfeil kollidiert.
		for (Block b : BlockManager.isCollidingWithBlock(a.getHitBox(a.getWorldPos()))) {
			//hat der Blocke ein Härte von 4 oder weniger geht er kapput
			if (b.getBlockData().getHardness() <= 4 && a.getShooter().equals(SkyFightClient.p) && a.isVisible()) {
				BlockManager.removeBlock(b);
				a.setVisible(false);
				EntityManager.removeEntity(a);
				ServerTicker.addArrowChange(0, 0, 0, 0, a.getUUID(), false, false);
				GameManager.setValue(GameManager.getValue() + b.getBlockData().getValue());
				ServerTicker.addBlockChange((int) b.getWorldPos().x, (int) b.getWorldPos().y,
						b.getBlockData().getName(), 1f);
				a.playSound(SkyFightClient.arrowHit, 50, false);
				return;
			}else {
				if(a.getShooter().equals(SkyFightClient.p) && a.isVisible() && b.getBlockData().getHardness() > 4) {
					a.setVisible(false);
					EntityManager.removeEntity(a);
					ServerTicker.addArrowChange(0, 0, 0, 0, a.getUUID(), false, false);
					a.playSound(SkyFightClient.arrowHit, 50, false);
					return;
				}
			}
			continue;
		}
	}

	//die position des Pfeils wird berechnet
	private static void calcArrow(Arrow a, float sec) {

		float addX = a.getxVel() * sec;
		float addY = a.getyVel() * sec;
		a.moveWithoutCollisionCheck(new Vector2f(addX, addY));

		addY = GRAVITY * sec;
		addX = XBREAK * sec;
		a.setxVel(a.getxVel() - addX);
		a.setyVel(a.getyVel() - addY);

		//es wird der Winkel berechnet, in dem der Pfeil fliegt, damit die Spitze immer in Flugrichtung zeigt
		Vector2f tra = new Vector2f(a.getxVel(), a.getyVel());
		tra = tra.normalize();
		Vector2f xAxis = new Vector2f(1, 0);
		float angle = -tra.angle(xAxis);
		angle = (float) Math.toDegrees(angle);
		a.getModels()[0].setRoll(angle + 180);

	}

}
