package com.infinityjump.ide.window;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GlobalProperties {

	public static Properties properties;
	
	public static void init(String assetDir) {
		properties = new Properties();
		
		try (FileReader reader = new FileReader(new File(assetDir, "infinity-jump-ide.properties"))) {
			properties.load(reader);
		} catch (IOException e) {
			System.err.println("Error loading global properties for IDE");
			return;
		}
	}
}
