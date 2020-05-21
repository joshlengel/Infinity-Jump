package com.infinityjump.core.game.base;

import java.math.BigDecimal;

import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.quad.QuadRenderable;
import com.infinityjump.core.game.base.quad.QuadShapeImpl;
import com.infinityjump.core.state.GameState;
import com.infinityjump.core.state.StateMachine;

public class Target extends QuadShapeImpl implements QuadRenderable {

	public static final double REQUIRED_FINISH_TIME = 1.0;
	
	private double timer;
	
	public Target(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}

	@Override
	public Color getColor(Theme theme) {
		return theme.getTargetColor();
	}
	
	@Override
	public Type getType() {
		return Type.TARGET;
	}
	
	public boolean update(Level level, BigDecimal dt) {
		// no collision detection
		
		Player player = level.getPlayer();
		
		if (player.getLeft().compareTo(left) >= 0
				&& player.getRight().compareTo(right) <= 0
				&& player.getBottom().compareTo(bottom) >= 0
				&& player.getTop().compareTo(top) <= 0) {
			timer += dt.doubleValue();
			
			if (timer >= REQUIRED_FINISH_TIME) {
				((GameState)StateMachine.machine.getCurrentState()).changeState();
				return false;
			}
		} else {
			timer = 0.0d;
		}
		
		return true;
	}
}
