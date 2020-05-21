package com.infinityjump.core.game.properties;

import java.math.BigDecimal;

public class PlayerProperties extends QuadProperties {

	public BigDecimal gravity, airDrag;
	
	@Override
	protected boolean parse(String key, String value) {
		if (super.parse(key, value)) return true;
		
		switch (key) {
		case "gravity":  gravity = new BigDecimal(value); return true;
		case "air-drag": airDrag = new BigDecimal(value); return true;
		default: return false;
		}
	}
}
