package de.Luca.Main;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.Luca.Connection.Connection;
import de.Luca.Entities.Player;
import de.Luca.GUIs.LoadingGUI;
import de.Luca.GUIs.LoginGUI;
import de.Luca.GUIs.RegisterGUI;
import de.Luca.GUIs.WorldEditorAuswahl;
import de.Luca.GUIs.WorldEditorErstellen;
import de.Luca.GUIs.WorldEditorOverlay;
import de.Luca.GUIs.WorldEditorSettings;
import de.Luca.GameLogic.GameState;
import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;
import de.Luca.Text.TextManager;

public class SkyFightClient {

	// Connection
	public static Connection handleServerConnection;

	// Gamelogic
	public static Player p;
	public static Player pother;

	// GUIs
	public static LoginGUI loginGUI;
	public static RegisterGUI registesrGUI;
	public static LoadingGUI loadingGUI;
	public static WorldEditorOverlay worldEditorOverlay;
	public static WorldEditorAuswahl worldEditorAuswahl;
	public static WorldEditorErstellen worldEditorErstellen;
	public static WorldEditorSettings worldEditorSettings;

	// Textures
	public static Texture backgroundLOGIN;
	public static Texture playerDown;
	public static Texture playerUP;
	public static Texture background;
	public static Texture weOverlayRahmenLinks;
	public static Texture weOverlayRahmenOben;
	public static Texture weOverlayRahmenRechts;
	public static Texture weOverlayRahmenUnten;
	public static Texture weOverlayLinksA;
	public static Texture weOverlayLinksB;
	public static Texture weOverlayLinksC;
	public static Texture weOverlayMitteA;
	public static Texture weOverlayMitteB;
	public static Texture weOverlayMitteC;
	public static Texture weOverlayRechtsA;
	public static Texture weOverlayRechtsB;
	public static Texture weOverlayRechtsC;
	public static Texture weAuswahlUmrahmungA;
	public static Texture weAuswahlSuchenA;
	public static Texture weAuswahlSuchenB;
	public static Texture weAuswahlSuchenC;
	public static Texture weAuswahlUmrahmungB;
	public static Texture weAuswahlButtonA;
	public static Texture weAuswahlButtonB;
	public static Texture weAuswahlButtonC;
	public static Texture weAuswahlUmrahmungC;
	public static Texture weErstellenFenster1;
	public static Texture weErstellenNameA;
	public static Texture weErstellenNameB;
	public static Texture weErstellenNameC;
	public static Texture weErstellenFenster2;
	public static Texture weErstellenValueA;
	public static Texture weErstellenValueB;
	public static Texture weErstellenValueC;
	public static Texture weErstellenHardnessTA;
	public static Texture weErstellenHardnessTB;
	public static Texture weErstellenHardnessTC;
	public static Texture weErstellenHardnessSA;
	public static Texture weErstellenHardnessSB;
	public static Texture weErstellenBrowseA;
	public static Texture weErstellenBrowseB;
	public static Texture weErstellenBrowseC;
	public static Texture weErstellenPreview;
	public static Texture weErstellenSourceA;
	public static Texture weErstellenSourceB;
	public static Texture weErstellenSourceC;
	public static Texture weErstellenPlayA;
	public static Texture weErstellenPlayB;
	public static Texture weErstellenPlayC;
	public static Texture weExitA;
	public static Texture weExitB;
	public static Texture weSettingsRahmen;
	public static Texture weSettingsMapnameA;
	public static Texture weSettingsMapnameB;
	public static Texture weSettingsPreview;
	public static Texture weSettingsSourceA;
	public static Texture weSettingsSourceB;
	public static Texture weSettingsGelbA;
	public static Texture weSettingsGelbB;
	public static Texture weSettingsGelbC;
	public static Texture weSettingsRotA;
	public static Texture weSettingsRotB;
	public static Texture weSettingsRotC;
	public static Texture weOverlayBackA;
	public static Texture weOverlayBackB;
	public static Texture weOverlayBackC;
	public static Texture weOverlaySaveA;
	public static Texture weOverlaySaveC;
	public static Texture weAuswahlGruenA;
	public static Texture weAuswahlGruenB;
	public static Texture weAuswahlGruenC;
	public static Texture weErstellenSave2A;
    public static Texture weErstellenSave2B;
    public static Texture weErstellenSave2C;

