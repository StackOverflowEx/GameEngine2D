package de.Luca.GameLogic;

import org.joml.Vector2f;

import de.Luca.Calculation.BeatHandler;
import de.Luca.Entities.EntityManager;
import de.Luca.Entities.Player;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.TextManager;

public class LoopHandler implements BeatHandler{

	@Override
	public void init() {
		SkyFightClient.p = new Player(Loader.loadTexture("D:\\Downloads\\up.png"), Loader.loadTexture("D:\\Downloads\\down.png"), new Vector2f(0, 0));
		SkyFightClient.p.setFlying(true);
		EntityManager.addEntity(SkyFightClient.p);
		PlayerCalc.init(SkyFightClient.p);
		loadFonts();
		SkyFightClient.load();
		SkyFightClient.loginGUI.setVisible(true);
	}

	@Override
	public void loop() {
		PlayerCalc.calc();
	}
	
	private void loadFonts() {
		TextManager.generateFont("C:\\Windows\\Fonts\\impact.ttf", 20, "Impact", false, false);
	}

}
