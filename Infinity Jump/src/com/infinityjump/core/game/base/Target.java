package com.infinityjump.core.game.base;

import java.math.BigDecimal;

import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.quad.QuadShapeImpl;
import com.infinityjump.core.game.level.Level;
import com.infinityjump.core.game.properties.TargetProperties;
import com.infinityjump.core.state.GameState;
import com.infinityjump.core.state.StateMachine;

public class Target extends QuadShapeImpl {

	private double timer;
	
	public Target(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}

	@Override
	public Type getType() {
		return Type.TARGET;
	}
	
	public boolean update(Level level, Theme theme, BigDecimal dt) {
		// no collision detection
		
		Player player = level.getPlayer();
		
		if (player.getLeft().compareTo(left) >= 0
				&& player.getRight().compareTo(right) <= 0
				&& player.getBottom().compareTo(bottom) >= 0
				&& player.getTop().compareTo(top) <= 0) {
			timer += dt.doubleValue();
			
			if (timer >= ((TargetProperties)theme.getProperties("target")).requiredFinishTime) {
				((GameState)StateMachine.machine.getCurrentState()).changeState();
				return false;
			}
		} else {
			timer = 0.0d;
		}
		
		return true;
	}
}
