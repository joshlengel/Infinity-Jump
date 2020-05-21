package com.infinityjump.core.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.game.base.Type;

public class Theme {

	private Color defaultQuadColor;
	private Color deadlyQuadColor;
	private Color bouncyQuadColor;
	private Color stickyQuadColor;
	private Color teleportQuadColor;
	
	private Color backgroundColor;
	private Color targetColor;
	private Color playerColor;
	
	public Theme(InputStream theme) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(theme))) {
			String line;
			
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("default-quad-color ")) {
					String[] args = line.split(" ");
					
					defaultQuadColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				} else if (line.startsWith("deadly-quad-color ")) {
					String[] args = line.split(" ");
					
					deadlyQuadColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				} else if (line.startsWith("bouncy-quad-color ")) {
					String[] args = line.split(" ");
					
					bouncyQuadColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				} else if (line.startsWith("sticky-quad-color ")) {
					String[] args = line.split(" ");
					
					stickyQuadColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				} else if (line.startsWith("teleport-quad-color ")) {
					String[] args = line.split(" ");
					
					teleportQuadColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				} else if (line.startsWith("background-color ")) {
					String[] args = line.split(" ");
					
					backgroundColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				} else if (line.startsWith("target-color ")) {
					String[] args = line.split(" ");
					
					targetColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				} else if (line.startsWith("player-color ")) {
					String[] args = line.split(" ");
					
					playerColor = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1.0f);
				}
			}
		} catch (IOException e) {
			Logger.getAPI().error("Error reading theme file. Using default theme instead");
			
			this.defaultQuadColor = new Color(0.5f, 0.4f, 0.1f, 1.0f);
			this.deadlyQuadColor = new Color(0.9f, 0.1f, 0.2f, 1.0f);
			
			this.backgroundColor = new Color(0.2f, 0.2f, 0.2f, 1.0f);
			this.targetColor = new Color(0.4f, 0.1f, 0.6f, 1.0f);
			this.playerColor = new Color(0.05f, 0.05f, 0.05f, 1.0f);
		}
	}
	
	public Color forQuad(Type type) {
		switch (type) {
		case NORMAL: return defaultQuadColor;
		case DEADLY: return deadlyQuadColor;
		case BOUNCY: return bouncyQuadColor;
		case STICKY: return stickyQuadColor;
		case TELEPORT: return teleportQuadColor;
		default:     return null;
		}
	}

	public Color getDefaultQuadColor() {
		return defaultQuadColor;
	}

	public Color getDeadlyQuadColor() {
		return deadlyQuadColor;
	}
	
	public Color getBouncyQuadColor() {
		return bouncyQuadColor;
	}
	
	public Color getStickyQuadColor() {
		return stickyQuadColor;
	}
	
	public Color getTeleportQuadColor() {
		return teleportQuadColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getTargetColor() {
		return targetColor;
	}

	public Color getPlayerColor() {
		return playerColor;
	}
}
