package com.infinityjump.ide.window.properties;

import java.math.BigDecimal;
import java.util.Map.Entry;

import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.base.type.Type;
import com.infinityjump.core.game.customizable.TeleportBlock;
import com.infinityjump.ide.window.leveleditor.LevelView;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BlockPropertiesPanel extends PropertyPanel {

	private Block block;
	private TextField leftField, rightField, bottomField, topField;
	
	private TeleportBlockPropertiesPanel tBlockProps;
	
	public BlockPropertiesPanel(Entry<Integer, Block> entry, LevelView view) {
		this.block = entry.getValue();
		
		VBox properties = new VBox();
		
		TitledPane edgesProperty = new TitledPane();
		edgesProperty.setText("Block properties");
		
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

		for (Type type : Type.CUSTOMIZABLES) {
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
				block.setLeft(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		rightDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				block.setRight(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		bottomDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				block.setBottom(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		topDF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				block.setTop(new BigDecimal(n.floatValue()));
				view.repaint();
			}
		});
		
		typeField.setValue(block.getType().toString());
		
		typeField.valueProperty().addListener((ov, o, n) -> {
			Type type = Type.parseType(n);
			
			block = view.setNewBlockType(entry.getKey(), type);
			
			if (type == Type.TELEPORT) {
				TeleportBlock tBlock = (TeleportBlock)block;
				tBlockProps = new TeleportBlockPropertiesPanel(tBlock, properties, view);
			} else {
				properties.getChildren().remove(tBlockProps);
			}
			
			view.repaint();
		});
		
		leftField.setText(Float.toString(block.getLeft().floatValue()));
		rightField.setText(Float.toString(block.getRight().floatValue()));
		bottomField.setText(Float.toString(block.getBottom().floatValue()));
		topField.setText(Float.toString(block.getTop().floatValue()));
		
		edgesProperty.setContent(edges);
		
		TitledPane miscProperty = new TitledPane();
		miscProperty.setText("Other");
		
		Button remove = new Button("Remove");
		
		remove.setOnAction(e -> {
			view.removeCurrentBlock(entry);
		});
		
		miscProperty.setContent(remove);
		
		properties.getChildren().addAll(edgesProperty, miscProperty);
		
		if (block.getType() == Type.TELEPORT) {
			tBlockProps = new TeleportBlockPropertiesPanel((TeleportBlock)block, properties, view);
			properties.getChildren().add(tBlockProps);
		}
		
		super.getChildren().add(properties);
	}

	@Override
	public void update() {
		leftField.setText(Float.toString(block.getLeft().floatValue()));
		rightField.setText(Float.toString(block.getRight().floatValue()));
		bottomField.setText(Float.toString(block.getBottom().floatValue()));
		topField.setText(Float.toString(block.getTop().floatValue()));
	}
}
