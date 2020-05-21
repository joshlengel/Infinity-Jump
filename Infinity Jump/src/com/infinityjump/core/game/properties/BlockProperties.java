package com.infinityjump.core.game.properties;

import java.math.BigDecimal;

public class BlockProperties extends QuadProperties {

	public BigDecimal surfaceDrag;
	
	@Override
	protected boolean parse(String key, String value) {
		if (super.parse(key, value)) return true;
		
		switch (key) {
		case "surface-drag": surfaceDrag = new BigDecimal(value); return true;
		default: return false;
		}
	}
}
