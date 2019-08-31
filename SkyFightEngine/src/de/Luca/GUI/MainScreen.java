package de.Luca.GUI;

import static org.lwjgl.nuklear.Nuklear.*;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import de.Luca.Entities.Texture;
import de.Luca.Window.Window;

public class MainScreen extends GUI {

	private Texture buttonTex;
	private NkImage buttonImg;
	
	public MainScreen(boolean visible, Texture buttonTex) {
		super(visible);
		this.buttonTex = buttonTex;
	}

	public void render() {
		
		if(buttonTex.getTextureID() == -1) {
			return;
		}
		
		if(buttonImg == null) {
			buttonImg = NkImage.create();
			buttonImg.handle(it -> it.id(buttonTex.getTextureID()));
		}
		
		NkContext ctx = NukManager.nukManager.getContext();
						
		try(MemoryStack stack = MemoryStack.stackPush()){
			
			NkRect rect = NkRect.mallocStack(stack);
			rect.set(0, 0, Window.window.getWindowSize().x, Window.window.getWindowSize().y);
			GUITemplates.mainMenue(buttonImg);
			
			if(nk_begin(ctx, "Main Menue", rect, NK_WINDOW_BORDER|NK_WINDOW_MOVABLE|NK_WINDOW_CLOSABLE)) {
				
//				nk_layout_row_dynamic(ctx, 50, 1);
				nk_layout_row_begin(ctx, NK_STATIC, 50, 1);
				nk_layout_row_push(ctx, Window.window.getWindowSize().x / 2);
				nk_button_text(ctx, "Spiel suchen");
				nk_layout_row_end(ctx);
				
			}
			
			nk_end(ctx);
			
		}
		
	}

}
