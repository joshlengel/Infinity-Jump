package com.infinityjump.ide.window.menu;

import com.infinityjump.ide.window.leveleditor.MouseHandler;

import javafx.geometry.Orientation;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class SettingsMenu extends Menu {

	public SettingsMenu() {
		super("_Settings");
		
		CustomMenuItem msMI = new CustomMenuItem();
		
		BorderPane msPane = new BorderPane();
		
		Slider sensitivity = new Slider();
		sensitivity.setValue(sensitivityToSliderVal(1.0));
		MouseHandler.setSensitivity(1.0);
		sensitivity.setOrientation(Orientation.HORIZONTAL);
		sensitivity.valueProperty().addListener((ov, o, n) -> {
			if (n != null) MouseHandler.setSensitivity(sliderValToSensitivity(n.doubleValue()));
		});
		
		Label sensitivityLabel = new Label("Sensitivity");
		sensitivityLabel.setTextFill(Color.BLACK);
		
		msPane.setTop(sensitivityLabel);
		msPane.setCenter(sensitivity);
		
		msMI.setContent(msPane);
		
		super.getItems().add(msMI);
	}
	
	private static double sliderValToSensitivity(double sliderVal) {
		return sliderVal * 0.01 * 1.5 + 0.5;
	}
	
	private static double sensitivityToSliderVal(double sensitivity) {
		return (sensitivity - 0.5) / 1.5 * 100;
	}
}
