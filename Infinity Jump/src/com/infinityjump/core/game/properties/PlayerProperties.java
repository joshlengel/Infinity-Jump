package com.infinityjump.core.game.properties;

import java.math.BigDecimal;

public class PlayerProperties extends QuadProperties {

	public BigDecimal gravity, airDrag;
	public BigDecimal onHitEmissionVelocityThreshold, emissionSpawnLength;
	public float emissionGravity, emissionSpeed, emissionSpawnRate, emissionQuadSize;
	public double emissionSpread, emissionLifespan;
	public float emissionQuadColorVariance;
	
	@Override
	protected boolean parse(String key, String value) {
		if (super.parse(key, value)) return true;
		
		switch (key) {
		case "gravity":                             gravity                        = new BigDecimal(value); return true;
		case "air-drag":                            airDrag                        = new BigDecimal(value); return true;
		case "on-hit-emission-velocity-threshold" : onHitEmissionVelocityThreshold = new BigDecimal(value); return true;
		case "emission-spawn-length" :              emissionSpawnLength            = new BigDecimal(value); return true;
		case "emission-gravity" :                   emissionGravity                = Float.parseFloat(value); return true;
		case "emission-spread" :                    emissionSpread                 = Double.parseDouble(value); return true;
		case "emission-speed" :                     emissionSpeed                  = Float.parseFloat(value); return true;
		case "emission-lifespan" :                  emissionLifespan               = Double.parseDouble(value); return true;
		case "emission-spawn-rate" :                emissionSpawnRate              = Float.parseFloat(value); return true;
		case "emission-quad-size" :                 emissionQuadSize               = Float.parseFloat(value); return true;
		case "emission-quad-color-variance":        emissionQuadColorVariance      = Float.parseFloat(value); return true;
		default: return false;
		}
	}
}
