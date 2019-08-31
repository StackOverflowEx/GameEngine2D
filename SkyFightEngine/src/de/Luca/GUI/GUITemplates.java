package de.Luca.GUI;


import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkStyle;
import org.lwjgl.nuklear.NkVec2;

public class GUITemplates {
	
	public static void mainMenue(NkImage button) {
		
		NkContext ctx = NukManager.nukManager.getContext();
		NkStyle style = ctx.style();
		style.window().min_row_height_padding(0);
		style.window().fixed_background().data().color().set((byte)0, (byte)0, (byte)0, (byte)0);
		style.button().padding(NkVec2.create().set(0, 0));
		style.button().normal().data().image(button);
		style.button().image_padding(NkVec2.create().set(0, 0));
		style.button().border_color().set((byte)0, (byte)0, (byte)0, (byte)0);
		
	}

}
