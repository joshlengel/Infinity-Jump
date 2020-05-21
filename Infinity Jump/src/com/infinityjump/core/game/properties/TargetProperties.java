package com.infinityjump.core.game.properties;

public class TargetProperties extends QuadProperties {

	public double requiredFinishTime;
	
	@Override
	protected boolean parse(String key, String value) {
		if (super.parse(key, value)) return true;
		
		switch (key) {
		case "required-finish-time": requiredFinishTime = Double.parseDouble(value); return true;
		default: return false;
		}
	}
}
