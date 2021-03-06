package de.Luca.GUIs;

import java.io.File;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Blocks.BlockData;
import de.Luca.Calculation.Camera;
import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GButton;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GSlider;
import de.Luca.GUI.GTextBox;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.GUI.GUIManager;
import de.Luca.GUI.TextFinishCallback;
import de.Luca.GUI.ValueChangeCallback;
import de.Luca.Loading.Loader;
import de.Luca.Main.SkyFightClient;
import de.Luca.Sound.AudioManager;
import de.Luca.Sound.Source;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.World.WorldEditor;
import de.Luca.World.WorldEditorListener;
import de.Luca.World.WorldLoader;

public class WorldEditorErstellen extends GUI {

	private GLabel Fenster1;
	private GLabel Fenster2;
	private GTextBox Name;
	private GTextBox Value;
	private GTextBox Hardness;
	private GSlider Hardness2;
	private GButton Browse1;
	private GLabel Source1;
	private GButton Browse2;
	private GButton Play1;
	private GLabel Source2;
	private GButton Browse3;
	private GButton Play2;
	private GLabel Source3;
	private GButton Browse4;
	private GButton Play3;
	private GLabel Source4;
	private GButton Exit;
	private GLabel Preview;
	private GButton Save2;
	
	private GLabel title;
	private GLabel name2;
	private GLabel value2;
	private GLabel hardness3;
	private GLabel texture;
	private GLabel breakSound;
	private GLabel playSound;
	private GLabel walkSound;
	private GLabel save3;
	private GButton loeschen;
    private GLabel loeschenText;

	private String S3 = "", S2 = "", S1 = "", S4 = "";
	
	private Source source;

	public WorldEditorErstellen() {
		super(Calc.getPixelWidth(0.5251875f), Calc.getPixelHeight(0.056f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.7728f));
		if(!GUIManager.getGUIS().contains(this)) {
			GUIManager.addGUI(this);
		}
		init();
	}

	@Override
	public void visibleUpdaet(boolean arg0) {
	}

