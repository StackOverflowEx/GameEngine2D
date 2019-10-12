package de.Luca.Main;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.Luca.Connection.Connection;
import de.Luca.Entities.Player;
import de.Luca.GIF.Animation;
import de.Luca.GUIs.BlockAuswahl;
import de.Luca.GUIs.EndscreenOverlayGUI;
import de.Luca.GUIs.IngameOverlayGUI;
import de.Luca.GUIs.LoginGUI;
import de.Luca.GUIs.MainMenuGUI;
import de.Luca.GUIs.PasswortVergessenGUI;
import de.Luca.GUIs.RegistrierenGUI;
import de.Luca.GUIs.WeltenAuswahlGUI;
import de.Luca.GUIs.WorldEditorAuswahl;
import de.Luca.GUIs.WorldEditorErstellen;
import de.Luca.GUIs.WorldEditorOverlay;
import de.Luca.GUIs.WorldEditorSettings;
import de.Luca.GameLogic.GameState;
import de.Luca.Loading.Loader;
import de.Luca.Models.Texture;
import de.Luca.Sound.AudioManager;
import de.Luca.Sound.SoundData;
import de.Luca.Sound.Source;

public class SkyFightClient {

	// Connection
	public static Connection handleServerConnection;

	// Gamelogic
	public static Player p;
	public static Player pother;

	// GUIs
//	public static LoginGUIOLD loginGUI;
//	public static RegisterGUIOLD registesrGUI;
	public static LoginGUI loginGUI;
	public static MainMenuGUI mainGUI;
	public static PasswortVergessenGUI forgotPWGUI;
	public static RegistrierenGUI registerGUI;
	public static WeltenAuswahlGUI worldSelctGUI;
	public static WorldEditorOverlay worldEditorOverlay;
	public static WorldEditorAuswahl worldEditorAuswahl;
	public static WorldEditorErstellen worldEditorErstellen;
	public static WorldEditorSettings worldEditorSettings;
	public static IngameOverlayGUI ingameOverlay;
	public static BlockAuswahl blockSelect;
	public static EndscreenOverlayGUI endGUI;

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
	public static Texture LoginBackground;
	public static Texture LoginRahmen;
	public static Texture LoginEingabeA;
	public static Texture LoginEingabeB;
	public static Texture LoginEingabeC;
	public static Texture LoginLoginA;
	public static Texture LoginLoginB;
	public static Texture LoginLoginC;
	public static Texture LoginVergessenA;
	public static Texture LoginVergessenB;
	public static Texture LoginVergessenC;
	public static Texture LoginRegistrierenA;
	public static Texture LoginRegistrierenB;
	public static Texture LoginRegistrierenC;
	public static Texture MainBackground;
	public static Texture MainButtonA;
	public static Texture MainButtonB;
	public static Texture MainButtonC;
	public static Texture AuswahlRahmen;
	public static Texture AuswahlPreview;
	public static Texture AuswahlEingabeG;
	public static Texture AuswahlEingabeK;
	public static Texture AuswahlScrollingRahmen;
	public static Texture AuswahlScrollingKnopfA;
	public static Texture AuswahlScrollingKnopfB;
	public static Texture AuswahlScrollingKnopfC;
	public static Texture AuswahlErstellenA;
	public static Texture AuswahlErstellenB;
	public static Texture AuswahlErstellenC;
	public static Texture IngameOverlayStatsPicture;
	public static Texture IngameOverlayStatsHeart;
	public static Texture IngameOverlayStatsCoin;
	public static Texture IngameOverlayHotbarLinks;
	public static Texture IngameOverlayHotbarMitte;
	public static Texture IngameOverlayHotbarRechts;
	public static Texture IngameOverlayHotbarHover;
	public static Texture IngameOverlayHotbarSword;
	public static Texture IngameOverlayHotbarBow;
	public static Texture IngameOverlayHotbarPickaxe;
	public static Texture IngameOverlayHotbarBlockStone;
	public static Texture EndscreenOverlayGewonnenRahmen;
	public static Texture EndscreenOverlayVerlorenRahmen;
	public static Texture EndscreenOverlayAbbruchRahmen;
	public static Texture EndscreenOverlayWeitermachenA;
	public static Texture EndscreenOverlayWeitermachenB;
	public static Texture EndscreenOverlayWeitermachenC;

	// Fonts
	public static long Impact20;
	public static long Constantia86;
	public static long ConstantiaB40;
	public static long ConstantiaB32;
	public static long ConstantiaB80;
	public static long ConstantiaB56;
	public static long ConstantiaB38;
	public static long ConstantiaB26;
	public static long Calibri26;
	public static long CalibriB20;
	public static long Alba18;
	public static long Alba38;
	
	// Sounds
	public static SoundData backMusic;
	public static SoundData footstep;
	public static SoundData breakingSound;
	public static SoundData arrowHit;
	public static SoundData bowShoot;
	public static SoundData missHit;
	public static SoundData hit;
	public static SoundData click;
	
	//Sound sources
	public static Source walking;
	public static Source walkingOther;
	public static Source backgroundMusic;
	
