package com.infinityjump.ide.window.menu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.script.Script;
import com.infinityjump.game.Launcher;
import com.infinityjump.ide.window.LuaEditorView;
import com.infinityjump.ide.window.leveleditor.LevelView;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class TestMenu extends Menu {

	public TestMenu(LevelView levelView, LuaEditorView editorView, Theme theme, String assetDir) {
		super("_Test");
		
		MenuItem launchCurrent = new MenuItem("Launch current");
		
		launchCurrent.setOnAction(e -> {
			String levelSource = levelView.makeLevelSource();
			String scriptSource = editorView.getScript();
			
			InputStream levelStream = new ByteArrayInputStream(levelSource.getBytes());
			
			Level level = new Level();
			level.read(levelStream);
			
			Script script = new Script(scriptSource);
			
			Launcher launcher = new Launcher();
			launcher.launch(level, script, theme, assetDir);
			
			try {
				levelStream.close();
			} catch (IOException e1) {
				System.err.println("Error closing level file");
			}
		});
		
		launchCurrent.setAccelerator(KeyCombination.valueOf("Ctrl+L"));
		
		super.getItems().add(launchCurrent);
	}
}
