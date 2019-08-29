package de.Luca.GUI;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.nuklear.Nuklear.*;


import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.joml.Vector2i;
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
	
	private ArrayList<MouseClick> fireMouseClicks;
	private ArrayList<KeyInput> fireKeyInput;
	private ArrayList<Byte> charInput;
	private ArrayList<NkVec2> scrollInput;
	private ArrayList<Vector2i> cursorInput;
	
	public GUIWindowListener() {
		fireMouseClicks = new ArrayList<MouseClick>();
		fireKeyInput = new ArrayList<KeyInput>();
		charInput = new ArrayList<Byte>();
		scrollInput = new ArrayList<NkVec2>();
		cursorInput = new ArrayList<Vector2i>();
	}

	@EventHandler
	public void onScroll(ScrollEvent e) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			NkVec2 scroll = NkVec2.mallocStack(stack).x((float) e.getxOffset()).y((float) e.getyOffset());
			queueScrollInput(scroll);
		}
	}

	@EventHandler
	public void onCharInput(CharInputEvent e) {
		queueCharInput((byte) e.getCodepoint());
	}

	@EventHandler
	public void onKeyAction(KeyEvent e) {
		long window = Window.window.getWindowID();
		boolean press = e.getAction() == GLFW_PRESS;

		switch (e.getKey()) {
		case GLFW_KEY_DELETE:
			queueKeyInput(new KeyInput(NK_KEY_DEL, press));
			break;
		case GLFW_KEY_ENTER:
			queueKeyInput(new KeyInput(NK_KEY_ENTER, press));
			break;
		case GLFW_KEY_TAB:
			queueKeyInput(new KeyInput(NK_KEY_TAB, press));
			break;
		case GLFW_KEY_BACKSPACE:
			queueKeyInput(new KeyInput(NK_KEY_BACKSPACE, press));
			break;
		case GLFW_KEY_UP:
			queueKeyInput(new KeyInput(NK_KEY_UP, press));
			break;
		case GLFW_KEY_DOWN:
			queueKeyInput(new KeyInput(NK_KEY_DOWN, press));
			break;
		case GLFW_KEY_HOME:
			queueKeyInput(new KeyInput(NK_KEY_TEXT_START, press));
			queueKeyInput(new KeyInput(NK_KEY_SCROLL_START, press));
			break;
		case GLFW_KEY_END:
			queueKeyInput(new KeyInput(NK_KEY_TEXT_END, press));
			queueKeyInput(new KeyInput(NK_KEY_SCROLL_END, press));
			break;
		case GLFW_KEY_PAGE_DOWN:
			queueKeyInput(new KeyInput(NK_KEY_SCROLL_DOWN, press));
			break;
		case GLFW_KEY_PAGE_UP:
			queueKeyInput(new KeyInput(NK_KEY_SCROLL_UP, press));
			break;
		case GLFW_KEY_LEFT_SHIFT:
		case GLFW_KEY_RIGHT_SHIFT:
			queueKeyInput(new KeyInput(NK_KEY_SHIFT, press));
			break;
		case GLFW_KEY_LEFT_CONTROL:
		case GLFW_KEY_RIGHT_CONTROL:
			if (press) {
				queueKeyInput(new KeyInput(NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS));
			} else {
				queueKeyInput(new KeyInput(NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS));
				queueKeyInput(new KeyInput(NK_KEY_COPY, false));
				queueKeyInput(new KeyInput(NK_KEY_PASTE, false));
				queueKeyInput(new KeyInput(NK_KEY_CUT, false));
				queueKeyInput(new KeyInput(NK_KEY_SHIFT, false));
			}
			break;
		}

	}
	
	@EventHandler
	public void onCursorPos(CursorPositionEvent e) {
		if(NukManager.nukManager == null) {
			return;
		}
		queueCursorInput(new Vector2i((int) e.getXpos(), (int) e.getYpos()));
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
                   
              queueMouseClick(new MouseClick(x, y, nkButton, e.getAction() == GLFW_PRESS));
          }
	}
	
	private void queueCursorInput(Vector2i vec) {
		synchronized (cursorInput) {
			cursorInput.add(vec);
		}
	}
	
	private void fireCursorInput(NkContext ctx) {
		synchronized (cursorInput) {
			for(Vector2i v : cursorInput) {
				nk_input_motion(ctx, v.x, v.y);
			}
			cursorInput.clear();
		}
	}
	
	private void queueScrollInput(NkVec2 vec) {
		synchronized (scrollInput) {
			scrollInput.add(vec);
		}
	}
	
	private void fireScrollInput(NkContext ctx) {
		synchronized (scrollInput) {
			for(NkVec2 v : scrollInput) {
				nk_input_scroll(ctx, v);
			}
			scrollInput.clear();
		}
	}
	
	private void queueCharInput(byte codepoint) {
		synchronized (charInput) {
			charInput.add(codepoint);
		}
	}
	
	private void fireCharInput(NkContext ctx) {
		synchronized (charInput) {
			for(byte i : charInput) {
				nk_input_char(ctx, i);
			}
			charInput.clear();
		}
	}

	private void queueKeyInput(KeyInput key) {
		synchronized (fireKeyInput) {
			fireKeyInput.add(key);
		}
	}
	
	private void fireKeyInput(NkContext ctx) {
		synchronized (fireKeyInput) {
			for(KeyInput i : fireKeyInput) {
				nk_input_key(ctx, i.getKey(), i.isPressed());
			}
			fireKeyInput.clear();
		}
	}
	
	private void queueMouseClick(MouseClick click) {
		synchronized (fireMouseClicks) {
			fireMouseClicks.add(click);
		}
	}
	
	private void fireMouseClicks(NkContext ctx) {
		synchronized (fireMouseClicks) {
			for(MouseClick c : fireMouseClicks) {
				nk_input_button(ctx, c.getKey(), c.getX(), c.getY(), c.isPressed());
			}
			fireMouseClicks.clear();
		}
	}
	
	public void fireActions(NkContext ctx) {
		fireCursorInput(ctx);
		fireMouseClicks(ctx);
		fireKeyInput(ctx);
		fireCharInput(ctx);
		fireScrollInput(ctx);
	}

}
