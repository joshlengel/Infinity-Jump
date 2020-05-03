package com.infinityjump.core.game.base;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.state.GameState;
import com.infinityjump.core.state.StateMachine;

public class Target extends Quad {

	public static final double REQUIRED_FINISH_TIME = 1.0;
	
	private double timer;
	
	public Target(float left, float right, float bottom, float top) {
		super(left, right, bottom, top);
	}

	@Override
	public void render(Theme theme, float scrollX, float scrollY) {
		render(theme.getTargetColor(), scrollX, scrollY);
	}
	
	@Override
	public void update(Player player, Collision collision, BigDecimal dt) {
		// no collision detection
		
		if (player.left.compareTo(left) >= 0 && player.right.compareTo(right) <= 0 && player.bottom.compareTo(bottom) >= 0 && player.top.compareTo(top) <= 0) {
			timer += dt.doubleValue();
			
			if (timer >= REQUIRED_FINISH_TIME) {
				((GameState)StateMachine.machine.getCurrentState()).changeState();
			}
		} else {
			timer = 0.0d;
		}
	}
}
