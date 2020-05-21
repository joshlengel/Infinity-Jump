package com.infinityjump.ide.window.menu;

import com.infinityjump.ide.window.leveleditor.LevelView;

import javafx.geometry.Orientation;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class ViewMenu extends Menu {

	public ViewMenu(LevelView levelView) {
		super("_View");
		
		CustomMenuItem adjustZoomMI = new CustomMenuItem();
		
		BorderPane adjustZoomPane = new BorderPane();
		
		Slider adjustZoomSlider = new Slider();
		adjustZoomSlider.setValue(100.0);
		adjustZoomSlider.setOrientation(Orientation.HORIZONTAL);
		adjustZoomSlider.valueProperty().addListener((ov, o, n) -> {
			if (n != null) levelView.setZoom(n.floatValue() * 0.01f * 0.75f + 0.25f);
		});
		
		Label adjustZoomLabel = new Label("Adjust zoom");
		adjustZoomLabel.setTextFill(Color.BLACK);
		adjustZoomPane.setTop(adjustZoomLabel);
		adjustZoomPane.setCenter(adjustZoomSlider);
		
		adjustZoomMI.setContent(adjustZoomPane);
		
		super.getItems().add(adjustZoomMI);
	}
}
