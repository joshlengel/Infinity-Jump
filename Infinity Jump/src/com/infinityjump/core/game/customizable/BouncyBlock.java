package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Type;
import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.properties.BouncyProperties;
import com.infinityjump.core.utils.BigConstants;

public class BouncyBlock extends Block {

	protected BigDecimal cacheBounciness;
	
	public BouncyBlock(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}
	
	@Override
	public Type getType() {
		return Type.BOUNCY;
	}

	@Override
	public void update(Level level, Theme theme, BigDecimal dt) {
		super.update(level, theme, dt);
		
		cacheBounciness = ((BouncyProperties)theme.getProperties("bouncy")).bounciness;
	}
	
	@Override
	public void resolveCollision(Player player, Collision collision) {
		switch (collision.type) {
		case LEFT:
			collision.vx = (player.getVX().compareTo(BigDecimal.ZERO) < 0? player.getVX().negate() : player.getVX()).multiply(cacheBounciness).negate();
			collision.vy = player.getVY();
			break;
			
		case RIGHT:
			collision.vx = (player.getVX().compareTo(BigDecimal.ZERO) < 0? player.getVX().negate() : player.getVX()).multiply(cacheBounciness);
			collision.vy = player.getVY();
			break;
			
		case BOTTOM:
			collision.vx = player.getVX();
			collision.vy = (player.getVY().compareTo(BigDecimal.ZERO) < 0? player.getVY().negate() : player.getVY()).multiply(cacheBounciness).negate();
			break;
			
		case TOP:
			BigDecimal absVY = player.getVY().compareTo(BigDecimal.ZERO) < 0? player.getVY().negate() : player.getVY();
			
			collision.vx = player.getVX().add(cacheSurfaceDrag.multiply(player.getVX()).negate().multiply(cacheDT));
			collision.vy = (absVY.compareTo(BigConstants.SMALL_EPSILON) < 0? BigDecimal.ZERO : absVY).multiply(cacheBounciness);
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
