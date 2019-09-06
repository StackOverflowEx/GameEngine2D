package de.Luca.Main;

import de.Luca.Calculation.BeatHandler;

public class Main {
	
	public static void main(String[] args) {	
		SkyFightEngine.init(new BeatHandler() {
			
			@Override
			public void loop() {

			}
			
			@Override
			public void init() {
				
			}
		});
	}

}
