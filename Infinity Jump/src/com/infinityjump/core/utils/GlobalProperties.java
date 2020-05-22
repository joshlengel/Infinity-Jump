package com.infinityjump.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.infinityjump.core.api.Logger;

public class GlobalProperties {

	public static Properties properties;
	
	public static void init(InputStream in) {
		properties = new Properties();
		
		try {
			properties.load(in);
		} catch (IOException e) {
			Logger.getAPI().error("Error loading global properties");
			return;
		}
	}
	
	private GlobalProperties() {}
}
