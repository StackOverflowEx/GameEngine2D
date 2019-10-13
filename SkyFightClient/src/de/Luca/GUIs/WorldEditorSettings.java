package de.Luca.GUIs;

import java.io.File;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GButton;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GTextBox;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.World.WorldEditor;

public class WorldEditorSettings extends GUI {

	private GLabel Fenster;
	private GTextBox Mapname;
	private GLabel Preview;
	private GLabel Source;
	private GButton Browse;
	private GButton Gelb;
	private GButton Rot;
	private GButton Gelb2;
	private GButton Rot2;
	private GButton Exit;
	
	private GLabel title;
	private GLabel mapname2;
	private GLabel background;
	private GLabel gelb1;
	private GLabel gelb2;
	private GLabel rot1;
	private GLabel rot2;
	
	private String S = "";

	public WorldEditorSettings() {
		super(Calc.getPixelWidth(0.28359375f), Calc.getPixelHeight(0.056f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.7728f));
		init();
	}

	@Override
	public void visibleUpdaet(boolean arg0) {
	}
	
	public String getName() {
		return Mapname.getText();
	}
	
	public String getBackground() {
		return S;
	}
	
	public void calc() {
		setX(Calc.getPixelWidth(0.28359375f));
		setY(Calc.getPixelHeight(0.056f));
		setWidth(Calc.getPixelWidth(0.4328125f));
		setHeight(Calc.getPixelHeight(0.7728f));	
		
		
		
		Fenster.setBounds(0, 0, Calc.getPixelWidth(0.4328125f), Calc.getPixelHeight(0.7728f));
		Mapname.setBounds(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.181f), Calc.getPixelWidth(0.358f),
				Calc.getPixelHeight(0.06389f));
		Preview.setBounds(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.339f), Calc.getPixelWidth(0.091f),
				Calc.getPixelHeight(0.161f));
		Source.setBounds(Calc.getPixelWidth(0.138f), Calc.getPixelHeight(0.339f), Calc.getPixelWidth(0.258f),
				Calc.getPixelHeight(0.06389f));
		Browse.setBounds(Calc.getPixelWidth(0.138f), Calc.getPixelHeight(0.431f), Calc.getPixelWidth(0.0640625f),
				Calc.getPixelHeight(0.04723f));
		Gelb.setBounds(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.561f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Rot.setBounds(Calc.getPixelWidth(0.23640625f), Calc.getPixelHeight(0.561f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Gelb2.setBounds(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.652f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Rot2.setBounds(Calc.getPixelWidth(0.23640625f), Calc.getPixelHeight(0.652f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Exit.setBounds(Calc.getPixelWidth(0.4088125f), 0, Calc.getPixelWidth(0.024f), Calc.getPixelHeight(0.043f));
		
		title.setBounds(Calc.getPixelWidth(0.165f), Calc.getPixelHeight(0.005f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		mapname2.setBounds(Calc.getPixelWidth(0.037f), Calc.getPixelHeight(0.1f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		background.setBounds(Calc.getPixelWidth(0.037f), Calc.getPixelHeight(0.26f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		gelb1.setBounds(Calc.getPixelWidth(0.065f), Calc.getPixelHeight(0.55f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		rot1.setBounds(Calc.getPixelWidth(0.265f), Calc.getPixelHeight(0.55f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		gelb2.setBounds(Calc.getPixelWidth(0.065f), Calc.getPixelHeight(0.645f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		rot2.setBounds(Calc.getPixelWidth(0.265f), Calc.getPixelHeight(0.645f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		
	}
	
	public void setup(String name, String backgroundTexture) {
		Mapname.setText(name);
		Preview.setTexture(Loader.loadTexture(backgroundTexture, "gui"));
		Source.setTextCut(backgroundTexture, SkyFightClient.Impact20,
				new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
	}

	public void setup(String name) {
		Mapname.setText(name);
	}

	
	private void init() {
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		title = new GLabel(Calc.getPixelWidth(0.165f), Calc.getPixelHeight(0.005f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		title.setText("Welteinstellungen", SkyFightClient.ConstantiaB40, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER,
				0);
		this.addComponent(title);
		
		mapname2 = new GLabel(Calc.getPixelWidth(0.037f), Calc.getPixelHeight(0.1f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		mapname2.setText("Mapname", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(mapname2);
		
		background = new GLabel(Calc.getPixelWidth(0.037f), Calc.getPixelHeight(0.26f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		background.setText("Hintergrund", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(background);
		
		gelb1 = new GLabel(Calc.getPixelWidth(0.065f), Calc.getPixelHeight(0.55f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		gelb1.setText("Spawn 1 Setzen", SkyFightClient.ConstantiaB26, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER,
				0);
		this.addComponent(gelb1);
		
		rot1 = new GLabel(Calc.getPixelWidth(0.265f), Calc.getPixelHeight(0.55f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		rot1.setText("Spawn 2 Setzen", SkyFightClient.ConstantiaB26, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER,
				0);
		this.addComponent(rot1);
		
		gelb2 = new GLabel(Calc.getPixelWidth(0.065f), Calc.getPixelHeight(0.645f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		gelb2.setText("Zu Spawn 1 Gehen", SkyFightClient.ConstantiaB26, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER,
				0);
		this.addComponent(gelb2);
		
		rot2 = new GLabel(Calc.getPixelWidth(0.265f), Calc.getPixelHeight(0.645f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		rot2.setText("Zu Spawn 2 Gehen", SkyFightClient.ConstantiaB26, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER,
				0);
		this.addComponent(rot2);
		
		Fenster = new GLabel(0, 0, Calc.getPixelWidth(0.4328125f), Calc.getPixelHeight(0.7728f));
		Fenster.setTexture(SkyFightClient.weSettingsRahmen);
		this.addComponent(Fenster);

		Mapname = new GTextBox(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.181f), Calc.getPixelWidth(0.358f),
				Calc.getPixelHeight(0.06389f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
		Mapname.setButtonTextures(SkyFightClient.weSettingsMapnameA, SkyFightClient.weSettingsMapnameB,
				SkyFightClient.weSettingsMapnameB);
		Mapname.setSelectedTextures(SkyFightClient.weSettingsMapnameB, SkyFightClient.weSettingsMapnameB,
				SkyFightClient.weSettingsMapnameB);
		this.addComponent(Mapname);

		Preview = new GLabel(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.339f), Calc.getPixelWidth(0.091f),
				Calc.getPixelHeight(0.161f));
		Preview.setTexture(SkyFightClient.weSettingsPreview);
		this.addComponent(Preview);

		Source = new GLabel(Calc.getPixelWidth(0.138f), Calc.getPixelHeight(0.339f), Calc.getPixelWidth(0.258f),
				Calc.getPixelHeight(0.06389f));
		Source.setTexture(SkyFightClient.weSettingsSourceA);
		this.addComponent(Source);

		Browse = new GButton(Calc.getPixelWidth(0.138f), Calc.getPixelHeight(0.431f), Calc.getPixelWidth(0.0640625f),
				Calc.getPixelHeight(0.04723f));
		Browse.setButtonTextures(SkyFightClient.weErstellenBrowseA, SkyFightClient.weErstellenBrowseB,
				SkyFightClient.weErstellenBrowseC);
		this.addComponent(Browse);

		Gelb = new GButton(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.561f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Gelb.setButtonTextures(SkyFightClient.weSettingsGelbA, SkyFightClient.weSettingsGelbB,
				SkyFightClient.weSettingsGelbC);
		this.addComponent(Gelb);

		Rot = new GButton(Calc.getPixelWidth(0.23640625f), Calc.getPixelHeight(0.561f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Rot.setButtonTextures(SkyFightClient.weSettingsRotA, SkyFightClient.weSettingsRotB,
				SkyFightClient.weSettingsRotC);
		this.addComponent(Rot);

		Gelb2 = new GButton(Calc.getPixelWidth(0.03740625f), Calc.getPixelHeight(0.652f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Gelb2.setButtonTextures(SkyFightClient.weSettingsGelbA, SkyFightClient.weSettingsGelbB,
				SkyFightClient.weSettingsGelbC);
		this.addComponent(Gelb2);

		Rot2 = new GButton(Calc.getPixelWidth(0.23640625f), Calc.getPixelHeight(0.652f), Calc.getPixelWidth(0.159f),
				Calc.getPixelHeight(0.077f));
		Rot2.setButtonTextures(SkyFightClient.weSettingsRotA, SkyFightClient.weSettingsRotB,
				SkyFightClient.weSettingsRotC);
		this.addComponent(Rot2);

		Exit = new GButton(Calc.getPixelWidth(0.4088125f), 0, Calc.getPixelWidth(0.024f), Calc.getPixelHeight(0.043f));
		Exit.setButtonTextures(SkyFightClient.weExitA, SkyFightClient.weExitA, SkyFightClient.weExitB);
		this.addComponent(Exit);

		Browse.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String rootDir = null;
					if (!Source.getLabelText().isEmpty()) {
						File f = new File(Source.getLabelText());
						rootDir = f.getParentFile().getPath();
					}
					FileDialog fd = new FileDialog("PNG (*.png)", rootDir, "png");
					int ret = fd.showToUser();
					if (ret == 0) {
						S = fd.getSelectedFile().getPath();
						Source.setTextCut(fd.getSelectedFile().getPath(), SkyFightClient.Impact20,
								new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
						WorldEditor.setBackground(fd.getSelectedFile().getPath());
					}
				}
			}
		});

		Exit.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldEditorSettings.setVisible(false);
				}
			}
		});

		Rot.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					WorldEditor.setSettingSpawn(2);
					SkyFightClient.worldEditorSettings.setVisible(false);
				}
			}
		});

		Rot2.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.p.setPosition(WorldEditor.getSpawn2());
					SkyFightClient.worldEditorSettings.setVisible(false);
				}
			}
		});

		Gelb.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					WorldEditor.setSettingSpawn(1);
					SkyFightClient.worldEditorSettings.setVisible(false);
				}
			}
		});

		Gelb2.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.p.setPosition(WorldEditor.getSpawn1());
					SkyFightClient.worldEditorSettings.setVisible(false);
				}
			}
		});
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}

}
