package de.Luca.GUIs;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.Luca.Blocks.BlockData;
import de.Luca.GUI.ClickCallback;
import de.Luca.GUI.GLabel;
import de.Luca.GUI.GUI;
import de.Luca.GUI.GUIComponent;
import de.Luca.GameLogic.GameManager.HOTBARSLOT;
import de.Luca.Main.SkyFightClient;
import de.Luca.Text.Paragraph.TEXT_ALIGN;
import de.Luca.Utils.Calc;
import de.Luca.Window.Window;

public class IngameOverlayGUI extends GUI {

	private GLabel statsPicture;
	private GLabel statsName;
	private GLabel statsHeart;
	private GLabel statsCoin;
	private GLabel statsHeartPoints;
	private GLabel statsCoinPoints;
	private GLabel hotbarLinks;
	private GLabel hotbarMitte1;
	private GLabel hotbarMitte2;
	private GLabel hotbarMitte3;
	private GLabel hotbarMitte4;
	private GLabel hotbarRechts;
	private GLabel hotbarHover;
	private GLabel hotbarSword;
	private GLabel hotbarBow;
	private GLabel hotbarPickaxe;
	private GLabel hotbarBlockStone;
	private GLabel hotbarBlockStoneNumber;
	private HOTBARSLOT selected = HOTBARSLOT.SWORD;
	private GLabel selectedLabel;
	private GLabel zeitRahmen;
	private GLabel zeitText;

	public IngameOverlayGUI() {
		super(0, 0, (int) Window.getWindowSize().x, (int) Window.getWindowSize().y);
		init();
	}

