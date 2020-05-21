package com.infinityjump.core.game.properties;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.game.Color;

public class QuadProperties {

	public Color color = new Color();
	
	protected boolean parse(String key, String value) {
		switch (key) {
		case "red":   color.r = Float.parseFloat(value); return true;
		case "green": color.g = Float.parseFloat(value); return true;
		case "blue":  color.b = Float.parseFloat(value); return true;
		case "alpha": color.a = Float.parseFloat(value); return true;
		default: return false;
		}
	}
	
	public final void evaluate(String key, String value) {
		if (!parse(key, value)) {
			Logger.getAPI().error("Unknown component '" + key + "' of quad properties");
			return;
		}
	}
}
