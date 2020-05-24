package com.infinityjump.ide.window.menu;

import com.infinityjump.ide.window.leveleditor.LevelView;
import com.infinityjump.ide.window.scripteditor.LuaEditorView;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class EditMenu extends Menu {

	public EditMenu(LevelView levelView, LuaEditorView editorView) {
		super("_Edit");
		
		MenuItem newQuadMI = new MenuItem("Add new Quad");
		
		newQuadMI.setOnAction(e -> {
			levelView.spawnQuad();
		});
		
		newQuadMI.setAccelerator(KeyCombination.valueOf("Alt+Q"));
		
		MenuItem resetMI = new MenuItem("Reset");
		
		resetMI.setOnAction(e -> {
			levelView.reset();
			editorView.reset();
		});
		
		resetMI.setAccelerator(KeyCombination.valueOf("Ctrl+R"));
		
		super.getItems().addAll(newQuadMI, resetMI);
	}
}
