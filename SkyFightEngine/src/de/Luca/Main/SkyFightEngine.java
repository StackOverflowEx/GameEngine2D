package de.Luca.Main;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import de.Luca.Blocks.Block;
import de.Luca.Blocks.BlockData;
import de.Luca.Blocks.BlockManager;
import de.Luca.Calculation.BeatHandler;
import de.Luca.Calculation.MainBeat;
import de.Luca.Calculation.PlayerCalc;
import de.Luca.Effects.EffectManager;
import de.Luca.Entities.EntityManager;
import de.Luca.Entities.Player;
import de.Luca.EventManager.EventManager;
import de.Luca.GIF.Animation;
import de.Luca.GUI.GTextBox;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIListener;
import de.Luca.GUI.GUIManager;
import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;
import de.Luca.Rendering.MasterRenderer;
import de.Luca.Rendering.RenderLoop;
import de.Luca.Sound.AudioManager;
import de.Luca.Text.TextManager;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.DefaultKeyListener;
import de.Luca.Window.Window;

public class SkyFightEngine {
		
	public static void init(BeatHandler bh) {
		Window.init(1280, 720, "SkyFight");
		
		GL11.glGenTextures();
		
		AudioManager.init();
		TextManager.init();
		GUIManager.init();
		BlockManager.init();
		EntityManager.init();
		EffectManager.init();
		EventManager.registerEvent(new GUIListener());
		EventManager.registerEvent(new DefaultKeyListener());
		
		new MasterRenderer(new RenderLoop());
		MasterRenderer.begin();
		
		TextManager.generateFont("C:\\Windows\\Fonts\\Arial.ttf", 20f, "Arial", false, false);	
		
		Texture test = Loader.loadTexture("D:\\Icons\\test.png");

		BlockData bd = new BlockData(1f, 1f, "Dirt", test);
		Block b = new Block(bd, new Vector2f(-2, 2));
		Block b1 = new Block(bd, new Vector2f(0, 2));
		Block b2 = new Block(bd, new Vector2f(1, 1));
//		Block b3 = new Block(bd, new Vector2f(-1, -1));
		BlockManager.addBlock(b);
		BlockManager.addBlock(b1);
		BlockManager.addBlock(b2);
//		BlockManager.addBlock(b3);
		
		Player p = new Player(test, test, new Vector2f(1, 3));
		PlayerCalc.init(p);
		EntityManager.addEntity(p);
		
		Animation a = new Animation("D:\\Downloads\\giphy.gif");
		p.startAnimation(0, a);
		
		GUI ui = new GUI(10, 10, 300, 50);
		GTextBox text = new GTextBox(0, 0, 300, 50, TextManager.getFont("Arial"), new Vector4f(1, 1, 1, 1), TEXT_ALIGN.LEFT, 10);
		ui.addComponent(text);
		ui.setVisible(true);
				
		MainBeat.init(bh);
	}
	
	public static void stop() {
		AudioManager.cleanUP();
		TextManager.cleanUP();
		MasterRenderer.cleanUP();
		GUIManager.cleanUP();
		Loader.cleanUP();
		Window.closeWindow();
	}

}
