package com.infinityjump.ide.window.properties;

import java.math.BigDecimal;
import java.util.Map.Entry;

import com.infinityjump.core.game.base.Quad;
import com.infinityjump.core.game.base.Quad.Type;
import com.infinityjump.core.game.customizable.TeleportQuad;
import com.infinityjump.ide.window.LevelView;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class QuadProperties extends PropertyPane {

	private Quad quad;
	private TextField leftField, rightField, bottomField, topField;
	
	private TeleportQuadProperties tQuadProps;
	
	public QuadProperties(Entry<Integer, Quad> entry, LevelView view) {
		this.quad = entry.getValue();
		
		VBox properties = new VBox();
		
		TitledPane edgesProperty = new TitledPane();
		edgesProperty.setText("Quad properties");
		
		GridPane edges = new GridPane();
		edges.setHgap(5.0d);
		
		edges.add(new Label("ID:"), 0, 0);
		TextField idField = new TextField(Integer.toString(entry.getKey()));
		idField.setEditable(false);
		edges.add(idField, 1, 0);
		
		edges.add(new Label("Left:"), 0, 1);
		leftField = new TextField();
		edges.add(leftField, 1, 1);
		
		edges.add(new Label("Right:"), 0, 2);
		rightField = new TextField();
		edges.add(rightField, 1, 2);
		
		edges.add(new Label("Bottom:"), 0, 3);
		bottomField = new TextField();
		edges.add(bottomField, 1, 3);
		
		edges.add(new Label("Top:"), 0, 4);
		topField = new TextField();
		edges.add(topField, 1, 4);
		
		edges.add(new Label("Type:"), 0, 5);
		
		ComboBox<String> typeField = new ComboBox<String>();

		for (Quad.Type type : Quad.Type.values()) {
			typeField.getItems().add(type.toString());
		}
		
		edges.add(typeField, 1, 5);
		
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
				quad.setLeft(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		rightDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				quad.setRight(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		bottomDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				quad.setBottom(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		topDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				quad.setTop(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		typeField.setValue(quad.getType().toString());
		
		typeField.valueProperty().addListener((ov, o, n) -> {
			Type type = Type.parseType(n);
			
			quad.setType(Type.parseType(n));
			
			if (type == Type.TELEPORT) {
				TeleportQuad tQuad = (TeleportQuad)view.setNewQuadType(entry.getKey(), Type.TELEPORT);
				tQuadProps = new TeleportQuadProperties(tQuad, properties);
			} else {
				properties.getChildren().remove(tQuadProps);
			}
			
			view.repaint();
		});
		
		leftField.setText(Float.toString(quad.getLeft().floatValue()));
		rightField.setText(Float.toString(quad.getRight().floatValue()));
		bottomField.setText(Float.toString(quad.getBottom().floatValue()));
		topField.setText(Float.toString(quad.getTop().floatValue()));
		
		edgesProperty.setContent(edges);
		
		TitledPane miscProperty = new TitledPane();
		miscProperty.setText("Other");
		
		Button remove = new Button("Remove");
		
		remove.setOnAction(e -> {
			view.removeCurrentQuad(entry);
		});
		
		miscProperty.setContent(remove);
		
		properties.getChildren().addAll(edgesProperty, miscProperty);
		
		if (quad.getType() == Type.TELEPORT) {
			tQuadProps = new TeleportQuadProperties((TeleportQuad)quad, properties);
			properties.getChildren().add(tQuadProps);
		}
		
		super.getChildren().add(properties);
	}

	@Override
	public void update() {
		leftField.setText(Float.toString(quad.getLeft().floatValue()));
		rightField.setText(Float.toString(quad.getRight().floatValue()));
		bottomField.setText(Float.toString(quad.getBottom().floatValue()));
		topField.setText(Float.toString(quad.getTop().floatValue()));
	}
}
