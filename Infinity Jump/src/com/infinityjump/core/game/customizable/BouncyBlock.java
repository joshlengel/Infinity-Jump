package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Type;
import com.infinityjump.core.utils.BigConstants;
import com.infinityjump.core.game.base.Block;

public class BouncyBlock extends Block {

	/// TODO Move to config files
	private static final BigDecimal SURFACE_DRAG = new BigDecimal(10.0);
	private static final BigDecimal BOUNCYNESS = new BigDecimal(0.9);
	
	public BouncyBlock(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}
	
	@Override
	public Color getColor(Theme theme) {
		return theme.getBouncyQuadColor();
	}
	
	@Override
	public Type getType() {
		return Type.BOUNCY;
	}

	@Override
	public void resolveCollision(Player player, Collision collision) {
		switch (collision.type) {
		case LEFT:
			collision.vx = (player.getVX().compareTo(BigDecimal.ZERO) < 0? player.getVX().negate() : player.getVX()).multiply(BOUNCYNESS).negate();
			collision.vy = player.getVY();
			break;
			
		case RIGHT:
			collision.vx = (player.getVX().compareTo(BigDecimal.ZERO) < 0? player.getVX().negate() : player.getVX()).multiply(BOUNCYNESS);
			collision.vy = player.getVY();
			break;
			
		case BOTTOM:
			collision.vx = player.getVX();
			collision.vy = (player.getVY().compareTo(BigDecimal.ZERO) < 0? player.getVY().negate() : player.getVY()).multiply(BOUNCYNESS).negate();
			break;
			
		case TOP:
			BigDecimal absVY = player.getVY().compareTo(BigDecimal.ZERO) < 0? player.getVY().negate() : player.getVY();
			
			collision.vx = player.getVX().add(SURFACE_DRAG.multiply(player.getVX()).negate().multiply(cacheDT));
			collision.vy = (absVY.compareTo(BigConstants.SMALL_EPSILON) < 0? BigDecimal.ZERO : absVY).multiply(BOUNCYNESS);
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public BouncyBlock clone() {
		return new BouncyBlock(left, right, bottom, top);
	}
}
