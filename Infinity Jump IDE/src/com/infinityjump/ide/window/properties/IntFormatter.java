package com.infinityjump.ide.window.properties;

import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class IntFormatter extends TextFormatter<Integer> {
	private static Pattern intPattern = Pattern.compile("-?\\d+");
		
	public IntFormatter() {
		super(new IntegerStringConverter(), 0, change -> {
			String text = change.getControlNewText();
			
			if (intPattern.matcher(text).matches()) {
				return change;
			} else {
				return null;
			}
		});
	}
}
