package de.Luca.GameLogic;

import org.joml.Vector2f;

import de.Luca.Calculation.BeatHandler;
import de.Luca.Connection.Connection;
import de.Luca.Connection.ConnectionListener;
import de.Luca.Entities.EntityManager;
import de.Luca.Entities.Player;
import de.Luca.EventManager.EventManager;
import de.Luca.GUIs.PopUp;
import de.Luca.Main.SkyFightClient;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Text.TextManager;
import de.Luca.World.WorldEditor;

public class LoopHandler implements BeatHandler{

	@Override
	public void init() {
		loadFonts();
		SkyFightClient.load();
		
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
//		SkyFightClient.loginGUI.setVisible(true);
		
		EventManager.registerEvent(new ConnectionListener());
		MasterRenderer.setBackground(SkyFightClient.background);
		
		WorldEditor.start("C:\\Users\\Luca\\AppData\\Roaming\\SkyFight\\maps\\own\\Test");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Connection con = new Connection("167.86.87.105", 33334);
			}
		}).start();
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