	// Fonts
	public static long Impact20;

	// Gamestate
	public static GameState gameState = GameState.MENUE;

	// Files
	public static String root = System.getenv("APPDATA") + "/SkyFight";

	public static void load() {
		root.replace("\\", "/");
		if (!new File(root).exists()) {
			new File(root).mkdir();
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		TextManager.generateFont("C:\\Windows\\Fonts\\impact.ttf", 20, "Impact", false, false);
		Impact20 = TextManager.getFont("Impact");

		String path = root + "/res/gui/worldeditor/";
		backgroundLOGIN = Loader.loadTexture(root + "/res/gui/login/login.png", "gui");
		playerUP = Loader.loadTexture(root + "/res/player/up.png", "player");
		playerDown = Loader.loadTexture(root + "/res/player/down.png", "player");
		background = Loader.loadTexture(root + "/res/background/background.png", "background");
		weOverlayRahmenLinks = Loader.loadTexture(path + "we_un_RahmenLinks.png", "gui");
		weOverlayRahmenOben = Loader.loadTexture(path + "we_un_RahmenOben.png", "gui");
		weOverlayRahmenRechts = Loader.loadTexture(path + "we_un_RahmenRechts.png", "gui");
		weOverlayRahmenUnten = Loader.loadTexture(path + "we_un_RahmenUnten.png", "gui");
		weOverlayLinksA = Loader.loadTexture(path + "we_un_Tx1a.png", "gui");
		weOverlayLinksB = Loader.loadTexture(path + "we_un_Tx1b.png", "gui");
		weOverlayLinksC = Loader.loadTexture(path + "we_un_Tx1c.png", "gui");
		weOverlayMitteA = Loader.loadTexture(path + "we_un_Tx2a.png", "gui");
		weOverlayMitteB = Loader.loadTexture(path + "we_un_Tx2b.png", "gui");
		weOverlayMitteC = Loader.loadTexture(path + "we_un_Tx2c.png", "gui");
		weOverlayRechtsA = Loader.loadTexture(path + "we_un_Tx3a.png", "gui");
		weOverlayRechtsB = Loader.loadTexture(path + "we_un_Tx3b.png", "gui");
		weOverlayRechtsC = Loader.loadTexture(path + "we_un_Tx3c.png", "gui");
		weAuswahlUmrahmungA = Loader.loadTexture(path + "we_li_UmrahmungA.png", "gui");
		weAuswahlSuchenA = Loader.loadTexture(path + "we_li_Tx4a.png", "gui");
		weAuswahlSuchenB = Loader.loadTexture(path + "we_li_Tx4b.png", "gui");
		weAuswahlSuchenC = Loader.loadTexture(path + "we_li_Tx4c.png", "gui");
		weAuswahlUmrahmungB = Loader.loadTexture(path + "we_li_UmrahmungB.png", "gui");
		weAuswahlButtonA = Loader.loadTexture(path + "we_li_Tx5a.png", "gui");
		weAuswahlButtonB = Loader.loadTexture(path + "we_li_Tx5b.png", "gui");
		weAuswahlButtonC = Loader.loadTexture(path + "we_li_Tx5c.png", "gui");
		weAuswahlUmrahmungC = Loader.loadTexture(path + "we_li_UmrahmungC.png", "gui");
		weErstellenFenster1 = Loader.loadTexture(path + "we_re_Background1.png", "gui");
		weErstellenNameA = Loader.loadTexture(path + "we_re_Tx6a.png", "gui");
		weErstellenNameB = Loader.loadTexture(path + "we_re_Tx6b.png", "gui");
		weErstellenNameC = Loader.loadTexture(path + "we_re_Tx6c.png", "gui");
		weErstellenFenster2 = Loader.loadTexture(path + "we_re_Background2.png", "gui");
		weErstellenValueA = Loader.loadTexture(path + "we_re_Tx7a.png", "gui");
		weErstellenValueB = Loader.loadTexture(path + "we_re_Tx7b.png", "gui");
		weErstellenValueC = Loader.loadTexture(path + "we_re_Tx7c.png", "gui");
		weErstellenHardnessTA = Loader.loadTexture(path + "we_re_Tx8a.png", "gui");
		weErstellenHardnessTB = Loader.loadTexture(path + "we_re_Tx8b.png", "gui");
		weErstellenHardnessTC = Loader.loadTexture(path + "we_re_Tx8c.png", "gui");
		weErstellenHardnessSA = Loader.loadTexture(path + "we_re_Tx9a.png", "gui");
		weErstellenHardnessSB = Loader.loadTexture(path + "we_re_Tx9b.png", "gui");
		weErstellenBrowseA = Loader.loadTexture(path + "we_re_Tx10a.png", "gui");
		weErstellenBrowseB = Loader.loadTexture(path + "we_re_Tx10b.png", "gui");
		weErstellenBrowseC = Loader.loadTexture(path + "we_re_Tx10c.png", "gui");
		weErstellenPreview = Loader.loadTexture(path + "we_re_PcPr2.png", "gui");
		weErstellenSourceA = Loader.loadTexture(path + "we_re_Tx11a.png", "gui");
		weErstellenSourceB = Loader.loadTexture(path + "we_re_Tx11b.png", "gui");
		weErstellenSourceC = Loader.loadTexture(path + "we_re_Tx11c.png", "gui");
		weErstellenPlayA = Loader.loadTexture(path + "we_re_Tx13a.png", "gui");
		weErstellenPlayB = Loader.loadTexture(path + "we_re_Tx13b.png", "gui");
		weErstellenPlayC = Loader.loadTexture(path + "we_re_Tx13c.png", "gui");
		weExitA = Loader.loadTexture(path + "weXa.png", "gui");
		weExitB = Loader.loadTexture(path + "weXb.png", "gui");
		weSettingsRahmen = Loader.loadTexture(path + "we_mi_Rahmen.png", "gui");
		weSettingsMapnameA = Loader.loadTexture(path + "we_mi_Tx14a.png", "gui");
		weSettingsMapnameB = Loader.loadTexture(path + "we_mi_Tx14b.png", "gui");
		weSettingsPreview = Loader.loadTexture(path + "we_mi_Tx15.png", "gui");
		weSettingsSourceA = Loader.loadTexture(path + "we_mi_Tx16a.png", "gui");
		weSettingsSourceB = Loader.loadTexture(path + "we_mi_Tx16b.png", "gui");
		weSettingsGelbA = Loader.loadTexture(path + "we_mi_Tx19a.png", "gui");
		weSettingsGelbB = Loader.loadTexture(path + "we_mi_Tx19b.png", "gui");
		weSettingsGelbC = Loader.loadTexture(path + "we_mi_Tx19c.png", "gui");
		weSettingsRotA = Loader.loadTexture(path + "we_mi_Tx18a.png", "gui");
		weSettingsRotB = Loader.loadTexture(path + "we_mi_Tx18b.png", "gui");
		weSettingsRotC = Loader.loadTexture(path + "we_mi_Tx18c.png", "gui");
		weOverlayBackA = Loader.loadTexture(path + "we_un_Tx18a.png", "gui");
		weOverlayBackB = Loader.loadTexture(path + "we_un_Tx18b.png", "gui");
		weOverlayBackC = Loader.loadTexture(path + "we_un_Tx18c.png", "gui");
		weOverlaySaveA = Loader.loadTexture(path + "we_un_Tx19a.png", "gui");
		weOverlaySaveC = Loader.loadTexture(path + "we_un_Tx19c.png", "gui");
		weAuswahlGruenA = Loader.loadTexture(path + "we_li_Tx5(2)a.png", "gui");
		weAuswahlGruenB = Loader.loadTexture(path + "we_li_Tx5(2)b.png", "gui");
		weAuswahlGruenC = Loader.loadTexture(path + "we_li_Tx5(2)c.png", "gui");
		weErstellenSave2A = Loader.loadTexture(path + "we_re_Tx20a.png", "gui");
        weErstellenSave2B = Loader.loadTexture(path + "we_re_Tx20b.png", "gui");
        weErstellenSave2C = Loader.loadTexture(path + "we_re_Tx20c.png", "gui");

		loginGUI = new LoginGUI();
		registesrGUI = new RegisterGUI();
		loadingGUI = new LoadingGUI();
		worldEditorOverlay = new WorldEditorOverlay();
		worldEditorAuswahl = new WorldEditorAuswahl();
		worldEditorErstellen = new WorldEditorErstellen();
		worldEditorSettings = new WorldEditorSettings();
	}

}
