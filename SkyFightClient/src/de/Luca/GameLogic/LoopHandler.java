package de.Luca.GameLogic;

import org.joml.Vector2f;

import de.Luca.Calculation.BeatHandler;
import de.Luca.Connection.ConnectionListener;
import de.Luca.Entities.EntityManager;
import de.Luca.Entities.Player;
import de.Luca.EventManager.EventManager;
import de.Luca.GUIs.PopUp;
import de.Luca.Main.SkyFightClient;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Text.TextManager;

public class LoopHandler implements BeatHandler{

	@Override
	public void init() {
		loadFonts();
		SkyFightClient.load();
		
		SkyFightClient.p = new Player(SkyFightClient.playerUP, SkyFightClient.playerDown, new Vector2f(0, 0));
		SkyFightClient.p.setFlying(true);
		SkyFightClient.p.setCollisionWithBlocks(false);
		EntityManager.addEntity(SkyFightClient.p);
		PlayerCalc.init(SkyFightClient.p);
//		SkyFightClient.loginGUI.setVisible(true);
		
		EventManager.registerEvent(new ConnectionListener());
		MasterRenderer.setBackground(SkyFightClient.background);
		
	}

	@Override
	public void loop() {
		PlayerCalc.calc();
		PopUp.update();
	}
	
	private void loadFonts() {
		TextManager.generateFont("C:\\Windows\\Fonts\\impact.ttf", 20, "Impact", false, false);
	}

}