	//Animations
	public static Animation upRun;
	public static Animation downRun;
	public static Animation punchUp;
	public static Animation punchDown;

	// Gamestate
	public static GameState gameState = GameState.MENUE;

	// Files
	public static String root = System.getenv("APPDATA") + "\\SkyFight";

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
		
		walking = AudioManager.genSource();
		walkingOther = AudioManager.genSource();
		backgroundMusic = AudioManager.genSource();
		
		upRun = new Animation(root + "/res/player/upRun.gif", "run");
		downRun = new Animation(root + "/res/player/downRun.gif", "run");
		punchUp = new Animation(root + "/res/player/punchUp.gif", "punch");
		punchDown = new Animation(root + "/res/player/punchDown.gif", "punch");
		
		breakingSound = AudioManager.loadSound(root + "/res/sounds/ingame/breaking.ogg", "breaking");
		backMusic = AudioManager.loadSound(root + "/res/sounds/background.ogg", "background");
		footstep = AudioManager.loadSound(root + "/res/sounds/ingame/footstep.ogg", "footsteop");
		arrowHit = AudioManager.loadSound(root + "/res/sounds/ingame/arrow_hit.ogg", "footsteop");
		bowShoot = AudioManager.loadSound(root + "/res/sounds/ingame/Bow_release.ogg", "footsteop");
		hit = AudioManager.loadSound(root + "/res/sounds/ingame/sword_hit_2.ogg", "footsteop");
		missHit = AudioManager.loadSound(root + "/res/sounds/ingame/sword_swing.ogg", "footsteop");
		click = AudioManager.loadSound(root + "/res/sounds/ingame/Button.ogg", "footsteop");

//		TextManager.generateFont("C:\\Windows\\Fonts\\impact.ttf", 20, "Impact", false, false);
//		TextManager.generateFont(root + "/res/fonts/constan.ttf", 86 , "Constantia", false, true);
//		TextManager.generateFont(root + "/res/fonts/constanb.ttf", 40 , "ConstantiaB", false, false);
//		TextManager.generateFont(root + "/res/fonts/constanb.ttf", 30 , "ConstantiaB2", false, false);
//		TextManager.generateFont(root + "/res/fonts/constanb.ttf", 62 , "ConstantiaB3", false, false);
//		TextManager.generateFont(root + "/res/fonts/constanb.ttf", 48 , "ConstantiaB4", false, false);
//		TextManager.generateFont(root + "/res/fonts/constanb.ttf", 36 , "ConstantiaB5", false, false);
//		TextManager.generateFont(root + "/res/fonts/constanb.ttf", 24 , "ConstantiaB6", false, false);
//		TextManager.generateFont(root + "/res/fonts/calibri.ttf", 24 , "Calibri", false, false);
//		TextManager.generateFont(root + "/res/fonts/calibrib.ttf", 20 , "CalibriB1", false, false);
//		TextManager.generateFont(root + "/res/fonts/ALBAM___.ttf", 26 , "Alba1", false, false);
//		TextManager.generateFont(root + "/res/fonts/ALBAM___.ttf", 36 , "Alba2", false, false);
//		Impact20 = TextManager.getFont("Impact");
//		Constantia86 = TextManager.getFont("Constantia");
//		ConstantiaB40 = TextManager.getFont("ConstantiaB");
//		ConstantiaB32 = TextManager.getFont("ConstantiaB2");
//		ConstantiaB80 = TextManager.getFont("ConstantiaB3");
//		ConstantiaB56 = TextManager.getFont("ConstantiaB4");
//		ConstantiaB38 = TextManager.getFont("ConstantiaB5");
//		ConstantiaB26 = TextManager.getFont("ConstantiaB6");
//		Calibri26 = TextManager.getFont("Calibri");
//		CalibriB20 = TextManager.getFont("CalibriB1");
//		Alba18 = TextManager.getFont("Alba1");
//		Alba38 = TextManager.getFont("Alba2");

		String path = root + "/res/gui/worldeditor/";
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

