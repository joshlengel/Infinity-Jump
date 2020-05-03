package com.infinityjump.core.game.sound;

import java.util.HashMap;
import java.util.Map;

import com.infinityjump.core.api.OpenAL;
import com.infinityjump.core.api.OpenAL.OpenALAPI;

public final class Sounds {

	private static Map<String, Object> sounds;
	
	public static void init() {
		sounds = new HashMap<>();
		
		OpenALAPI api = OpenAL.getAPI();
		
		sounds.put("jump", api.newAudio("jump.wav"));
		sounds.put("level-finished", api.newAudio("level-finished.wav"));
		sounds.put("die", api.newAudio("die.wav"));
	}
	
	private Sounds() {}
	
	public static void playSound(String name) {
		OpenAL.getAPI().play(sounds.get(name));
	}
}
