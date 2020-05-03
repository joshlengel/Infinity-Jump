package com.infinityjump.ide.window.properties;

import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

public class DoubleFormatter extends TextFormatter<Double> {
	private static Pattern doublePattern = Pattern.compile("-?((\\d*)|(\\d+.\\d*))");
		
	public DoubleFormatter() {
		super(new DoubleStringConverter(), 0.0, change -> {
			String text = change.getControlNewText();
			
			if (doublePattern.matcher(text).matches()) {
				return change;
			} else {
				return null;
			}
		});
	}
}
