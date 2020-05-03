package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Quad;
import com.infinityjump.core.game.sound.Sounds;
import com.infinityjump.core.state.GameState;
import com.infinityjump.core.state.StateMachine;

public class DeadlyQuad extends Quad {

	public DeadlyQuad(float left, float right, float bottom, float top) {
		this(new BigDecimal(left), new BigDecimal(right), new BigDecimal(bottom), new BigDecimal(top));
	}
	
	public DeadlyQuad(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
		
		this.type = Type.DEADLY;
	}
	
	@Override
	public void render(Theme theme, float scrollX, float scrollY) {
		render(theme.getDeadlyQuadColor(), scrollX, scrollY);
	}

	@Override
	public void collided(Player player, Collision collision, BigDecimal dt) {
		Sounds.playSound("die");
		((GameState)StateMachine.machine.getCurrentState()).restart();
	}
	
	@Override
	public DeadlyQuad clone() {
		return new DeadlyQuad(left.floatValue(), right.floatValue(), bottom.floatValue(), top.floatValue());
	}
}
