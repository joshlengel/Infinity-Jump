package com.infinityjump.ide.window;

import java.io.FileInputStream;
import java.io.InputStream;

import com.infinityjump.core.game.Theme;
import com.infinityjump.game.Launcher;
import com.infinityjump.ide.window.leveleditor.LevelView;
import com.infinityjump.ide.window.menu.EditMenu;
import com.infinityjump.ide.window.menu.FileMenu;
import com.infinityjump.ide.window.menu.TestMenu;
import com.infinityjump.ide.window.menu.ViewMenu;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Window extends Application {

	private static String assetDir;
	
	public static void main(String[] args) {
		assetDir = args[0];
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Launcher.init(assetDir);
		
		stage.setTitle("Infinity Jump IDE");
		
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 800, 600);
		
		SplitPane editor = new SplitPane();
		
		SplitPane center = new SplitPane();
		center.setOrientation(Orientation.HORIZONTAL);
		
		InputStream levelStream = new FileInputStream(assetDir + "default_level.lev");
		InputStream defaultScript = new FileInputStream(assetDir + "default_script.lua");
		
		InputStream themeStream = new FileInputStream(assetDir + "default-theme.properties");
		Theme theme = new Theme(themeStream);
		themeStream.close();
		
		LevelView levelView = new LevelView(center, theme);
		levelView.load(levelStream);
		
		LuaEditorView ijsEditor = new LuaEditorView();
		ijsEditor.load(defaultScript);
		
		levelStream.close();
		
		MenuBar menuBar = new MenuBar();
		
		FileMenu fileMenu = new FileMenu(levelView, ijsEditor);
		EditMenu editMenu = new EditMenu(levelView);
		ViewMenu viewMenu = new ViewMenu(levelView);
		TestMenu testMenu = new TestMenu(levelView, ijsEditor, theme, assetDir);
		
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, testMenu);
		
		menuBar.setVisible(true);
		
		root.setTop(menuBar);
		
		center.getItems().add(levelView);
		
		editor.getItems().addAll(center, ijsEditor);
		
		root.setCenter(editor);

		stage.setScene(scene);
		stage.show();
		
		levelView.repaint();
	}
}
