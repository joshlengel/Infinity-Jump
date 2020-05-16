package com.infinityjump.ide.window.properties;

import com.infinityjump.core.game.customizable.TeleportQuad;
import com.infinityjump.core.game.customizable.TeleportQuad.EjectType;
import com.infinityjump.ide.window.LevelView;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TeleportQuadProperties extends PropertyPane {

	private TeleportQuad quad;
	private TextField linkIDField;
	
	public TeleportQuadProperties(TeleportQuad quad, VBox parent, LevelView view) {
		this.quad = quad;
		
		TitledPane channelProperty = new TitledPane();
		channelProperty.setText("Teleport Quad Properties");
		
		GridPane channel = new GridPane();
		channel.setHgap(5.0d);
		
		channel.add(new Label("Channel:"), 0, 0);
		linkIDField = new TextField();
		channel.add(linkIDField, 1, 0);
		
		ComboBox<String> ejectType = new ComboBox<String>();
		
		for (EjectType type : EjectType.values()) {
			ejectType.getItems().add(type.toString());
		}
		
		channel.add(new Label("Eject Type:"), 0, 1);
		channel.add(ejectType, 1, 1);
		
		IntFormatter linkIF = new IntFormatter();
		linkIDField.setTextFormatter(linkIF);
		
		linkIF.valueProperty().addListener((ov, o, n) -> {
			if (n != null) {
				quad.setChannel(n);
			}
		});
		
		ejectType.valueProperty().addListener((ov, o, n) -> {
			EjectType type = EjectType.parseType(n);
			quad.setEjectType(type);
			
			view.repaint();
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
