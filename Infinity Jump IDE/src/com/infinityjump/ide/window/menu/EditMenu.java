package com.infinityjump.ide.window.menu;

import com.infinityjump.ide.window.LevelView;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class EditMenu extends Menu {

	public EditMenu(LevelView view) {
		super("_Edit");
		
		MenuItem newQuadMI = new MenuItem("Add new Quad");
		
		newQuadMI.setOnAction(e -> {
			view.spawnQuad();
		});
		
		newQuadMI.setAccelerator(KeyCombination.valueOf("Alt+Q"));
		
		super.getItems().addAll(newQuadMI);
	}
}
