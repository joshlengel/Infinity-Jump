package com.infinityjump.core.game.base;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.properties.BlockProperties;
import com.infinityjump.core.game.base.quad.QuadCollidable;
import com.infinityjump.core.game.base.quad.QuadShapeImpl;
import com.infinityjump.core.game.base.type.Type;
import com.infinityjump.core.game.level.Level;

public class Boundary extends QuadShapeImpl implements QuadCollidable {

	private BigDecimal cacheDT;
	private BigDecimal cacheSurfaceDrag;
	
	public Boundary(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}
	
	@Override
	public Type getType() {
		return Type.BOUNDARY;
	}

	@Override
	public void update(Level level, Theme theme, BigDecimal dt) {
		cacheDT = dt;
		cacheSurfaceDrag = ((BlockProperties)theme.getProperties("normal")).surfaceDrag;
	}
	
	@Override
	public void checkCollision(Player player, Collision collision) {
		// calculate collision
		if (player.getLeft().compareTo(left) >= 0 && player.getIVX().compareTo(BigDecimal.ZERO) < 0) {
			// ix = left
			// ix = player.vx * t + player.getLeft()
			// t = (left - player.getLeft()) / player.vx
			
			if (player.getIVX().compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = left.subtract(player.getLeft()).divide(player.getIVX(), 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = left.add(player.getHWidth());
					collision.py = player.getY().add(player.getIVY().multiply(time));
					
					collision.time = time;
					
					collision.type = Collision.Type.LEFT;
				}
			}
		} else if (right.compareTo(player.getRight()) >= 0 && player.getIVX().compareTo(BigDecimal.ZERO) > 0) {
			// ix = right
			// ix = player.vx * t + player.getRight()
			// t = (right - player.getRight()) / player.vx
			
			if (player.getIVX().compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = right.subtract(player.getRight()).divide(player.getIVX(), 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = right.subtract(player.getHWidth());
					collision.py = player.getY().add(player.getIVY().multiply(time));
					
					collision.time = time;
					
					collision.type = Collision.Type.RIGHT;
				}
			}
		}
		
		if (player.getBottom().compareTo(bottom) >= 0 && player.getIVY().compareTo(BigDecimal.ZERO) < 0) {
			// ix = bottom
			// ix = player.vy * t + player.getBottom()
			// t = (bottom - player.getBottom()) / player.vy
			
			if (player.getIVY().compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = bottom.subtract(player.getBottom()).divide(player.getIVY(), 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = player.getX().add(player.getIVX().multiply(time));
					collision.py = bottom.add(player.getHHeight());
					
					collision.time = time;
					
					collision.type = Collision.Type.TOP;
				}
			}
		} else if (top.compareTo(player.getTop()) >= 0 && player.getIVY().compareTo(BigDecimal.ZERO) > 0) {
			// ix = top
			// ix = player.vy * t + player.getTop()
			// t = (top - player.getTop()) / player.vy
			
			if (player.getIVY().compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = top.subtract(player.getTop()).divide(player.getIVY(), 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = player.getX().add(player.getIVX().multiply(time));
					collision.py = top.subtract(player.getHHeight());
					
					collision.time = time;
					
					collision.type = Collision.Type.BOTTOM;
				}
			}
		}
	}
	
	@Override
	public void resolveCollision(Player player, Collision collision) {
		switch (collision.type) {
		case LEFT:
			if (vx.compareTo(BigDecimal.ZERO) < 0) {
				collision.ivx = vx;
			} else {
				collision.ivx = BigDecimal.ZERO;
			}
			
			collision.vx = BigDecimal.ZERO;
			collision.vy = player.getVY();
			
			break;
		case RIGHT:
			if (vx.compareTo(BigDecimal.ZERO) > 0) {
				collision.ivx = vx;
			} else {
				collision.ivx = BigDecimal.ZERO;
			}
			
			collision.vx = BigDecimal.ZERO;
			collision.vy = player.getVY();
			break;
			
		case BOTTOM:
			if (vy.compareTo(BigDecimal.ZERO) < 0) {
				collision.ivy = vy;
			} else {
				collision.ivy = BigDecimal.ZERO;
			}
			
			collision.vx = player.getVX();
			collision.vy = BigDecimal.ZERO;
			break;
			
		case TOP:
			if (vy.compareTo(BigDecimal.ZERO) > 0) {
				collision.ivy = vy;
			} else {
				collision.ivy = BigDecimal.ZERO;
			}
			
			collision.vx = player.getVX().add(cacheSurfaceDrag.multiply(player.getVX()).negate().multiply(cacheDT));
			collision.vy = BigDecimal.ZERO;
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void updateFrame(Level level, BigDecimal elapsed) {}
}
