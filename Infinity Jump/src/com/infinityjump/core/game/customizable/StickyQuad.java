package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Quad;

public class StickyQuad extends Quad {

	private static final BigDecimal SURFACE_DRAG = new BigDecimal(10.0);

	private boolean playerStuck, leftSide;
	
	public StickyQuad(float left, float right, float bottom, float top) {
		this(new BigDecimal(left), new BigDecimal(right), new BigDecimal(bottom), new BigDecimal(top));
	}
	
	public StickyQuad(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
		
		this.type = Type.STICKY;
	}
	
	@Override
	public void update(Player player, Collision collision, BigDecimal dt) {
		if (playerStuck) {
			if (leftSide && player.getIVX().compareTo(BigDecimal.ZERO) < 0 || !leftSide && player.getIVX().compareTo(BigDecimal.ZERO) > 0) {
				playerStuck = false;
				return;
			} else {
				collision.quad = this;
				
				collision.time = BigDecimal.ZERO; // min time
				
				collision.px = player.getX();
				collision.py = player.getY();
				
				collision.type = leftSide? Collision.Type.LEFT : Collision.Type.RIGHT;
			}
		}
		
		super.update(player, collision, dt);
	}

	@Override
	public void collided(Player player, Collision collision, BigDecimal dt) {
		switch (collision.type) {
		case LEFT:
			collision.ivx = BigDecimal.ZERO;
			collision.ivy = BigDecimal.ZERO;
			
			collision.time = dt; // use up time
			
			collision.type = Collision.Type.TOP; // simulate top collision so player can jump
			
			collision.vx = BigDecimal.ZERO;
			collision.vy = BigDecimal.ZERO;
			
			playerStuck = true;
			leftSide = true;
			
			break;
			
		case RIGHT:
			collision.ivx = BigDecimal.ZERO;
			collision.ivy = BigDecimal.ZERO;
			
			collision.time = dt; // use up time
			
			collision.type = Collision.Type.TOP; // simulate top collision so player can jump
			
			collision.vx = BigDecimal.ZERO;
			collision.vy = BigDecimal.ZERO;
			
			playerStuck = true;
			leftSide = false;
			
			break;
			
		case BOTTOM:
			collision.ivy = BigDecimal.ZERO;
			
			collision.vx = player.getVX();
			collision.vy = BigDecimal.ZERO;
			break;
			
		case TOP:
			collision.ivy = BigDecimal.ZERO;
			
			collision.vx = player.getVX().multiply(one.subtract(SURFACE_DRAG.multiply(dt)));
			collision.vy = BigDecimal.ZERO;
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void render(Theme theme, float scrollX, float scrollY) {
		render(theme.getStickyQuadColor(), scrollX, scrollY);
	}
	
	@Override
	public StickyQuad clone() {
		return new StickyQuad(left.floatValue(), right.floatValue(), bottom.floatValue(), top.floatValue());
	}
}
