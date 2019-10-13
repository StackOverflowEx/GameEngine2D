package de.Luca.Utils;

import org.joml.Vector2f;
import org.joml.Vector2i;

import de.Luca.Window.Window;

public class Calc {
	
	//Gibt die Pixelanzahl für eine Prozentzahl zurück
	//ist xValue 0.5 und das Fenster 600 Pixel breit, wird 300 zurück gegeben
	
	public static Vector2i getPixel(float xValue, float yValue) {
		Vector2f windowSize = Window.getWindowSize();
		int x = (int) (windowSize.x * xValue);
		int y = (int) (windowSize.y * yValue);
		return new Vector2i(x, y);
	}
	
	public static int getPixelWidth(float value) {
		return (int) (Window.getWindowSize().x * value);
	}
	
	public static int getPixelHeight(float value) {
		return (int) (Window.getWindowSize().y * value);
	}

}
