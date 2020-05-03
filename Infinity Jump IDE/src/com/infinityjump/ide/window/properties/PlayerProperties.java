package com.infinityjump.ide.window.properties;

import java.math.BigDecimal;

import com.infinityjump.core.game.base.Quad;
import com.infinityjump.ide.window.LevelView;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class PlayerProperties extends PropertyPane {

	private Quad quad;
	private TextField xField, yField, sizeField;
	
	public PlayerProperties(Quad player, LevelView view) {
		this.quad = player;
		
		TitledPane edgesProperty = new TitledPane();
		edgesProperty.setText("Player properties");
		
		GridPane edges = new GridPane();
		edges.setHgap(5.0d);
		
		edges.add(new Label("X:"), 0, 0);
		xField = new TextField();
		edges.add(xField, 1, 0);
		
		edges.add(new Label("Y:"), 0, 1);
		yField = new TextField();
		edges.add(yField, 1, 1);
		
		edges.add(new Label("Size:"), 0, 2);
		sizeField = new TextField();
		edges.add(sizeField, 1, 2);
		
		DoubleFormatter xDF = new DoubleFormatter();
		DoubleFormatter yDF = new DoubleFormatter();
		DoubleFormatter sizeDF = new DoubleFormatter();
		
		xField.setTextFormatter(xDF);
		yField.setTextFormatter(yDF);
		sizeField.setTextFormatter(sizeDF);
		
		xDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				float hSize = (float) (sizeDF.getValue() * 0.5);
				player.setLeft(new BigDecimal(n.floatValue() - hSize));
				player.setRight(new BigDecimal(n.floatValue() + hSize));
				view.repaint();
			}
		});
		
		yDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				float hSize = (float) (sizeDF.getValue() * 0.5);
				player.setBottom(new BigDecimal(n.floatValue() - hSize));
				player.setTop(new BigDecimal(n.floatValue() + hSize));
				view.repaint();
			}
		});
		
		sizeDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				float hSize = (float) (sizeDF.getValue() * 0.5);
				player.setLeft(new BigDecimal(xDF.getValue().floatValue() - hSize));
				player.setRight(new BigDecimal(xDF.getValue().floatValue() + hSize));
				player.setBottom(new BigDecimal(yDF.getValue().floatValue() - hSize));
				player.setTop(new BigDecimal(yDF.getValue().floatValue() + hSize));
				view.repaint();
			}
		});
		
		xField.setText(Float.toString((player.getLeft().floatValue() + player.getRight().floatValue()) * 0.5f));
		yField.setText(Float.toString((player.getBottom().floatValue() + player.getTop().floatValue()) * 0.5f));
		sizeField.setText(Float.toString(player.getRight().floatValue() - player.getLeft().floatValue()));
		
		edgesProperty.setContent(edges);
		
		super.getChildren().add(edgesProperty);
	}

	@Override
	public void update() {
		xField.setText(Float.toString((quad.getLeft().floatValue() + quad.getRight().floatValue()) * 0.5f));
		yField.setText(Float.toString((quad.getBottom().floatValue() + quad.getTop().floatValue()) * 0.5f));
		sizeField.setText(Float.toString(quad.getRight().floatValue() - quad.getLeft().floatValue()));
	}
}
