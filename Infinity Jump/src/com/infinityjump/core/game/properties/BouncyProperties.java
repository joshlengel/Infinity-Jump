package com.infinityjump.core.game.properties;

import java.math.BigDecimal;

public class BouncyProperties extends BlockProperties {
	
	public BigDecimal bounciness;
	
	@Override
	protected boolean parse(String key, String value) {
		if (super.parse(key, value)) return true;
		
		switch (key) {
		case "bounciness": bounciness = new BigDecimal(value); return true;
		default: return false;
		}
	}
}
