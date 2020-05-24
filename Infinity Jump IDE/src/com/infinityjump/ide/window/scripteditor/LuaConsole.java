package com.infinityjump.ide.window.scripteditor;

import java.io.IOException;
import java.io.OutputStream;

import com.infinityjump.ide.window.GlobalProperties;

import javafx.scene.control.TextArea;

public class LuaConsole extends OutputStream {

	private static int MAX_LINES;
	
	public static void init() {
		MAX_LINES = Integer.parseInt(GlobalProperties.properties.getProperty("console-max-lines"));
	}
	
	private String[] lines;
	private int linePointer;
	
	private TextArea area;
	
	public LuaConsole(TextArea area) {
		this.area = area;
		
		this.lines = new String[MAX_LINES];
		this.linePointer = 0;
	}
	
	@Override
	public void write(int b) throws IOException {
		char c = (char)b;
		
		if (c == '\n') {
			if (linePointer == MAX_LINES - 1) {
				lines[0] = null;
				
				System.arraycopy(lines, 1, lines, 0, MAX_LINES - 1);
				
				lines[linePointer] = "";
				
				area.clear();
				
				for (String line : lines) {
					area.appendText(line + '\n');
				}
			} else {
				lines[++linePointer] = "";
				
				area.appendText("\n");
			}
		} else {
			lines[linePointer] = lines[linePointer] + c;
			area.appendText(String.valueOf(c));
		}
	}
}
