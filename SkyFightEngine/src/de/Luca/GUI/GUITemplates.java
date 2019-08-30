package de.Luca.GUI;

import org.lwjgl.nuklear.NkContext;

public class GUITemplates {
	
	public static void mainMenue() {
		
		NkContext ctx = NukManager.nukManager.getContext();
		ctx.style().window().fixed_background().data().color().set((byte)0, (byte)0, (byte)0, (byte)0);
	}

}
