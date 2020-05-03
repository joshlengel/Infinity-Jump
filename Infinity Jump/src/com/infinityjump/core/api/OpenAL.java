package com.infinityjump.core.api;

public class OpenAL {

	public static interface OpenALAPI {
		
		Object newAudio(String file);
		void play(Object audioClip);
	}
	
	private static OpenALAPI api;
	
	public static void init(OpenALAPI api) {
		OpenAL.api = api;
	}
	
	public static OpenALAPI getAPI() {
		return api;
	}
}
