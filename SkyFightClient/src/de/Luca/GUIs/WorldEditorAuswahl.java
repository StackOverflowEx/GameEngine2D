package de.Luca.GUIs;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Blocks.BlockData;
import de.Luca.GUI.CharInputCallback;
import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GButton;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GPanel;
import de.Luca.GUI.GScrollPanel;
import de.Luca.GUI.GTextBox;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.GUI.GScrollPanel.SLIDER_POSITION;
import de.Luca.GUI.GTextBox.INPUT_MODE;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.World.WorldEditorListener;
import de.Luca.World.WorldLoader;

public class WorldEditorAuswahl extends GUI {
	
	private GScrollPanel scroll;
	private GLabel UmrahmungA;
	private GTextBox Suchen;
	private GLabel UmrahmungB;
	private GButton Gelb;
	private GLabel UmrahmungC;
	private GButton Exit;
	private GButton Gruen;
	private GLabel title;
	private GLabel bearbeiten;
	private GLabel erstellen;
	
	public WorldEditorAuswahl() {
		super(Calc.getPixelWidth(0.042f), Calc.getPixelHeight(0.056f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.7728f));
		init();
	}

	@Override
	public void visibleUpdaet(boolean arg0) {
	}

	public GScrollPanel getScrollPanel() {
					
		scroll = new GScrollPanel(0, Calc.getPixelHeight(0.14167f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.44723f));
		
		int width = Calc.getPixelWidth(0.4328125f);
		int spacing = Calc.getPixelWidth(0.0075f);
		int rowAmount = (width + 2*spacing) / Calc.getPixelWidth(0.05f);
		if(rowAmount > 1) {
			rowAmount -= 1;
		}
		
		int added = 0;
		GPanel row = new GPanel(0 ,0, 0, 0);
		for (BlockData bdp : WorldLoader.getBlockData().values()) {
			
			if(!bdp.getName().toLowerCase().contains(Suchen.getText().toLowerCase())) {
				continue;
			}
			
			int h = (int) (Calc.getPixelWidth(0.05f));
			int w = Calc.getPixelWidth(0.05f);
			GButton block = new GButton(spacing*2, spacing, w, h);
			GButton name = new GButton(spacing*2, h + spacing, w, Calc.getPixelHeight(0.05f));
			name.setTextCut(bdp.getName(), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.CENTER, 0);
			name.setColor(new Vector4f(0, 0, 0, 0));
			System.out.println(bdp.getBlockModel().getModel().getTexture().getTextureID());
			block.setTexture(bdp.getBlockModel().getModel().getTexture());
			
			GPanel all = new GPanel(0, 0, w+2*spacing, h + Calc.getPixelHeight(0.05f) + spacing);
			all.addComponent(block);
			all.addComponent(name);
			
			block.addClickCallback(new ClickCallback() {
				
				@Override
				public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
					if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
						WorldEditorListener.selectedBlock = bdp;
						SkyFightClient.worldEditorErstellen.fill();
					}
				}
			});
			name.addClickCallback(new ClickCallback() {
				
				@Override
				public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
					if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
						WorldEditorListener.selectedBlock = bdp;
						SkyFightClient.worldEditorErstellen.fill();
					}
				}
			});
			
			if(rowAmount > added) {
				all.setX((spacing + w) * added);
				row.addComponent(all);
			}else {
				scroll.addItem(row);
				added = 0;
				row = new GPanel(0 ,0, 0, 0);
				all.setX((spacing + w) * added);
				row.addComponent(all);
			}
			added++;
		}
		if(added != 0) {
			scroll.addItem(row);
		}
		
		scroll.setSlider(SLIDER_POSITION.RIGHT, Calc.getPixelWidth(0.02f));
		scroll.getSliderLR().getLabel().setTexture(SkyFightClient.AuswahlScrollingRahmen);
		scroll.getSliderLR().getSlider().setButtonTextures(SkyFightClient.AuswahlScrollingKnopfA,
				SkyFightClient.AuswahlScrollingKnopfB, SkyFightClient.AuswahlScrollingKnopfC);
		
		return scroll;
	}
	
	public void addBlock() {
//		scroll.dispose();
		scroll.setVisible(false);
		removeComponent(scroll);
		this.addComponent(getScrollPanel());
	}

	public void calc() {
		setX(Calc.getPixelWidth(0.042f));
		setY(Calc.getPixelHeight(0.056f));
		setWidth(Calc.getPixelWidth(0.4328125f));
		setHeight(Calc.getPixelHeight(0.7728f));
		
		
		UmrahmungA.setBounds(0, 0, Calc.getPixelWidth(0.4328125f), Calc.getPixelHeight(0.09167f));
		Suchen.setBounds(0, Calc.getPixelHeight(0.09167f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.05f));
		UmrahmungB.setBounds(0, Calc.getPixelHeight(0.14167f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.44723f));
		Gelb.setBounds(0, Calc.getPixelHeight(0.5889f), Calc.getPixelWidth(0.4328125f),
                Calc.getPixelHeight(0.0725f));
		Gruen.setBounds(0, Calc.getPixelHeight(0.6614f), Calc.getPixelWidth(0.4328125f),
                Calc.getPixelHeight(0.0725f));
		UmrahmungC.setBounds(0, Calc.getPixelHeight(0.7339f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.0389f));
		Exit.setBounds(Calc.getPixelWidth(0.4088125f), 0, Calc.getPixelWidth(0.024f),
				Calc.getPixelHeight(0.043f));
		
//		scroll.setBounds(0, Calc.getPixelHeight(0.14167f), Calc.getPixelWidth(0.4328125f),
//				Calc.getPixelHeight(0.44723f));
		title.setBounds(Calc.getPixelWidth(0.17f), Calc.getPixelHeight(0.005f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		bearbeiten.setBounds(Calc.getPixelWidth(0.17f), Calc.getPixelHeight(0.58f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		erstellen.setBounds(Calc.getPixelWidth(0.17f), Calc.getPixelHeight(0.66f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		
		scroll.dispose();
		this.addComponent(getScrollPanel());

	}
	
	public void init() {
		
		for (GUIComponent c : getComponents()) {
			c.dispose();
		}
				
		title = new GLabel(Calc.getPixelWidth(0.17f), Calc.getPixelHeight(0.005f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		title.setText("Block Ausw�hlen", SkyFightClient.ConstantiaB40, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(title);
		
		bearbeiten = new GLabel(Calc.getPixelWidth(0.17f), Calc.getPixelHeight(0.58f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		bearbeiten.setText("Block Bearbeiten", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(bearbeiten);
		
		erstellen = new GLabel(Calc.getPixelWidth(0.17f), Calc.getPixelHeight(0.66f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		erstellen.setText("Block Erstellen", SkyFightClient.ConstantiaB32, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(erstellen);
		
		UmrahmungA = new GLabel(0, 0, Calc.getPixelWidth(0.4328125f), Calc.getPixelHeight(0.09167f));
		UmrahmungA.setTexture(SkyFightClient.weAuswahlUmrahmungA);
		this.addComponent(UmrahmungA);

		Suchen = new GTextBox(0, Calc.getPixelHeight(0.09167f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.05f), SkyFightClient.Impact20, new Vector4f(0, 0, 0, 1), TEXT_ALIGN.LEFT, 20);
		Suchen.setButtonTextures(SkyFightClient.weAuswahlSuchenA, SkyFightClient.weAuswahlSuchenB,
				SkyFightClient.weAuswahlSuchenC);
		Suchen.setSelectedTextures(SkyFightClient.weAuswahlSuchenC, SkyFightClient.weAuswahlSuchenC,
				SkyFightClient.weAuswahlSuchenC);
		this.addComponent(Suchen);

		UmrahmungB = new GLabel(0, Calc.getPixelHeight(0.14167f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.44723f));
		UmrahmungB.setTexture(SkyFightClient.weAuswahlUmrahmungB);
		this.addComponent(UmrahmungB);
		
		Gelb = new GButton(0, Calc.getPixelHeight(0.5889f), Calc.getPixelWidth(0.4328125f),
                Calc.getPixelHeight(0.0725f));
        Gelb.setButtonTextures(SkyFightClient.weAuswahlButtonA, SkyFightClient.weAuswahlButtonB,
                SkyFightClient.weAuswahlButtonC);
        this.addComponent(Gelb);

        Gruen = new GButton(0, Calc.getPixelHeight(0.6614f), Calc.getPixelWidth(0.4328125f),
                Calc.getPixelHeight(0.0725f));
        Gruen.setButtonTextures(SkyFightClient.weAuswahlGruenA, SkyFightClient.weAuswahlGruenB,
                SkyFightClient.weAuswahlGruenC);
        this.addComponent(Gruen);

		UmrahmungC = new GLabel(0, Calc.getPixelHeight(0.7339f), Calc.getPixelWidth(0.4328125f),
				Calc.getPixelHeight(0.0389f));
		UmrahmungC.setTexture(SkyFightClient.weAuswahlUmrahmungC);
		this.addComponent(UmrahmungC);

		Exit = new GButton(Calc.getPixelWidth(0.4088125f), 0, Calc.getPixelWidth(0.024f),
				Calc.getPixelHeight(0.043f));
		Exit.setButtonTextures(SkyFightClient.weExitA, SkyFightClient.weExitA, SkyFightClient.weExitB);
		this.addComponent(Exit);
		
		this.addComponent(getScrollPanel());
		
		Exit.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldEditorAuswahl.setVisible(false);
				}
			}
		});
		
		Gelb.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.worldEditorErstellen.fill();
					SkyFightClient.worldEditorErstellen.setVisible(true);
				}
			}
		});
		
		Gruen.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					WorldEditorListener.selectedBlock = null;
					SkyFightClient.worldEditorErstellen.fill();
					SkyFightClient.worldEditorErstellen.setVisible(true);
				}
			}
		});
		
		Suchen.addCharInputCallback(new CharInputCallback() {
			
			@Override
			public void run(String arg0, INPUT_MODE arg1) {
				if(arg1 == INPUT_MODE.TEXT || arg1 == INPUT_MODE.DELETE) {
					addBlock();
					setVisible(false);
					setVisible(true);
				}
			}
		});
	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}
}
