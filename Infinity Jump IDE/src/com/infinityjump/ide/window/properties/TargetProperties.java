package com.infinityjump.ide.window.properties;

import java.math.BigDecimal;

import com.infinityjump.core.game.base.Quad;
import com.infinityjump.ide.window.LevelView;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class TargetProperties extends PropertyPane {

	private Quad quad;
	private TextField leftField, rightField, bottomField, topField;
	
	public TargetProperties(Quad target, LevelView view) {
		this.quad = target;
		
		TitledPane edgesProperty = new TitledPane();
		edgesProperty.setText("Target properties");
		
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
				target.setLeft(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		rightDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				target.setRight(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		bottomDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				target.setBottom(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		topDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				target.setTop(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		leftField.setText(Float.toString(target.getLeft().floatValue()));
		rightField.setText(Float.toString(target.getRight().floatValue()));
		bottomField.setText(Float.toString(target.getBottom().floatValue()));
		topField.setText(Float.toString(target.getTop().floatValue()));
		
		edgesProperty.setContent(edges);
		
		super.getChildren().add(edgesProperty);
	}

	@Override
	public void update() {
		leftField.setText(Float.toString(quad.getLeft().floatValue()));
		rightField.setText(Float.toString(quad.getRight().floatValue()));
		bottomField.setText(Float.toString(quad.getBottom().floatValue()));
		topField.setText(Float.toString(quad.getTop().floatValue()));
	}
}
