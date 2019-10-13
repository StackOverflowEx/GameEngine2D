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

	private static long lastCalc;
	private static final float GRAVITY = 9.81f;
	private static final float XBREAK = 1f;

	public static void init() {
		lastCalc = -1;
	}

	public static void calc() {

		float sec = (System.currentTimeMillis() - lastCalc) / 1000f;
		if (lastCalc == -1) {
			lastCalc = System.currentTimeMillis();
			return;
		}

		for (Entity e : EntityManager.getEntities()) {
			if (e instanceof Arrow) {
				Arrow a = (Arrow) e;

				if ((System.currentTimeMillis() - a.getCreated()) > 15 * 1000) {
					a.setVisible(false);
					EntityManager.removeEntity(a);
					continue;
				}

				calcArrow(a, sec);
				calcHit(a);
				calcPlayerHit(a);

			}
		}
		lastCalc = System.currentTimeMillis();
	}

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
				Vector4f phit = SkyFightClient.pother.getHitBox(SkyFightClient.pother.getWorldPos());
				Vector4f ahit = a.getHitBox(a.getWorldPos());

				if(collides(ahit, phit) || inside(ahit, phit)) {
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
		for (Block b : BlockManager.isCollidingWithBlock(a.getHitBox(a.getWorldPos()))) {
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
			}
			continue;
		}
	}

	private static void calcArrow(Arrow a, float sec) {

		float addX = a.getxVel() * sec;
		float addY = a.getyVel() * sec;
		a.moveWithoutCollisionCheck(new Vector2f(addX, addY));

		addY = GRAVITY * sec;
		addX = XBREAK * sec;
		a.setxVel(a.getxVel() - addX);
		a.setyVel(a.getyVel() - addY);

		Vector2f tra = new Vector2f(a.getxVel(), a.getyVel());
		tra = tra.normalize();
		Vector2f xAxis = new Vector2f(1, 0);
		float angle = -tra.angle(xAxis);
		angle = (float) Math.toDegrees(angle);
		a.getModels()[0].setRoll(angle + 180);

	}

}
