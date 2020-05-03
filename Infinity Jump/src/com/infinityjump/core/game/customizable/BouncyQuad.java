package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Quad;

public class BouncyQuad extends Quad {

	private static final BigDecimal SURFACE_DRAG = new BigDecimal(10.0);

	private static final BigDecimal BOUNCYNESS = new BigDecimal(0.9);
	private static final BigDecimal EPSILON = new BigDecimal(0.0001);
	
	public BouncyQuad(float left, float right, float bottom, float top) {
		this(new BigDecimal(left), new BigDecimal(right), new BigDecimal(bottom), new BigDecimal(top));
	}
	
	public BouncyQuad(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
		
		this.type = Type.BOUNCY;
	}

	@Override
	public void collided(Player player, Collision collision, BigDecimal dt) {
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
			
			collision.vx = player.getVX().multiply(one.subtract(SURFACE_DRAG.multiply(dt)));
			collision.vy = (absVY.compareTo(EPSILON) < 0? BigDecimal.ZERO : absVY).multiply(BOUNCYNESS);
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void render(Theme theme, float scrollX, float scrollY) {
		render(theme.getBouncyQuadColor(), scrollX, scrollY);
	}
	
	@Override
	public BouncyQuad clone() {
		return new BouncyQuad(left.floatValue(), right.floatValue(), bottom.floatValue(), top.floatValue());
	}
}