	public void calc() {
		setX(Calc.getPixelWidth(0.5251875f));
		setY(Calc.getPixelHeight(0.056f));
		setWidth(Calc.getPixelWidth(0.4328125f));
		setHeight(Calc.getPixelHeight(0.7728f));
		
		
		Fenster1.setBounds(0, 0, Calc.getPixelWidth(0.4328125f), Calc.getPixelHeight(0.245f));
		Fenster2.setBounds(0, Calc.getPixelHeight(0.245f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.5278f));
		Name.setBounds(Calc.getPixelWidth(0.01875f), Calc.getPixelHeight(0.18111f), Calc.getPixelWidth(0.3125f),
				Calc.getPixelHeight(0.06389f));
		Value.setBounds(Calc.getPixelWidth(0.3390625f), Calc.getPixelHeight(0.2506f), Calc.getPixelWidth(0.075f),
				Calc.getPixelHeight(0.06389f));
		Hardness.setBounds(Calc.getPixelWidth(0.01875f), Calc.getPixelHeight(0.38394f), Calc.getPixelWidth(0.046875f),
				Calc.getPixelHeight(0.06389f));
		Hardness2.setBounds(Calc.getPixelWidth(0.0796875f), Calc.getPixelHeight(0.38394f),
				Calc.getPixelWidth(0.334375f), Calc.getPixelHeight(0.06389f));
		Browse1.setBounds(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.46728f), Calc.getPixelWidth(0.0640625f),
				Calc.getPixelHeight(0.04723f));
		Source1.setBounds(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.45343f), Calc.getPixelWidth(0.153125f),
				Calc.getPixelHeight(0.06389f));
		Browse2.setBounds(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.53751f), Calc.getPixelWidth(0.0640625f),
				Calc.getPixelHeight(0.04723f));
		Play1.setBounds(Calc.getPixelWidth(0.215625f), Calc.getPixelHeight(0.52566f), Calc.getPixelWidth(0.0359375f),
				Calc.getPixelHeight(0.06389f));
		Source2.setBounds(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.52566f), Calc.getPixelWidth(0.153125f),
				Calc.getPixelHeight(0.06389f));
		Browse3.setBounds(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.60774f), Calc.getPixelWidth(0.0640625f),
				Calc.getPixelHeight(0.04723f));
		Play2.setBounds(Calc.getPixelWidth(0.215625f), Calc.getPixelHeight(0.59789f), Calc.getPixelWidth(0.0359375f),
				Calc.getPixelHeight(0.06389f));
		Source3.setBounds(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.59789f), Calc.getPixelWidth(0.153125f),
				Calc.getPixelHeight(0.06389f));
		Browse4.setBounds(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.67797f), Calc.getPixelWidth(0.0640625f),
				Calc.getPixelHeight(0.04723f));
		Play3.setBounds(Calc.getPixelWidth(0.215625f), Calc.getPixelHeight(0.67012f), Calc.getPixelWidth(0.0359375f),
				Calc.getPixelHeight(0.06389f));
		Source4.setBounds(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.67012f), Calc.getPixelWidth(0.153125f),
				Calc.getPixelHeight(0.06389f));
		Exit.setBounds(Calc.getPixelWidth(0.4088125f), 0, Calc.getPixelWidth(0.024f), Calc.getPixelHeight(0.043f));
		Preview.setBounds(Calc.getPixelWidth(0.215f), Calc.getPixelHeight(0.45343f), Calc.getPixelWidth(0.0359375f),
				Calc.getPixelHeight(0.06389f));
		Save2.setBounds(Calc.getPixelWidth(0.325f), Calc.getPixelHeight(0.0194f), Calc.getPixelWidth(0.0594f),
                Calc.getPixelHeight(0.0556f));
		
		title.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.005f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		save3.setBounds(Calc.getPixelWidth(0.305f), Calc.getPixelHeight(0.004f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		name2.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.105f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		value2.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.245f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		hardness3.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.31f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		texture.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.45f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		breakSound.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.52f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		playSound.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.59f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		walkSound.setBounds(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.66f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		loeschen.setBounds(Calc.getPixelWidth(0.255f), Calc.getPixelHeight(0.0194f),
                Calc.getPixelWidth(0.0594f), Calc.getPixelHeight(0.0556f));
		loeschenText.setBounds(Calc.getPixelWidth(0.235f), Calc.getPixelHeight(0.004f), Calc.getPixelWidth(0.1f),
                Calc.getPixelHeight(0.1f));
		
	}

	public void init() {
		
		this.source = AudioManager.genSource();
		
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}
		
		loeschen = new GButton(Calc.getPixelWidth(0.255f), Calc.getPixelHeight(0.0194f),
                Calc.getPixelWidth(0.0594f), Calc.getPixelHeight(0.0556f));
		loeschen.setButtonTextures(SkyFightClient.weErstellenSave2A, SkyFightClient.weErstellenSave2B,
                SkyFightClient.weErstellenSave2C);
        this.addComponent(loeschen);

        loeschenText = new GLabel(Calc.getPixelWidth(0.235f), Calc.getPixelHeight(0.004f), Calc.getPixelWidth(0.1f),
                Calc.getPixelHeight(0.1f));
        loeschenText.setText("Delete", SkyFightClient.ConstantiaB26, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER,
                0);
        this.addComponent(loeschenText);

		title = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.005f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		title.setText("Block Erstellen", SkyFightClient.ConstantiaB40, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(title);
		
		save3 = new GLabel(Calc.getPixelWidth(0.305f), Calc.getPixelHeight(0.004f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		save3.setText("Save", SkyFightClient.ConstantiaB26, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.CENTER,
				0);
		this.addComponent(save3);
		
		name2 = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.105f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		name2.setText("Name", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(name2);
		
		value2 = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.245f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		value2.setText("Wert", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(value2);

		hardness3 = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.31f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		hardness3.setText("H�rte", SkyFightClient.ConstantiaB40, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(hardness3);
		
		texture = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.45f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		texture.setText("Texture", SkyFightClient.ConstantiaB32, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(texture);
		
		breakSound = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.52f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		breakSound.setText("Break Sound", SkyFightClient.ConstantiaB26, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(breakSound);
		
		playSound = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.59f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		playSound.setText("Place Sound", SkyFightClient.ConstantiaB26, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(playSound);
		
		
		walkSound = new GLabel(Calc.getPixelWidth(0.02f), Calc.getPixelHeight(0.66f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		walkSound.setText("Walk Sound", SkyFightClient.ConstantiaB26, new Vector4f(0.35f, 0.2f, 0.11f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(walkSound);
		
		Fenster1 = new GLabel(0, 0, Calc.getPixelWidth(0.4328125f), Calc.getPixelHeight(0.245f));
		Fenster1.setTexture(SkyFightClient.weErstellenFenster1);
		this.addComponent(Fenster1);

		Fenster2 = new GLabel(0, Calc.getPixelHeight(0.245f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.5278f));
		Fenster2.setTexture(SkyFightClient.weErstellenFenster2);
		this.addComponent(Fenster2);

		Name = new GTextBox(Calc.getPixelWidth(0.01875f), Calc.getPixelHeight(0.18111f), Calc.getPixelWidth(0.3125f),
				Calc.getPixelHeight(0.06389f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 20);
		Name.setButtonTextures(SkyFightClient.weErstellenNameA, SkyFightClient.weErstellenNameB,
				SkyFightClient.weErstellenNameC);
		Name.setSelectedTextures(SkyFightClient.weErstellenNameC, SkyFightClient.weErstellenNameA,
				SkyFightClient.weErstellenNameA);
		this.addComponent(Name);

		Value = new GTextBox(Calc.getPixelWidth(0.3390625f), Calc.getPixelHeight(0.2506f), Calc.getPixelWidth(0.075f),
				Calc.getPixelHeight(0.06389f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER,
				20);
		Value.setButtonTextures(SkyFightClient.weErstellenValueA, SkyFightClient.weErstellenValueB,
				SkyFightClient.weErstellenValueC);
		Value.setSelectedTextures(SkyFightClient.weErstellenValueC, SkyFightClient.weErstellenValueC,
				SkyFightClient.weErstellenValueC);
		this.addComponent(Value);

		Hardness = new GTextBox(Calc.getPixelWidth(0.01875f), Calc.getPixelHeight(0.38394f),
				Calc.getPixelWidth(0.046875f), Calc.getPixelHeight(0.06389f), SkyFightClient.Impact20,
				new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 10);
		Hardness.setButtonTextures(SkyFightClient.weErstellenHardnessTA, SkyFightClient.weErstellenHardnessTB,
				SkyFightClient.weErstellenHardnessTC);
		Hardness.setSelectedTextures(SkyFightClient.weErstellenValueC, SkyFightClient.weErstellenValueC,
				SkyFightClient.weErstellenValueC);
		this.addComponent(Hardness);

		Hardness2 = new GSlider(Calc.getPixelWidth(0.0796875f), Calc.getPixelHeight(0.38394f),
				Calc.getPixelWidth(0.334375f), Calc.getPixelHeight(0.06389f), false);
		Hardness2.getLabel().setTexture(SkyFightClient.weErstellenHardnessSA);
		Hardness2.getSlider().setButtonTextures(SkyFightClient.weErstellenHardnessSB,
				SkyFightClient.weErstellenHardnessSB, SkyFightClient.weErstellenHardnessSB);
		Hardness2.setSliderPercentage(16);
		this.addComponent(Hardness2);

		Browse1 = new GButton(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.46728f),
				Calc.getPixelWidth(0.0640625f), Calc.getPixelHeight(0.04723f));
		Browse1.setButtonTextures(SkyFightClient.weErstellenBrowseA, SkyFightClient.weErstellenBrowseB,
				SkyFightClient.weErstellenBrowseC);
		this.addComponent(Browse1);

		Preview = new GLabel(Calc.getPixelWidth(0.215f), Calc.getPixelHeight(0.45343f), Calc.getPixelWidth(0.0359375f),
				Calc.getPixelHeight(0.06389f));
		Preview.setTexture(SkyFightClient.weErstellenPreview);
		this.addComponent(Preview);

		Source1 = new GLabel(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.45343f),
				Calc.getPixelWidth(0.153125f), Calc.getPixelHeight(0.06389f));
		Source1.setTexture(SkyFightClient.weErstellenSourceA);
		this.addComponent(Source1);

		Browse2 = new GButton(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.53751f),
				Calc.getPixelWidth(0.0640625f), Calc.getPixelHeight(0.04723f));
		Browse2.setButtonTextures(SkyFightClient.weErstellenBrowseA, SkyFightClient.weErstellenBrowseB,
				SkyFightClient.weErstellenBrowseC);
		this.addComponent(Browse2);

		Play1 = new GButton(Calc.getPixelWidth(0.215625f), Calc.getPixelHeight(0.52566f),
				Calc.getPixelWidth(0.0359375f), Calc.getPixelHeight(0.06389f));
		Play1.setButtonTextures(SkyFightClient.weErstellenPlayA, SkyFightClient.weErstellenPlayB,
				SkyFightClient.weErstellenPlayC);
		this.addComponent(Play1);

		Source2 = new GLabel(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.52566f),
				Calc.getPixelWidth(0.153125f), Calc.getPixelHeight(0.06389f));
		Source2.setTexture(SkyFightClient.weErstellenSourceA);
		this.addComponent(Source2);

		Browse3 = new GButton(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.60774f),
				Calc.getPixelWidth(0.0640625f), Calc.getPixelHeight(0.04723f));
		Browse3.setButtonTextures(SkyFightClient.weErstellenBrowseA, SkyFightClient.weErstellenBrowseB,
				SkyFightClient.weErstellenBrowseC);
		this.addComponent(Browse3);

		Play2 = new GButton(Calc.getPixelWidth(0.215625f), Calc.getPixelHeight(0.59789f),
				Calc.getPixelWidth(0.0359375f), Calc.getPixelHeight(0.06389f));
		Play2.setButtonTextures(SkyFightClient.weErstellenPlayA, SkyFightClient.weErstellenPlayB,
				SkyFightClient.weErstellenPlayC);
		this.addComponent(Play2);

		Source3 = new GLabel(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.59789f),
				Calc.getPixelWidth(0.153125f), Calc.getPixelHeight(0.06389f));
		Source3.setTexture(SkyFightClient.weErstellenSourceA);
		this.addComponent(Source3);

		Browse4 = new GButton(Calc.getPixelWidth(0.1375f), Calc.getPixelHeight(0.67797f),
				Calc.getPixelWidth(0.0640625f), Calc.getPixelHeight(0.04723f));
		Browse4.setButtonTextures(SkyFightClient.weErstellenBrowseA, SkyFightClient.weErstellenBrowseB,
				SkyFightClient.weErstellenBrowseC);
		this.addComponent(Browse4);

		Play3 = new GButton(Calc.getPixelWidth(0.215625f), Calc.getPixelHeight(0.67012f),
				Calc.getPixelWidth(0.0359375f), Calc.getPixelHeight(0.06389f));
		Play3.setButtonTextures(SkyFightClient.weErstellenPlayA, SkyFightClient.weErstellenPlayB,
				SkyFightClient.weErstellenPlayC);
		this.addComponent(Play3);

		Source4 = new GLabel(Calc.getPixelWidth(0.2609375f), Calc.getPixelHeight(0.67012f),
				Calc.getPixelWidth(0.153125f), Calc.getPixelHeight(0.06389f));
		Source4.setTexture(SkyFightClient.weErstellenSourceA);
		this.addComponent(Source4);

		Exit = new GButton(Calc.getPixelWidth(0.4088125f), 0, Calc.getPixelWidth(0.024f), Calc.getPixelHeight(0.043f));
		Exit.setButtonTextures(SkyFightClient.weExitA, SkyFightClient.weExitA, SkyFightClient.weExitB);
		this.addComponent(Exit);
		
		Save2 = new GButton(Calc.getPixelWidth(0.325f), Calc.getPixelHeight(0.0194f), Calc.getPixelWidth(0.0594f),
                Calc.getPixelHeight(0.0556f));
        Save2.setButtonTextures(SkyFightClient.weErstellenSave2A, SkyFightClient.weErstellenSave2B,
                SkyFightClient.weErstellenSave2C);
        this.addComponent(Save2);

		addCallbacks();
	}

	private BlockData old;
	public void fill() {
		if(WorldEditorListener.selectedBlock == null) {
			S1 = "";
			S2 = "";
			S3 = "";
			S4 = "";
			old = null;
			Name.setText("");
			Value.setText("1.0");
			Hardness.setText("1.0");
			Source1.setText("", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
			Source2.setText("", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
			Source3.setText("", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
			Source4.setText("", SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
		}else {
			BlockData bd = WorldEditorListener.selectedBlock;
			old = bd;
			Name.setText(bd.getName());
			Value.setText(bd.getValue() + "");
			Hardness.setText(bd.getHardness() + "");
			S1 = bd.getBlockModel().getModel().getTexture().getFile();
			Source1.setTextCut(bd.getBlockModel().getModel().getTexture().getFile(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
			if(bd.getBreakSound() != null) {
				S2 = bd.getBreakSound().getFile();
				Source2.setTextCut(bd.getBreakSound().getFile(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
			}
			if(bd.getPlaceSound() != null) {
				S3 = bd.getPlaceSound().getFile();
				Source3.setTextCut(bd.getPlaceSound().getFile(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
			}
			if(bd.getWalkSound() != null) {
				S4 = bd.getWalkSound().getFile();
				Source4.setTextCut(bd.getWalkSound().getFile(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
			}
		}
		if(old != null) {
			Preview.setTexture(old.getBlockModel().getModel().getTexture());
		}
	}

	private void addCallbacks() {
		Exit.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldEditorErstellen.setVisible(false);
				}
			}
		});

		Exit.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldEditorErstellen.setVisible(false);
				}
			}
		});

		Hardness.addTextFinishCallback(new TextFinishCallback() {

			@Override
			public void run(String text) {
				try {
					float hardness = Float.parseFloat(Hardness.getText());
					if (hardness > 20) {
						hardness = 20;
					}
					if (hardness <= 0) {
						hardness = 1.0f;
					}
					float value = hardness / 20f;
					Hardness2.setValue(value);
				} catch (NumberFormatException e) {
					Hardness.setText("1.0");
				}
			}
		});

		Hardness2.addValueChangeCallback(new ValueChangeCallback() {

			@Override
			public void run(float value) {
				float hardness = value * 20f;
				float th = hardness * 100f;
				hardness = Math.round(th);
				hardness = hardness / 100f;
				Hardness.setText(hardness + "");
			}
		});

		Value.addTextFinishCallback(new TextFinishCallback() {

			@Override
			public void run(String arg0) {
				try {
					Float.parseFloat(Value.getText());
				} catch (NumberFormatException e) {
					Value.setText("1.0");
				}
			}
		});

		Browse1.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String root = Source1.getLabelText();
					if (root.isEmpty()) {
						root = null;
					} else {
						root = new File(Source1.getLabelText()).getParentFile().getPath();
					}
					FileDialog fd = new FileDialog("PNG (*png)", root, "png");
					int ret = fd.showToUser();
					if (ret == 0) {
						S1 = fd.getSelectedFile().getPath();
						Source1.setTextCut(fd.getSelectedFile().getPath(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
					}
				}
			}
		});

		Browse2.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String root = Source2.getLabelText();
					if (root.isEmpty()) {
						root = null;
					} else {
						root = new File(Source2.getLabelText()).getParentFile().getPath();
					}
					FileDialog fd = new FileDialog("OGG (*ogg)", root, "ogg");
					int ret = fd.showToUser();
					if (ret == 0) {
						S2 = fd.getSelectedFile().getPath();
						Source2.setTextCut(fd.getSelectedFile().getPath(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
					}
				}
			}
		});

		Browse3.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String root = Source3.getLabelText();
					if (root.isEmpty()) {
						root = null;
					} else {
						root = new File(Source3.getLabelText()).getParentFile().getPath();
					}
					FileDialog fd = new FileDialog("OGG (*ogg)", root, "ogg");
					int ret = fd.showToUser();
					if (ret == 0) {
						S3 = fd.getSelectedFile().getPath();
						Source3.setTextCut(fd.getSelectedFile().getPath(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
					}
				}
			}
		});

		Browse4.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String root = Source4.getLabelText();
					if (root.isEmpty()) {
						root = null;
					} else {
						root = new File(Source4.getLabelText()).getParentFile().getPath();
					}
					FileDialog fd = new FileDialog("OGG (*ogg)", root, "ogg");
					int ret = fd.showToUser();
					if (ret == 0) {
						S4 = fd.getSelectedFile().getPath();
						Source4.setTextCut(fd.getSelectedFile().getPath(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 10);
					}
				}
			}
		});
		
		Play1.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String file = S2;
					if(!file.isEmpty()) {
						source.setPosition(new Vector2f(0, 0));
						source.playSound(AudioManager.loadSound(file, "block"));
					}
				}
			}
		});
		
		Play2.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String file = S3;
					if(!file.isEmpty()) {
						source.setPosition(new Vector2f(0, 0));
						source.playSound(AudioManager.loadSound(file, "block"));
					}
				}
			}
		});
		
		Play3.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String file = S4;
					if(!file.isEmpty()) {
						source.setPosition(Camera.getPosition());
						source.playSound(AudioManager.loadSound(file, "block"));
					}
				}
			}
		});
		
		loeschen.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					if(old != null) {
						WorldLoader.removeBlockData(old);
						if(SkyFightClient.worldEditorAuswahl.isVisible()) {
							SkyFightClient.worldEditorAuswahl.setVisible(false);
							SkyFightClient.worldEditorAuswahl.setVisible(true);
						}
					}
				}
			}
		});
		
		Save2.addClickCallback(new ClickCallback() {
			
			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					String name = Name.getText();
					float value = Float.parseFloat(Value.getText());
					float hardness = Float.parseFloat(Hardness.getText());
					String texture = S1;
					if(name.isEmpty()) {
						new PopUp("Du musst dem Block einen Namen geben", new Vector4f(1, 0, 0, 1));
						return;
					}
					if(texture.isEmpty()) {
						new PopUp("Du musst dem Block eine Texture geben", new Vector4f(1, 0, 0, 1));
						return;
					}
					if(!new File(texture).exists()) {
						new PopUp("Die Texturdatei existiert nicht.", new Vector4f(1, 0, 0, 1));
						return;
					}
					if(!WorldEditor.validBlockName(name)) {
						if(WorldEditorListener.selectedBlock == null || !name.equals(WorldEditorListener.selectedBlock.getName())) {
							new PopUp("Es existiert bereits ein Block mit diesem Namen.", new Vector4f(1, 0, 0, 1));
							return;
						}
					}
					
					String breakSound = S2;
					String placeSound = S3;
					String walkSound = S4;
					if(breakSound.isEmpty()) {
						breakSound = null;
					}
					if(placeSound.isEmpty()) {
						placeSound = null;
					}
					if(walkSound.isEmpty()) {
						walkSound = null;
					}
					
					
					if(old != null) {
						WorldLoader.removeBlockData(old);
					}
					
					BlockData bdp;
					try {
						bdp = new BlockData(value, hardness, name, Loader.loadTexture(texture, "block"), breakSound, placeSound, walkSound);
					}catch (Exception e) {
						new PopUp("Ung�ltige Datei", new Vector4f(1, 0, 0, 1));
						return;
					}
					
					WorldLoader.addBlockData(bdp);
					SkyFightClient.worldEditorAuswahl.addBlock();
					SkyFightClient.blockSelect.addBlock();
					if(SkyFightClient.worldEditorAuswahl.isVisible()) {
						SkyFightClient.worldEditorAuswahl.setVisible(false);
						SkyFightClient.worldEditorAuswahl.setVisible(true);
					}
					new PopUp("Der Block wurde erstellt", new Vector4f(0, 1, 0, 1));
				}
			}
		});

	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}
}