		path = root + "/res/gui/preGame/";
		LoginBackground = Loader.loadTexture(path + "LoginBackground.png", "gui");
		LoginRahmen = Loader.loadTexture(path + "Login_Rahmen.png", "gui");
		LoginEingabeA = Loader.loadTexture(path + "Login_Login1a.png", "gui");
		LoginEingabeB = Loader.loadTexture(path + "Login_Login1b.png", "gui");
		LoginEingabeC = Loader.loadTexture(path + "Login_Login1c.png", "gui");
		LoginLoginA = Loader.loadTexture(path + "Login_AnmeldenA.png", "gui");
		LoginLoginB = Loader.loadTexture(path + "Login_AnmeldenB.png", "gui");
		LoginLoginC = Loader.loadTexture(path + "Login_AnmeldenC.png", "gui");
		LoginVergessenA = Loader.loadTexture(path + "Login_VergessenA.png", "gui");
		LoginVergessenB = Loader.loadTexture(path + "Login_VergessenB.png", "gui");
		LoginVergessenC = Loader.loadTexture(path + "Login_VergessenC.png", "gui");
		LoginRegistrierenA = Loader.loadTexture(path + "Login_RegistrierenA.png", "gui");
		LoginRegistrierenB = Loader.loadTexture(path + "Login_RegistrierenB.png", "gui");
		LoginRegistrierenC = Loader.loadTexture(path + "Login_RegistrierenC.png", "gui");
		MainBackground = Loader.loadTexture(path + "PxBackMenu.png", "gui");
		MainButtonA = Loader.loadTexture(path + "Menu_ButtonA.png", "gui");
		MainButtonB = Loader.loadTexture(path + "Menu_ButtonB.png", "gui");
		MainButtonC = Loader.loadTexture(path + "Menu_ButtonC.png", "gui");
		AuswahlRahmen = Loader.loadTexture(path + "Auswahl_Rahmen.png", "gui");
		AuswahlPreview = Loader.loadTexture(path + "Auswahl_Preview.png", "gui");
		AuswahlEingabeG = Loader.loadTexture(path + "Auswahl_EingabeGroﬂ.png", "gui");
		AuswahlEingabeK = Loader.loadTexture(path + "Auswahl_EingabeKlein.png", "gui");
		AuswahlScrollingRahmen = Loader.loadTexture(path + "Auswahl_SliderRahmen.png", "gui");
		AuswahlScrollingKnopfA = Loader.loadTexture(path + "Auswahl_SliderKnopfA.png", "gui");
		AuswahlScrollingKnopfB = Loader.loadTexture(path + "Auswahl_SliderKnopfB.png", "gui");
		AuswahlScrollingKnopfC = Loader.loadTexture(path + "Auswahl_SliderKnopfC.png", "gui");
		AuswahlErstellenA = Loader.loadTexture(path + "Auswahl_ErstellenA.png", "gui");
		AuswahlErstellenB = Loader.loadTexture(path + "Auswahl_ErstellenB.png", "gui");
		AuswahlErstellenC = Loader.loadTexture(path + "Auswahl_ErstellenC.png", "gui");
		path = root + "/res/gui/ingame/";
		IngameOverlayStatsPicture = Loader.loadTexture(path + "IngameOverlay_StatsPicture.png", "gui");
		IngameOverlayStatsHeart = Loader.loadTexture(path + "IngameOverlay_StatsHeart.png", "gui");
		IngameOverlayStatsCoin = Loader.loadTexture(path + "IngameOverlay_StatsCoin.png", "gui");
		IngameOverlayHotbarLinks = Loader.loadTexture(path + "IngameOverlay_HotbarLinks.png", "gui");
		IngameOverlayHotbarMitte = Loader.loadTexture(path + "IngameOverlay_HotbarMitte.png", "gui");
		IngameOverlayHotbarRechts = Loader.loadTexture(path + "IngameOverlay_HotbarRechts.png", "gui");
		IngameOverlayHotbarHover = Loader.loadTexture(path + "IngameOverlay_HotbarHover.png", "gui");
		IngameOverlayHotbarSword = Loader.loadTexture(path + "IngameOverlay_HotbarSword.png", "gui");
		IngameOverlayHotbarBow = Loader.loadTexture(path + "IngameOverlay_HotbarBow.png", "gui");
		IngameOverlayHotbarPickaxe = Loader.loadTexture(path + "IngameOverlay_HotbarPickaxe.png", "gui");
		IngameOverlayHotbarBlockStone = Loader.loadTexture(path + "IngameOverlay_HotbarBlockStone.png", "gui");
		EndscreenOverlayGewonnenRahmen = Loader.loadTexture(path + "EndscreenOverlay_GewonnenRahmen.png", "gui");
		EndscreenOverlayVerlorenRahmen = Loader.loadTexture(path + "EndscreenOverlay_VerlorenRahmen.png", "gui");
		EndscreenOverlayAbbruchRahmen = Loader.loadTexture(path + "EndscreenOverlay_AbbruchRahmen.png", "gui");
		EndscreenOverlayWeitermachenA = Loader.loadTexture(path + "EndscreenOverlay_WeitermachenA.png", "gui");
		EndscreenOverlayWeitermachenB = Loader.loadTexture(path + "EndscreenOverlay_WeitermachenB.png", "gui");
		EndscreenOverlayWeitermachenC = Loader.loadTexture(path + "EndscreenOverlay_WeitermachenC.png", "gui");

//		loginGUI = new LoginGUIOLD();
//		registesrGUI = new RegisterGUIOLD();
		endGUI = new EndscreenOverlayGUI();
		loginGUI = new LoginGUI();
		registerGUI = new RegistrierenGUI();
		worldSelctGUI = new WeltenAuswahlGUI();
		mainGUI = new MainMenuGUI();
		forgotPWGUI = new PasswortVergessenGUI();
		worldEditorOverlay = new WorldEditorOverlay();
		worldEditorAuswahl = new WorldEditorAuswahl();
		worldEditorErstellen = new WorldEditorErstellen();
		worldEditorSettings = new WorldEditorSettings();
		ingameOverlay = new IngameOverlayGUI();
		blockSelect = new BlockAuswahl();
	}

}
