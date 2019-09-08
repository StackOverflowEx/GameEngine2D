package de.Luca.GameLogic;

import org.joml.Vector2f;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockManager;
import de.Luca.Calculation.BeatHandler;
import de.Luca.Connection.ConnectionListener;
import de.Luca.Entities.EntityManager;
import de.Luca.Entities.Player;
import de.Luca.EventManager.EventManager;
import de.Luca.GUIs.PopUp;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.TextManager;
import de.Luca.WorldEditor.BlockDataPre;
import de.Luca.WorldEditor.WorldEditor;

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
		
		EventManager.registerEvent(new ConnectionListener());
		
		
		BlockDataPre pre = new BlockDataPre(10, 10, "Test", "D:\\Icons\\Icon2.png");
		
		Block b = new Block(pre, new Vector2f(0, 0));
		Block b1 = new Block(pre, new Vector2f(0, 1));
		BlockManager.addBlock(b);
		BlockManager.addBlock(b1);
		
		WorldEditor.save("TEST");
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
