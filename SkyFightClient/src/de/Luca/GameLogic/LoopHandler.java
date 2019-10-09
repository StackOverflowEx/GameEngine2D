package de.Luca.GameLogic;

import java.lang.reflect.Field;

import org.joml.Vector2f;

import de.Luca.Calculation.BeatHandler;
import de.Luca.Connection.ConnectionListener;
import de.Luca.Entities.EntityManager;
import de.Luca.Entities.Player;
import de.Luca.EventManager.EventManager;
import de.Luca.GUI.GUIManager;
import de.Luca.GUIs.PopUp;
import de.Luca.Main.SkyFightClient;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Text.TextManager;

public class LoopHandler implements BeatHandler {

	@Override
	public void init() {
		
		System.setProperty("java.library.path",
				SkyFightClient.root + "\\res\\dlls;" + System.getProperty("java.library.path"));
		Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			System.out.println("ERROR TO SET DLL LOCATION");
			e.printStackTrace();
		}
		System.loadLibrary("freetype");
		
		
		loadFonts();
		SkyFightClient.load();
		
		GUIManager.setClickSound(SkyFightClient.click);

		SkyFightClient.p = new Player(SkyFightClient.playerUP, SkyFightClient.playerDown, new Vector2f(0, 2));
		SkyFightClient.p.setVisible(true);
		SkyFightClient.p.setFlying(false);
		SkyFightClient.p.setCollisionWithBlocks(true);
		SkyFightClient.pother = new Player(SkyFightClient.playerUP, SkyFightClient.playerDown, new Vector2f(-1, 2));
		SkyFightClient.pother.setFlying(false);
		SkyFightClient.pother.setCollisionWithBlocks(true);
		SkyFightClient.pother.setVisible(true);

		EntityManager.addEntity(SkyFightClient.p);
		PlayerCalc.init(SkyFightClient.p);
		ArrowCalc.init();
		SkyFightClient.loginGUI.setVisible(true);

		EventManager.registerEvent(new ConnectionListener());
		MasterRenderer.setBackground(SkyFightClient.background);

		SkyFightClient.backgroundMusic.setVolume(0.1f);
		SkyFightClient.backgroundMusic.playSound(SkyFightClient.backMusic);
		SkyFightClient.backgroundMusic.setLoop(true);

	}

	@Override
	public void loop() {
		PlayerCalc.calc();
		
		SkyFightClient.p.updateAnimation();
		SkyFightClient.pother.updateAnimation();
		
		ArrowCalc.calc();
		PopUp.update();
		GameManager.calcGameData();
	}

	private void loadFonts() {
		TextManager.generateFont("C:\\Windows\\Fonts\\impact.ttf", 20, "Impact", false, false);
	}

}
