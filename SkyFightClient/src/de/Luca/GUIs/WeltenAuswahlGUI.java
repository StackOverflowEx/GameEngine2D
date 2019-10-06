package de.Luca.GUIs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GButton;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GPanel;
import de.Luca.GUI.GScrollPanel;
import de.Luca.GUI.GScrollPanel.SLIDER_POSITION;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.Window.Window;
import de.Luca.World.WorldEditor;

public class WeltenAuswahlGUI extends GUI {

	private GLabel background;
	private GButton erstellenKnopf;
	private GLabel erstellenText;
	private GButton back;
//	private GLabel rahmen;
//	private GLabel preview;
//	private GLabel eingabeG;
//	private GLabel eingabeK;
//	private GLabel eingabeK2;
//	private GLabel name;
//	private GLabel bearbeitungsdatum;
//	private GLabel dateigroesse;
//	private GSlider scrolling;

	private GScrollPanel panel;

	public WeltenAuswahlGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		init();
	}

	public void calc() {
		background.setBounds(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		erstellenKnopf.setBounds(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		erstellenText.setBounds(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		back.setBounds(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		panel.dispose();
		setUpScrollpanel();
	}

	private void setUpScrollpanel() {
		panel = new GScrollPanel(Calc.getPixelWidth(0.125f), Calc.getPixelHeight(0.05f), Calc.getPixelWidth(0.842f),
				Calc.getPixelHeight(0.9f));
//		int width = Calc.getPixelWidth(0.75f);
//		int height = Calc.getPixelHeight(0.28f);

		int wholeWidth = Calc.getPixelWidth(0.842f);
		int wholeHeight = Calc.getPixelHeight(0.3f);

		File customMaps = new File(SkyFightClient.root + "/maps/own");
		int i = 0;
		for (File map : customMaps.listFiles()) {
			if (map.isDirectory()) {
				String mapName = map.getName();
				long bytes = getFolderSize(map);
				float kb = bytes / 1024f;
				
				kb = kb * 10;
				kb = Math.round(kb) / 10f;
				
				String date = "";
				try {
					FileTime ft = Files.getLastModifiedTime(new File(map.getPath() + "/mapdata/blocks.txt").toPath(),
							LinkOption.NOFOLLOW_LINKS);
					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
					date = dateFormat.format(ft.toMillis());
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
				File previewTexture = new File(map.getPath() + "/preview.png");

				GPanel worldPanel = new GPanel(0, i * (wholeHeight), wholeWidth, wholeHeight);

				GLabel rahmen = new GLabel(0, 0, Calc.getPixelWidth(0.75f), Calc.getPixelHeight(0.28f));
				rahmen.setTexture(SkyFightClient.AuswahlRahmen);
				worldPanel.addComponent(rahmen);

				GLabel preview = new GLabel(Calc.getPixelWidth(0.015f), Calc.getPixelHeight(0.03f),
						Calc.getPixelWidth(0.125f), Calc.getPixelHeight(0.22f));
//				preview.setTexture(SkyFightClient.AuswahlPreview);
				preview.setTexture(Loader.loadTexture(previewTexture.getPath(), "gui"));
				worldPanel.addComponent(preview);

				GLabel name = new GLabel(Calc.getPixelWidth(0.155f), Calc.getPixelHeight(0.02f),
						Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.1f));
				name.setText("Name", SkyFightClient.ConstantiaB38, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
						TEXT_ALIGN.LEFT, 0);
				worldPanel.addComponent(name);

				GLabel eingabeG = new GLabel(Calc.getPixelWidth(0.255f), Calc.getPixelHeight(0.025f),
						Calc.getPixelWidth(0.48f), Calc.getPixelHeight(0.07f));
				eingabeG.setTexture(SkyFightClient.AuswahlEingabeG);
				eingabeG.setText(mapName, SkyFightClient.ConstantiaB38, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
						TEXT_ALIGN.LEFT, 10);
				worldPanel.addComponent(eingabeG);

				GLabel bearbeitungsdatum = new GLabel(Calc.getPixelWidth(0.155f), Calc.getPixelHeight(0.1f),
						Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.1f));
				bearbeitungsdatum.setText("Bearbeitungsdatum", SkyFightClient.ConstantiaB38,
						new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT, 0);
				worldPanel.addComponent(bearbeitungsdatum);

				GLabel eingabeK = new GLabel(Calc.getPixelWidth(0.435f), Calc.getPixelHeight(0.105f),
						Calc.getPixelWidth(0.3f), Calc.getPixelHeight(0.07f));
				eingabeK.setTexture(SkyFightClient.AuswahlEingabeG);
				eingabeK.setText(date, SkyFightClient.ConstantiaB38, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
						TEXT_ALIGN.LEFT, 10);
				worldPanel.addComponent(eingabeK);

				GLabel dateigroesse = new GLabel(Calc.getPixelWidth(0.155f), Calc.getPixelHeight(0.18f),
						Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.1f));
				dateigroesse.setText("Dateigröße", SkyFightClient.ConstantiaB38, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
						TEXT_ALIGN.LEFT, 0);
				worldPanel.addComponent(dateigroesse);
				
				GLabel eingabeK2 = new GLabel(Calc.getPixelWidth(0.435f), Calc.getPixelHeight(0.185f), Calc.getPixelWidth(0.3f),
						Calc.getPixelHeight(0.07f));
				eingabeK2.setTexture(SkyFightClient.AuswahlEingabeG);
				eingabeK2.setText(kb + " KB", SkyFightClient.ConstantiaB38, new Vector4f(0.35f, 0.2f, 0.11f, 1f),
						TEXT_ALIGN.LEFT, 10);
				worldPanel.addComponent(eingabeK2);
				
				worldPanel.addClickCallback(new ClickCallback() {
					
					@Override
					public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
						if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
							WorldEditor.start(map.getPath());
							SkyFightClient.worldSelctGUI.setVisible(false);
						}
					}
				});

				panel.addItem(worldPanel);
			}
		}
		
		panel.setSlider(SLIDER_POSITION.RIGHT, 40);
		panel.getSliderLR().getLabel().setTexture(SkyFightClient.AuswahlScrollingRahmen);
		panel.getSliderLR().getSlider().setButtonTextures(SkyFightClient.AuswahlScrollingKnopfA,
				SkyFightClient.AuswahlScrollingKnopfB, SkyFightClient.AuswahlScrollingKnopfC);
		panel.getSliderLR().setSliderPercentage(32);
		
		this.addComponent(panel);
	}

	private long getFolderSize(File dir) {
		long size = 0;
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				System.out.println(file.getName() + " " + file.length());
				size += file.length();
			} else
				size += getFolderSize(file);
		}
		return size;
	}

	private void init() {
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		background = new GLabel(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		background.setTexture(SkyFightClient.MainBackground);
		this.addComponent(background);
		
		erstellenKnopf = new GButton(Calc.getPixelWidth(0.0063f), Calc.getPixelHeight(0.089f),
				Calc.getPixelWidth(0.113f), Calc.getPixelHeight(0.2f));
		erstellenKnopf.setButtonTextures(SkyFightClient.AuswahlErstellenA, SkyFightClient.AuswahlErstellenB,
				SkyFightClient.AuswahlErstellenC);
		this.addComponent(erstellenKnopf);
		
		erstellenText = new GLabel(Calc.getPixelWidth(0.014f), Calc.getPixelHeight(0.14f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		erstellenText.setText("Neue" + "\n" + "Welt" + "\n" + "Erstellen", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER, 0);
		this.addComponent(erstellenText);
		
		back = new GButton(0, 0, Calc.getPixelWidth(0.056f), Calc.getPixelHeight(0.047f));
        back.setButtonTextures(SkyFightClient.weOverlayBackA, SkyFightClient.weOverlayBackB,
                SkyFightClient.weOverlayBackC);
        this.addComponent(back);
        
        back.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldSelctGUI.setVisible(false);
					SkyFightClient.mainGUI.setVisible(true);
				}
			}
		});

		erstellenKnopf.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldSelctGUI.setVisible(false);
					WorldEditor.start();
				}
			}
		});
		
		setUpScrollpanel();

	}

	@Override
	public void visibleUpdaet(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowResize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
