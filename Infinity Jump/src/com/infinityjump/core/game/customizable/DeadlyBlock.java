package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Type;
import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.sound.Sounds;
import com.infinityjump.core.state.GameState;
import com.infinityjump.core.state.StateMachine;

public class DeadlyBlock extends Block {

	public DeadlyBlock(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}
	
	@Override
	public Color getColor(Theme theme) {
		return theme.getDeadlyQuadColor();
	}
	
	@Override
	public Type getType() {
		return Type.DEADLY;
	}

	@Override
	public void resolveCollision(Player player, Collision collision) {
		Sounds.playSound("die");
		((GameState)StateMachine.machine.getCurrentState()).restart();
	}
	
	@Override
	public DeadlyBlock clone() {
		return new DeadlyBlock(left, right, bottom, top);
	}
}
