package com.infinityjump.ide.window.scripteditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.infinityjump.ide.Utils;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class LuaEditorView extends Pane {
	
	private TextArea editor;
	
	public LuaEditorView() {
		editor = new TextArea();
		editor.setEditable(true);
		
		editor.prefWidthProperty().bind(super.widthProperty());
		editor.prefHeightProperty().bind(super.heightProperty());
		
		super.getChildren().add(editor);
	}

	public void load(InputStream script) {
		StringBuilder builder = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(script))) {
			String line;
			
			while ((line = reader.readLine()) != null) {
				builder.append(line).append('\n');
			}
		} catch (IOException e) {
			System.err.println("Error loading script source");
		}
		
		editor.setText(builder.toString());
	}
	
	public void reset() {
		editor.setText(Utils.getDefaultScript());
	}
	
	public String getScript() {
		return editor.getText();
	}
}
