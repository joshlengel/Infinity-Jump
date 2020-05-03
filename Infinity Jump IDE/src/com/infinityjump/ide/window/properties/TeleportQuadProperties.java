package com.infinityjump.ide.window.properties;

import com.infinityjump.core.game.customizable.TeleportQuad;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TeleportQuadProperties extends PropertyPane {

	private TeleportQuad quad;
	private TextField linkIDField;
	
	public TeleportQuadProperties(TeleportQuad quad, VBox parent) {
		this.quad = quad;
		
		TitledPane channelProperty = new TitledPane();
		channelProperty.setText("Teleport Quad Properties");
		
		GridPane channel = new GridPane();
		channel.setHgap(5.0d);
		
		channel.add(new Label("Channel:"), 0, 0);
		linkIDField = new TextField();
		channel.add(linkIDField, 1, 0);
		
		IntFormatter linkIF = new IntFormatter();
		linkIDField.setTextFormatter(linkIF);
		
		linkIF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				quad.setChannel(n);
			}
		});
		
		linkIDField.setText(Integer.toString(quad.getChannel()));
		
		channelProperty.setContent(channel);
		
		parent.getChildren().add(channelProperty);
	}
	
	@Override
	public void update() {
		linkIDField.setText(Integer.toString(quad.getChannel()));
	}
}
