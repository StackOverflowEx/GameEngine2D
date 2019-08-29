package de.Luca.GUI;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.nuklear.Nuklear.*;


import java.nio.DoubleBuffer;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.system.MemoryStack;

import de.Luca.EventManager.EventHandler;
import de.Luca.EventManager.Listener;
import de.Luca.Events.CharInputEvent;
import de.Luca.Events.CursorPositionEvent;
import de.Luca.Events.KeyEvent;
import de.Luca.Events.MouseButtonEvent;
import de.Luca.Events.ScrollEvent;
import de.Luca.Window.Window;

public class GUIWindowListener implements Listener {

	@EventHandler
	public void onScroll(ScrollEvent e) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			NkVec2 scroll = NkVec2.mallocStack(stack).x((float) e.getxOffset()).y((float) e.getyOffset());
			nk_input_scroll(NukManager.nukManager.getContext(), scroll);
		}
	}

	@EventHandler
	public void onCharInput(CharInputEvent e) {
		nk_input_unicode(NukManager.nukManager.getContext(), e.getCodepoint());
	}

	@EventHandler
	public void onKeyAction(KeyEvent e) {
		NkContext ctx = NukManager.nukManager.getContext();
		long window = Window.window.getWindowID();
		boolean press = e.getAction() == GLFW_PRESS;

		switch (e.getKey()) {
		case GLFW_KEY_DELETE:
			nk_input_key(ctx, NK_KEY_DEL, press);
			break;
		case GLFW_KEY_ENTER:
			nk_input_key(ctx, NK_KEY_ENTER, press);
			break;
		case GLFW_KEY_TAB:
			nk_input_key(ctx, NK_KEY_TAB, press);
			break;
		case GLFW_KEY_BACKSPACE:
			nk_input_key(ctx, NK_KEY_BACKSPACE, press);
			break;
		case GLFW_KEY_UP:
			nk_input_key(ctx, NK_KEY_UP, press);
			break;
		case GLFW_KEY_DOWN:
			nk_input_key(ctx, NK_KEY_DOWN, press);
			break;
		case GLFW_KEY_HOME:
			nk_input_key(ctx, NK_KEY_TEXT_START, press);
			nk_input_key(ctx, NK_KEY_SCROLL_START, press);
			break;
		case GLFW_KEY_END:
			nk_input_key(ctx, NK_KEY_TEXT_END, press);
			nk_input_key(ctx, NK_KEY_SCROLL_END, press);
			break;
		case GLFW_KEY_PAGE_DOWN:
			nk_input_key(ctx, NK_KEY_SCROLL_DOWN, press);
			break;
		case GLFW_KEY_PAGE_UP:
			nk_input_key(ctx, NK_KEY_SCROLL_UP, press);
			break;
		case GLFW_KEY_LEFT_SHIFT:
		case GLFW_KEY_RIGHT_SHIFT:
			nk_input_key(ctx, NK_KEY_SHIFT, press);
			break;
		case GLFW_KEY_LEFT_CONTROL:
		case GLFW_KEY_RIGHT_CONTROL:
			if (press) {
				nk_input_key(ctx, NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
			} else {
				nk_input_key(ctx, NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
				nk_input_key(ctx, NK_KEY_COPY, false);
				nk_input_key(ctx, NK_KEY_PASTE, false);
				nk_input_key(ctx, NK_KEY_CUT, false);
				nk_input_key(ctx, NK_KEY_SHIFT, false);
			}
			break;
		}

	}
	
	@EventHandler
	public void onCursorPos(CursorPositionEvent e) {
		if(NukManager.nukManager == null) {
			return;
		}
		nk_input_motion(NukManager.nukManager.getContext(), (int)e.getXpos(), (int)e.getYpos());
	}
	
	@EventHandler
	public void onMouseButton(MouseButtonEvent e) {
		
		
		  try (MemoryStack stack = MemoryStack.stackPush()) {
              DoubleBuffer cx = stack.mallocDouble(1);
              DoubleBuffer cy = stack.mallocDouble(1);

              glfwGetCursorPos(Window.window.getWindowID(), cx, cy);

              int x = (int)cx.get(0);
              int y = (int)cy.get(0);

              int nkButton;
              switch (e.getButton()) {
                  case GLFW_MOUSE_BUTTON_RIGHT:
                      nkButton = NK_BUTTON_RIGHT;
                      break;
                  case GLFW_MOUSE_BUTTON_MIDDLE:
                      nkButton = NK_BUTTON_MIDDLE;
                      break;
                  default:
                      nkButton = NK_BUTTON_LEFT;
              }
                                          
              nk_input_button(NukManager.nukManager.getContext(), nkButton, x, y, e.getAction() == GLFW_PRESS);
          }
	}

}
