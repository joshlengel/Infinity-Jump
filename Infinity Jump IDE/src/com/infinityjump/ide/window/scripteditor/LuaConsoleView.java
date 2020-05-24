package com.infinityjump.ide.window.scripteditor;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class LuaConsoleView extends Pane {

	private LuaConsole console;
	
	private TextArea area;
	
	public LuaConsoleView() {
		area = new TextArea();
		area.setEditable(false);
		
		area.setStyle("-fx-text-fill: red;");
		
		area.prefWidthProperty().bind(super.widthProperty());
		area.prefHeightProperty().bind(super.heightProperty());
		
		super.getChildren().add(area);
		
		console = new LuaConsole(area);
	}
	
	public LuaConsole getConsole() {
		return console;
	}
}
