package de.Luca.GUI;

import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import de.Luca.Window.Window;

public class MainScreen extends GUI {

	
	public MainScreen(boolean visible) {
		super(visible);
	}

	public void render() {
		
		NkContext ctx = NukManager.nukManager.getContext();
						
		try(MemoryStack stack = MemoryStack.stackPush()){
			
			NkRect rect = NkRect.mallocStack(stack);
			rect.set(0, 0, Window.window.getWindowSize().x, Window.window.getWindowSize().y);
			GUITemplates.mainMenue();
			
			if(nk_begin(ctx, "Main Menue", rect, 0)) {
				
//				nk_layout_row_dynamic(ctx, 50, 1);
				nk_layout_row_begin(ctx, NK_STATIC, 50, 1);
				nk_layout_row_push(ctx, Window.window.getWindowSize().x / 2);
				nk_button_label(ctx, "Spiel suchen");
				nk_layout_row_end(ctx);
				
			}
			
			nk_end(ctx);
			
		}
		
	}

}
