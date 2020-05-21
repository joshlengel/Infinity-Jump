package com.infinityjump.ide.window.properties;

import java.math.BigDecimal;

import com.infinityjump.core.game.base.Boundary;
import com.infinityjump.ide.window.leveleditor.LevelView;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class BoundaryPropertiesPanel extends PropertyPanel {
	
	private Boundary boundary;
	private TextField leftField, rightField, bottomField, topField;

	public BoundaryPropertiesPanel(Boundary boundary, LevelView view) {
		this.boundary = boundary;
		
		TitledPane edgesProperty = new TitledPane();
		edgesProperty.setText("Boundary properties");
		
		GridPane edges = new GridPane();
		edges.setHgap(5.0d);
		
		edges.add(new Label("Left:"), 0, 0);
		leftField = new TextField();
		edges.add(leftField, 1, 0);
		
		edges.add(new Label("Right:"), 0, 1);
		rightField = new TextField();
		edges.add(rightField, 1, 1);
		
		edges.add(new Label("Bottom:"), 0, 2);
		bottomField = new TextField();
		edges.add(bottomField, 1, 2);
		
		edges.add(new Label("Top:"), 0, 3);
		topField = new TextField();
		edges.add(topField, 1, 3);
		
		DoubleFormatter leftDF = new DoubleFormatter();
		DoubleFormatter rightDF = new DoubleFormatter();
		DoubleFormatter bottomDF = new DoubleFormatter();
		DoubleFormatter topDF = new DoubleFormatter();
		
		leftField.setTextFormatter(leftDF);
		rightField.setTextFormatter(rightDF);
		bottomField.setTextFormatter(bottomDF);
		topField.setTextFormatter(topDF);
		
		leftDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				boundary.setLeft(new BigDecimal(n));
				view.repaint();
			}
		});
		
		rightDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				boundary.setRight(new BigDecimal(n));
				view.repaint();
			}
		});
		
		bottomDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				boundary.setBottom(new BigDecimal(n));
				view.repaint();
			}
		});
		
		topDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				boundary.setTop(new BigDecimal(n));
				view.repaint();
			}
		});
		
		leftField.setText(Float.toString(boundary.getLeft().floatValue()));
		rightField.setText(Float.toString(boundary.getRight().floatValue()));
		bottomField.setText(Float.toString(boundary.getBottom().floatValue()));
		topField.setText(Float.toString(boundary.getTop().floatValue()));
		
		edgesProperty.setContent(edges);
		
		super.getChildren().add(edgesProperty);
	}

	@Override
	public void update() {
		leftField.setText(Float.toString(boundary.getLeft().floatValue()));
		rightField.setText(Float.toString(boundary.getRight().floatValue()));
		bottomField.setText(Float.toString(boundary.getBottom().floatValue()));
		topField.setText(Float.toString(boundary.getTop().floatValue()));
	}
}
