package com.infinityjump.game.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.infinityjump.core.api.OpenAL.OpenALAPI;

public class OpenALAPIImpl implements OpenALAPI {

	private String assetDir;
	
	public OpenALAPIImpl(String assetDir) {
		this.assetDir = assetDir;
	}
	
	@Override
	public Object newAudio(String file) {
		URL audio;
		try {
			audio = new File(assetDir, "sound/" + file).toURI().toURL();
		} catch (MalformedURLException e) {
			System.err.println("Error finding sound file '" + file + "'");
			return null;
		}
		
		Clip clip = null;
		
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio);
			
			clip = AudioSystem.getClip();
			
			clip.open(audioStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.err.println("Error loading audio file:\n" + e.getMessage());
		}
		
		return clip;
	}

	@Override
	public void play(Object audioClip) {
		Clip clip = (Clip)audioClip;
		
		if (clip.isRunning()) {
			clip.stop();
		}
		
		clip.setFramePosition(0);
		clip.start();
	}
}
