package com.infinityjump.ide.window.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.infinityjump.ide.window.LuaEditorView;
import com.infinityjump.ide.window.leveleditor.LevelView;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileMenu extends Menu {

	public FileMenu(LevelView levelView, LuaEditorView ijsEditor) {
		super("_File");
		
		MenuItem importMI = new MenuItem("Import .lev/.lua file");
		
		importMI.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().addAll(
					new ExtensionFilter("Level file (*.lev)", "*.lev"),
					new ExtensionFilter("Script file (*.lua)", "*.lua"));
			
			File file = chooser.showOpenDialog(null);
			
			if (file == null) return;
			
			try {
				InputStream stream = new FileInputStream(file);

				if (file.getPath().endsWith(".lev")) {
					levelView.load(stream);
					levelView.repaint();
				} else {
					ijsEditor.load(stream);
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		importMI.setAccelerator(KeyCombination.valueOf("Ctrl+I"));
		
		MenuItem exportMI = new MenuItem("Export");
		
		exportMI.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Export");
			chooser.getExtensionFilters().add(new ExtensionFilter("Level file (*.lev)", "*.lev"));
			
			File levelFile = chooser.showSaveDialog(null);
			
			if (levelFile == null) return;
			
			String levelFilename = levelFile.getAbsolutePath();
			
			File scriptFile = new File(levelFilename.substring(0, levelFilename.length() - 4) + ".lua");
			
			try {
				levelFile.createNewFile();
				scriptFile.createNewFile();
				String levelSource = levelView.makeLevelSource();
				String ijsSource = ijsEditor.getScript();
				
				FileWriter levelWriter = new FileWriter(levelFile);
				levelWriter.write(levelSource);
				
				FileWriter ijsWriter = new FileWriter(scriptFile);
				ijsWriter.write(ijsSource);
				
				levelWriter.flush();
				levelWriter.close();
				
				ijsWriter.flush();
				ijsWriter.close();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		exportMI.setAccelerator(KeyCombination.valueOf("Ctrl+E"));
		
		super.getItems().addAll(importMI, exportMI);
	}
}
