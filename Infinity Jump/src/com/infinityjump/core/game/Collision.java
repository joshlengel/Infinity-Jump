package com.infinityjump.core.game;

import java.math.BigDecimal;

import com.infinityjump.core.game.base.Quad;

public class Collision {

	public enum Type {
		LEFT,
		RIGHT,
		BOTTOM,
		TOP,
		NONE
	}
	
	public Quad quad;
	
	public BigDecimal time;
	
	public BigDecimal px, py;
	public BigDecimal ivx, ivy;
	public BigDecimal vx, vy;
	
	public Type type;
}
