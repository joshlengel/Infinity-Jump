package com.infinityjump.ide.window;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.prefs.Preferences;

import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.script.Script;
import com.infinityjump.game.Launcher;
import com.infinityjump.ide.Utils;
import com.infinityjump.ide.window.leveleditor.LevelData;
import com.infinityjump.ide.window.leveleditor.LevelView;
import com.infinityjump.ide.window.menu.EditMenu;
import com.infinityjump.ide.window.menu.FileMenu;
import com.infinityjump.ide.window.menu.SettingsMenu;
import com.infinityjump.ide.window.menu.TestMenu;
import com.infinityjump.ide.window.menu.ViewMenu;
import com.infinityjump.ide.window.scripteditor.LuaConsole;
import com.infinityjump.ide.window.scripteditor.LuaConsoleView;
import com.infinityjump.ide.window.scripteditor.LuaEditorView;

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
	private LuaConsoleView luaConsoleView;
	
	public static void main(String[] args) {
		assetDir = args[0];
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Launcher.init(assetDir);
		GlobalProperties.init(assetDir);
		LevelData.init();
		LuaConsole.init();
		
		stage.setTitle("Infinity Jump IDE");
		
		BorderPane root = new BorderPane();
		
		Scene scene = new Scene(root, 800, 600);
		
		SplitPane editor = new SplitPane();
		
		SplitPane levelEditor = new SplitPane();
		levelEditor.setOrientation(Orientation.HORIZONTAL);
		
		SplitPane luaEditor = new SplitPane();
		luaEditor.setOrientation(Orientation.VERTICAL);
		
		Preferences prefs = Preferences.userRoot();
		
		InputStream levelStream = new ByteArrayInputStream(prefs.get("levelSource", Utils.getDefaultLevel()).getBytes());
		InputStream defaultScript = new ByteArrayInputStream(prefs.get("scriptSource", Utils.getDefaultScript()).getBytes());
		
		InputStream themeStream = new FileInputStream(assetDir + "default-theme.properties");
		Theme theme = new Theme(themeStream);
		themeStream.close();
		
		levelView = new LevelView(levelEditor, theme);
		levelView.load(levelStream);
		
		luaEditorView = new LuaEditorView();
		luaEditorView.load(defaultScript);
		
		luaConsoleView = new LuaConsoleView();
		
		Script.setConsole(new PrintStream(luaConsoleView.getConsole()));
		
		levelStream.close();
		
		MenuBar menuBar = new MenuBar();
		
		FileMenu fileMenu = new FileMenu(levelView, luaEditorView);
		EditMenu editMenu = new EditMenu(levelView, luaEditorView);
		ViewMenu viewMenu = new ViewMenu(levelView);
		TestMenu testMenu = new TestMenu(levelView, luaEditorView, theme, assetDir);
		SettingsMenu settingsMenu = new SettingsMenu();
		
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, testMenu, settingsMenu);
		
		menuBar.setVisible(true);
		
		root.setTop(menuBar);
		
		levelEditor.getItems().add(levelView);
		luaEditor.getItems().addAll(luaEditorView, luaConsoleView);
		
		editor.getItems().addAll(levelEditor, luaEditor);
		
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
