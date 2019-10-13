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
import de.Luca.GameLogic.GameManager.HOTBARSLOT;
import de.Luca.Main.SkyFightClient;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Text.TextManager;

public class LoopHandler implements BeatHandler {

	//Handlet den Client
	
	@Override
	public void init() {
		
		//Fügt einen Librarypfad hinzu und lädt die freetype.dll
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
		
		//lädt eine standard Schriftart
		loadFonts();
		//Lädt alle Variablen in der Klasse SkyFightClient
		SkyFightClient.load();
		
		//Setzt den Clicksound für GButtons
		GUIManager.setClickSound(SkyFightClient.click);

		//initalisiert die Spieler
		SkyFightClient.p = new Player(SkyFightClient.playerUP, SkyFightClient.playerDown, new Vector2f(0, 2));
		SkyFightClient.p.setVisible(true);
		SkyFightClient.p.setFlying(false);
		SkyFightClient.p.setCollisionWithBlocks(true);
		SkyFightClient.pother = new Player(SkyFightClient.playerUP, SkyFightClient.playerDown, new Vector2f(-1, 2));
		SkyFightClient.pother.setFlying(false);
		SkyFightClient.pother.setCollisionWithBlocks(true);
		SkyFightClient.pother.setVisible(true);
		SkyFightClient.pother.setSelected(HOTBARSLOT.SWORD);

		EntityManager.addEntity(SkyFightClient.p);
		PlayerCalc.init(SkyFightClient.p);
		ArrowCalc.init();
		//Zeigt das Logingui an
		SkyFightClient.loginGUI.setVisible(true);

		//registriert den ConnectionListener
		EventManager.registerEvent(new ConnectionListener());
		//Setzt den Hintergrund fest
		MasterRenderer.setBackground(SkyFightClient.background);

		//started die Hintergrundmusik
		SkyFightClient.backgroundMusic.setVolume(0.5f);
		SkyFightClient.backgroundMusic.playSound(SkyFightClient.backMusic);
		SkyFightClient.backgroundMusic.setLoop(true);

	}

	//wird jeden Frame ausgeführt
	@Override
	public void loop() {
		//Brechnet die Spielerposition und updated die Animation
		PlayerCalc.calc();
		
		SkyFightClient.p.updateAnimation();
		SkyFightClient.pother.updateAnimation();
		
		//Brechnet die Pefile
		ArrowCalc.calc();
		//Berechnet die Popups und die Gamedaten
		PopUp.update();
		GameManager.calcGameData();
	}

	private void loadFonts() {
		TextManager.generateFont("C:\\Windows\\Fonts\\impact.ttf", 20, "Impact", false, false);
	}

}
