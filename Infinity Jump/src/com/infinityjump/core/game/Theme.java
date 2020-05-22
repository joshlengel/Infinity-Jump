package com.infinityjump.core.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.game.base.Type;
import com.infinityjump.core.game.properties.QuadProperties;

public class Theme {

	private Map<String, QuadProperties> properties;
	
	public Theme(InputStream theme) {
		properties = new HashMap<>();
		Properties props = new Properties();
		
		try {
			props.load(theme);
		} catch (IOException e) {
			Logger.getAPI().error("Error loading theme properties. Using default theme instead");
			
			return;
		}
		
		for (Entry<Object, Object> entry : props.entrySet()) {
			if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof String)) {
				Logger.getAPI().error("Error reading theme properties file");
				return;
			}
			
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			
			int dashI = key.indexOf('-');
			String keyPart1 = key.substring(0, dashI);
			String keyPart2 = key.substring(dashI + 1);
			
			QuadProperties p;
			
			if (properties.containsKey(keyPart1)) {
				p = properties.get(keyPart1);
			} else if (keyPart1.contentEquals("background")) {
				p = new QuadProperties();
				properties.put(keyPart1, p);
			} else {
				p = Type.parseType(keyPart1).generateProperties();
				properties.put(keyPart1, p);
			}
			
			p.evaluate(keyPart2, value);
		}
		
		properties.put("boundary", properties.get("normal"));
	}
	
	public QuadProperties getProperties(String name) {
		return properties.get(name);
	}
}
