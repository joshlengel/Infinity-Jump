package com.infinityjump.core.utils;

import java.util.regex.Pattern;

public class Patterns {

	public static final Pattern braces = Pattern.compile("[\\[\\]]");
	public static final Pattern commaWithSpaces = Pattern.compile("\\s*,\\s*");
	public static final Pattern equals = Pattern.compile("\\s*=\\s*");
	
	
	public static final Pattern multipleSpaces = Pattern.compile(" +");
	
	private Patterns() {}
}
