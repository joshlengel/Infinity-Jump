package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Type;
import com.infinityjump.core.game.base.Block;

public class StickyBlock extends Block {

	private boolean playerStuck, leftSide;
	
	public StickyBlock(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}
	
	@Override
	public Type getType() {
		return Type.STICKY;
	}
	
	@Override
	public void checkCollision(Player player, Collision collision) {
		if (playerStuck) {
			if (leftSide && player.getIVX().compareTo(BigDecimal.ZERO) < 0 || !leftSide && player.getIVX().compareTo(BigDecimal.ZERO) > 0) {
				playerStuck = false;
				return;
			} else {
				collision.quad = this;
				
				collision.time = cacheDT; // min time
				
				collision.px = player.getX();
				collision.py = player.getY();
				
				collision.type = leftSide? Collision.Type.LEFT : Collision.Type.RIGHT;
			}
		}
		
		super.checkCollision(player, collision);
	}

	@Override
	public void resolveCollision(Player player, Collision collision) {
		switch (collision.type) {
		case LEFT:
			collision.ivx = BigDecimal.ZERO;
			collision.ivy = BigDecimal.ZERO;
			
			collision.type = Collision.Type.TOP; // simulate top collision so player can jump
			
			collision.vx = BigDecimal.ZERO;
			collision.vy = BigDecimal.ZERO;
			
			playerStuck = true;
			leftSide = true;
			
			break;
			
		case RIGHT:
			collision.ivx = BigDecimal.ZERO;
			collision.ivy = BigDecimal.ZERO;
			
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
			
			collision.vx = player.getVX().add(cacheSurfaceDrag.multiply(player.getVX()).negate().multiply(cacheDT));
			collision.vy = BigDecimal.ZERO;
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public StickyBlock clone() {
		return new StickyBlock(left, right, bottom, top);
	}
}
