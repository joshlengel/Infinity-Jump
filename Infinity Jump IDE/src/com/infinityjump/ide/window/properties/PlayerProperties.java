package com.infinityjump.ide.window.properties;

import java.math.BigDecimal;

import com.infinityjump.core.game.base.Player;
import com.infinityjump.ide.window.leveleditor.LevelView;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class PlayerProperties extends PropertyPane {

	private Player player;
	private TextField xField, yField, sizeField;
	
	public PlayerProperties(Player player, LevelView view) {
		this.player = player;
		
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
				player.setX(new BigDecimal(n));
				view.repaint();
			}
		});
		
		yDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				player.setY(new BigDecimal(n));
				view.repaint();
			}
		});
		
		sizeDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				player.setSize(new BigDecimal(n));
				view.repaint();
			}
		});
		
		xField.setText(Float.toString(player.getX().floatValue()));
		yField.setText(Float.toString(player.getY().floatValue()));
		sizeField.setText(Float.toString(player.getSize().floatValue()));
		
		edgesProperty.setContent(edges);
		
		super.getChildren().add(edgesProperty);
	}

	@Override
	public void update() {
		xField.setText(Float.toString(player.getX().floatValue()));
		yField.setText(Float.toString(player.getY().floatValue()));
		sizeField.setText(Float.toString(player.getSize().floatValue()));
	}
}