	public void calc() {
		statsPicture.setBounds(0, 0, Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.15f));
		statsName.setBounds(Calc.getPixelWidth(0.075f), 0, Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.08f));
		statsHeart.setBounds(Calc.getPixelWidth(0.075f), Calc.getPixelHeight(0.055f), Calc.getPixelWidth(0.019f),
				Calc.getPixelHeight(0.028f));
		statsCoin.setBounds(Calc.getPixelWidth(0.075f), Calc.getPixelHeight(0.095f), Calc.getPixelWidth(0.016f),
				Calc.getPixelHeight(0.033f));
		statsHeartPoints.setBounds(Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.035f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.08f));
		statsCoinPoints.setBounds(Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.08f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.08f));
		hotbarLinks.setBounds(Calc.getPixelWidth(0.398f), 0, Calc.getPixelWidth(0.008f), Calc.getPixelHeight(0.083f));
		hotbarMitte1.setBounds(Calc.getPixelWidth(0.406f), 0, Calc.getPixelWidth(0.047f), Calc.getPixelHeight(0.083f));
		hotbarMitte2.setBounds(Calc.getPixelWidth(0.453f), 0, Calc.getPixelWidth(0.0477f), Calc.getPixelHeight(0.083f));
		hotbarMitte3.setBounds(Calc.getPixelWidth(0.5f), 0, Calc.getPixelWidth(0.047f), Calc.getPixelHeight(0.083f));
		hotbarMitte4.setBounds(Calc.getPixelWidth(0.547f), 0, Calc.getPixelWidth(0.047f), Calc.getPixelHeight(0.083f));
		hotbarRechts.setBounds(Calc.getPixelWidth(0.594f), 0, Calc.getPixelWidth(0.008f), Calc.getPixelHeight(0.083f));
		hotbarSword.setBounds(Calc.getPixelWidth(0.406f), 0, Calc.getPixelWidth(0.047f), Calc.getPixelHeight(0.083f));
		hotbarBow.setBounds(Calc.getPixelWidth(0.453f), 0, Calc.getPixelWidth(0.0477f), Calc.getPixelHeight(0.083f));
		hotbarPickaxe.setBounds(Calc.getPixelWidth(0.5f), 0, Calc.getPixelWidth(0.047f), Calc.getPixelHeight(0.083f));
		hotbarBlockStone.setBounds(Calc.getPixelWidth(0.547f), 0, Calc.getPixelWidth(0.047f),
				Calc.getPixelHeight(0.083f));
		hotbarBlockStoneNumber.setBounds(Calc.getPixelWidth(0.575f), Calc.getPixelHeight(0.02f),
				Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.1f));
		hotbarHover.setBounds(selectedLabel.getX(), selectedLabel.getY(), selectedLabel.getWidth(),
				selectedLabel.getHeight());

		zeitRahmen.setBounds(Calc.getPixelWidth(0.909f), 0, Calc.getPixelWidth(0.091f), Calc.getPixelHeight(0.094f));
		zeitText.setBounds(Calc.getPixelWidth(0.91f), 0, Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));

	}

	public void setUsername(String username) {
		statsName.setText(username, SkyFightClient.CalibriB20, new Vector4f(0.2f, 0.18f, 0.17f, 1f), TEXT_ALIGN.LEFT,
				0);
		System.out.println("USER");
	}

	public void setHealth(float hp) {
		statsHeartPoints.setText(hp + " / 100", SkyFightClient.Alba18, new Vector4f(0, 0, 0, 1f), TEXT_ALIGN.LEFT, 0);
		System.out.println("HEALTH");
	}

	public void setCoins(float coins) {
		statsCoinPoints.setText(coins + "", SkyFightClient.Alba18, new Vector4f(0, 0, 0, 1f), TEXT_ALIGN.LEFT, 0);
		System.out.println("COINS");

	}

	public void setSelectedSlot(HOTBARSLOT slot) {
		selected = slot;
		if (slot == HOTBARSLOT.SWORD) {
			selectedLabel = hotbarSword;
		} else if (slot == HOTBARSLOT.BOW) {
			selectedLabel = hotbarBow;
		} else if (slot == HOTBARSLOT.BLOCK) {
			selectedLabel = hotbarBlockStone;
		} else if (slot == HOTBARSLOT.PICKAXE) {
			selectedLabel = hotbarPickaxe;
		}
		SkyFightClient.p.setSelected(slot);
		hotbarHover.setBounds(selectedLabel.getX(), selectedLabel.getY(), selectedLabel.getWidth(),
				selectedLabel.getHeight());

	}

	public HOTBARSLOT getSelectedSlot() {
		return selected;
	}
	
	public void setTime(String time) {
		zeitText.setText(time, SkyFightClient.Alba50, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
	}

	private void init() {
		for (GUIComponent c : getComponents()) {
			c.dispose();
			removeComponent(c);
		}

		zeitRahmen = new GLabel(Calc.getPixelWidth(0.909f), 0, Calc.getPixelWidth(0.091f), Calc.getPixelHeight(0.094f));
		zeitRahmen.setTexture(SkyFightClient.IngameOverlayZeit);
		this.addComponent(zeitRahmen);
				
		zeitText = new GLabel(Calc.getPixelWidth(0.91f), 0, Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.1f));
		zeitText.setText("15:00", SkyFightClient.Alba50, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.CENTER, 0);
		this.addComponent(zeitText);
		
		statsPicture = new GLabel(0, 0, Calc.getPixelWidth(0.2f), Calc.getPixelHeight(0.15f));
		statsPicture.setTexture(SkyFightClient.IngameOverlayStatsPicture);
		this.addComponent(statsPicture);

		statsName = new GLabel(Calc.getPixelWidth(0.075f), 0, Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.08f));
		statsName.setText("UNKNOWN", SkyFightClient.CalibriB20, new Vector4f(0.2f, 0.18f, 0.17f, 1f), TEXT_ALIGN.LEFT,
				0);
		this.addComponent(statsName);

		statsHeart = new GLabel(Calc.getPixelWidth(0.075f), Calc.getPixelHeight(0.055f), Calc.getPixelWidth(0.019f),
				Calc.getPixelHeight(0.028f));
		statsHeart.setTexture(SkyFightClient.IngameOverlayStatsHeart);
		this.addComponent(statsHeart);

		statsHeartPoints = new GLabel(Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.035f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.08f));
		statsHeartPoints.setText("--- / 100", SkyFightClient.Alba18, new Vector4f(0, 0, 0, 1f), TEXT_ALIGN.LEFT, 0);
		this.addComponent(statsHeartPoints);

		statsCoin = new GLabel(Calc.getPixelWidth(0.075f), Calc.getPixelHeight(0.095f), Calc.getPixelWidth(0.016f),
				Calc.getPixelHeight(0.033f));
		statsCoin.setTexture(SkyFightClient.IngameOverlayStatsCoin);
		this.addComponent(statsCoin);

		statsCoinPoints = new GLabel(Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.08f), Calc.getPixelWidth(0.1f),
				Calc.getPixelHeight(0.08f));
		statsCoinPoints.setText("--", SkyFightClient.Alba18, new Vector4f(0, 0, 0, 1f), TEXT_ALIGN.LEFT, 0);
		this.addComponent(statsCoinPoints);

		hotbarLinks = new GLabel(Calc.getPixelWidth(0.398f), 0, Calc.getPixelWidth(0.008f),
				Calc.getPixelHeight(0.083f));
		hotbarLinks.setTexture(SkyFightClient.IngameOverlayHotbarLinks);
		this.addComponent(hotbarLinks);

		hotbarMitte1 = new GLabel(Calc.getPixelWidth(0.406f), 0, Calc.getPixelWidth(0.047f),
				Calc.getPixelHeight(0.083f));
		hotbarMitte1.setTexture(SkyFightClient.IngameOverlayHotbarMitte);
		this.addComponent(hotbarMitte1);

		hotbarSword = new GLabel(Calc.getPixelWidth(0.406f), 0, Calc.getPixelWidth(0.047f),
				Calc.getPixelHeight(0.083f));
		hotbarSword.setTexture(SkyFightClient.IngameOverlayHotbarSword);
		hotbarSword.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					setSelectedSlot(HOTBARSLOT.SWORD);
				}
			}
		});
		selectedLabel = hotbarSword;
		this.addComponent(hotbarSword);

		hotbarMitte2 = new GLabel(Calc.getPixelWidth(0.453f), 0, Calc.getPixelWidth(0.0477f),
				Calc.getPixelHeight(0.083f));
		hotbarMitte2.setTexture(SkyFightClient.IngameOverlayHotbarMitte);
		this.addComponent(hotbarMitte2);

		hotbarBow = new GLabel(Calc.getPixelWidth(0.453f), 0, Calc.getPixelWidth(0.0477f), Calc.getPixelHeight(0.083f));
		hotbarBow.setTexture(SkyFightClient.IngameOverlayHotbarBow);
		hotbarBow.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					setSelectedSlot(HOTBARSLOT.BOW);
				}
			}
		});
		this.addComponent(hotbarBow);

		hotbarMitte3 = new GLabel(Calc.getPixelWidth(0.5f), 0, Calc.getPixelWidth(0.047f), Calc.getPixelHeight(0.083f));
		hotbarMitte3.setTexture(SkyFightClient.IngameOverlayHotbarMitte);
		this.addComponent(hotbarMitte3);

		hotbarPickaxe = new GLabel(Calc.getPixelWidth(0.5f), 0, Calc.getPixelWidth(0.047f),
				Calc.getPixelHeight(0.083f));
		hotbarPickaxe.setTexture(SkyFightClient.IngameOverlayHotbarPickaxe);
		hotbarPickaxe.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					setSelectedSlot(HOTBARSLOT.PICKAXE);
				}
			}
		});
		this.addComponent(hotbarPickaxe);

		hotbarMitte4 = new GLabel(Calc.getPixelWidth(0.547f), 0, Calc.getPixelWidth(0.047f),
				Calc.getPixelHeight(0.083f));
		hotbarMitte4.setTexture(SkyFightClient.IngameOverlayHotbarMitte);
		this.addComponent(hotbarMitte4);

		hotbarBlockStone = new GLabel(Calc.getPixelWidth(0.547f), 0, Calc.getPixelWidth(0.047f),
				Calc.getPixelHeight(0.083f));
		hotbarBlockStone.setTexture(SkyFightClient.IngameOverlayHotbarBlockStone);
		hotbarBlockStone.addClickCallback(new ClickCallback() {

			@Override
			public void run(GUIComponent component, int key, int action, int mouseX, int mouseY) {
				if (key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
					setSelectedSlot(HOTBARSLOT.BLOCK);
				} else if (key == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_RELEASE) {
					SkyFightClient.blockSelect.setVisible(true);
				}
			}
		});
		this.addComponent(hotbarBlockStone);

		hotbarBlockStoneNumber = new GLabel(Calc.getPixelWidth(0.575f), Calc.getPixelHeight(0.02f),
				Calc.getPixelWidth(0.1f), Calc.getPixelHeight(0.1f));
		hotbarBlockStoneNumber.setText("--", SkyFightClient.Alba18, new Vector4f(1f, 1f, 1f, 1f), TEXT_ALIGN.LEFT, 0);
		this.addComponent(hotbarBlockStoneNumber);

		hotbarRechts = new GLabel(Calc.getPixelWidth(0.594f), 0, Calc.getPixelWidth(0.008f),
				Calc.getPixelHeight(0.083f));
		hotbarRechts.setTexture(SkyFightClient.IngameOverlayHotbarRechts);
		this.addComponent(hotbarRechts);

		hotbarHover = new GLabel(selectedLabel.getX(), selectedLabel.getY(), selectedLabel.getWidth(),
				selectedLabel.getHeight());
		hotbarHover.setTexture(SkyFightClient.IngameOverlayHotbarHover);
		this.addComponent(hotbarHover);

		System.out.println("INIT");

	}

	private BlockData selectedBlock;

	public void setBlock(BlockData data) {
		hotbarBlockStoneNumber.setText(data.getValue() + "", SkyFightClient.Alba18, new Vector4f(1f, 1f, 1f, 1f),
				TEXT_ALIGN.LEFT, 0);
		System.out.println("BLOCK");
		hotbarBlockStone.setTexture(data.getBlockModel().getModel().getTexture());
		selectedBlock = data;
	}

	public BlockData getSelectedBlock() {
		return selectedBlock;
	}

	@Override
	public void visibleUpdaet(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowResize(int arg0, int arg1) {
		calc();
	}
}
