package com.infinityjump.ide.window;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.prefs.Preferences;

import com.infinityjump.core.game.Theme;
import com.infinityjump.game.Launcher;
import com.infinityjump.ide.Utils;
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
	
	private LevelView levelView;
	private LuaEditorView luaEditorView;
	
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
		
		Preferences prefs = Preferences.userRoot();
		
		InputStream levelStream = new ByteArrayInputStream(prefs.get("levelSource", Utils.getDefaultLevel()).getBytes());
		InputStream defaultScript = new ByteArrayInputStream(prefs.get("scriptSource", Utils.getDefaultScript()).getBytes());
		
		InputStream themeStream = new FileInputStream(assetDir + "default-theme.properties");
		Theme theme = new Theme(themeStream);
		themeStream.close();
		
		levelView = new LevelView(center, theme);
		levelView.load(levelStream);
		
		luaEditorView = new LuaEditorView();
		luaEditorView.load(defaultScript);
		
		levelStream.close();
		
		MenuBar menuBar = new MenuBar();
		
		FileMenu fileMenu = new FileMenu(levelView, luaEditorView);
		EditMenu editMenu = new EditMenu(levelView, luaEditorView);
		ViewMenu viewMenu = new ViewMenu(levelView);
		TestMenu testMenu = new TestMenu(levelView, luaEditorView, theme, assetDir);
		
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, testMenu);
		
		menuBar.setVisible(true);
		
		root.setTop(menuBar);
		
		center.getItems().add(levelView);
		
		editor.getItems().addAll(center, luaEditorView);
		
		root.setCenter(editor);

		stage.setScene(scene);
		stage.show();
		
		levelView.repaint();
	}
	
	@Override
	public void stop() {
		// save unsaved changes
		String level = levelView.makeLevelSource();
		String script = luaEditorView.getScript();
		
		Preferences prefs = Preferences.userRoot();
		prefs.put("levelSource", level);
		prefs.put("scriptSource", script);
	}
}
