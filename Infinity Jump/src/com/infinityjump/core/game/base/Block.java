package com.infinityjump.core.game.base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.quad.QuadCollidable;
import com.infinityjump.core.game.base.quad.QuadRenderable;
import com.infinityjump.core.game.base.quad.QuadShapeImpl;

public class Block extends QuadShapeImpl implements QuadRenderable, QuadCollidable {
	
	/// TODO Move to config files
	private static final BigDecimal SURFACE_DRAG = new BigDecimal(5.0);
	
	protected BigDecimal cacheDT;
	
	public Block(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}
	
	@Override
	public void update(Level level, BigDecimal dt) {
		cacheDT = dt;
	}
	
	@Override
	public void updateFrame(Level level, BigDecimal elapsed) {
		BigDecimal dx = vx.multiply(elapsed);
		BigDecimal dy = vy.multiply(elapsed);
		
		x = x.add(dx);
		y = y.add(dy);
		
		left = left.add(dx);
		right = right.add(dx);
		bottom = bottom.add(dy);
		top = top.add(dy);
	}
	
	@Override
	public void checkCollision(Player player, Collision collision) {
		// calculate collision
		if (player.getLeft().compareTo(right) >= 0 && player.ivx.compareTo(vx) < 0) {
			// ix = vx * t + right
			// ix = player.vx * t + player.left
			// t = (player.left - right) / (vx - player.vx)
			
			BigDecimal dv = vx.subtract(player.ivx);
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.getLeft().subtract(right).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dY = vy.multiply(time);
					BigDecimal pDY = player.ivy.multiply(time);
					
					BigDecimal nBottom = bottom.add(dY);
					BigDecimal nTop = top.add(dY);
					
					BigDecimal nPBottom = player.getBottom().add(pDY);
					BigDecimal nPTop = player.getTop().add(pDY);
					
					if (nPTop.compareTo(nBottom) > 0 && nPBottom.compareTo(nTop) < 0) {
						collision.quad = this;
						
						collision.px = right.add(vx.multiply(time)).add(player.getHWidth());
						collision.py = player.getY().add(pDY);
						
						collision.time = time;
						
						collision.type = Collision.Type.RIGHT;
					}
				}
			}
		} else if (left.compareTo(player.getRight()) >= 0 && player.getIVX().compareTo(vx) > 0) {
			// ix = vx * t + left
			// ix = player.vx * t + player.getRight()
			// t = (player.getRight() - left) / (vx - player.vx)
			
			BigDecimal dv = vx.subtract(player.getIVX());
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.getRight().subtract(left).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dY = vy.multiply(time);
					BigDecimal pDY = player.getIVY().multiply(time);
					
					BigDecimal nBottom = bottom.add(dY);
					BigDecimal nTop = top.add(dY);
					
					BigDecimal nPBottom = player.getBottom().add(pDY);
					BigDecimal nPTop = player.getTop().add(pDY);
					
					if (nPTop.compareTo(nBottom) > 0 && nPBottom.compareTo(nTop) < 0) {
						collision.quad = this;
						
						collision.px = left.add(vx.multiply(time)).subtract(player.getHWidth());
						collision.py = player.getY().add(pDY);
						
						collision.time = time;
						
						collision.type = Collision.Type.LEFT;
					}
				}
			}
		} else if (player.getBottom().compareTo(top) >= 0 && player.getIVY().compareTo(vy) < 0) {
			// iy = vy * t + top
			// iy = player.vy * t + player.getBottom()
			// t = (player.getBottom() - top) / (vy - player.vy)
			
			BigDecimal dv = vy.subtract(player.getIVY());
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.getBottom().subtract(top).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dX = vx.multiply(time);
					BigDecimal pDX = player.getIVX().multiply(time);
					
					BigDecimal nLeft = left.add(dX);
					BigDecimal nRight = right.add(dX);
					
					BigDecimal nPLeft = player.getLeft().add(pDX);
					BigDecimal nPRight = player.getRight().add(pDX);
					
					if (nPRight.compareTo(nLeft) > 0 && nPLeft.compareTo(nRight) < 0) {
						collision.quad = this;
						
						collision.px = player.getX().add(pDX);
						collision.py = top.add(vy.multiply(time)).add(player.getHHeight());
						
						collision.time = time;
						
						collision.type = Collision.Type.TOP;
					}
				}
			}
		} else if (bottom.compareTo(player.getTop()) >= 0 && player.getIVY().compareTo(vy) > 0) {
			// iy = vy * t + bottom
			// iy = player.vy * t + player.getTop()
			// t = (player.getTop() - bottom) / (vy - player.vy)
			
			BigDecimal dv = vy.subtract(player.getIVY());
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.getTop().subtract(bottom).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dX = vx.multiply(time);
					BigDecimal pDX = player.getIVX().multiply(time);
					
					BigDecimal nLeft = left.add(dX);
					BigDecimal nRight = right.add(dX);
					
					BigDecimal nPLeft = player.getLeft().add(pDX);
					BigDecimal nPRight = player.getRight().add(pDX);
					
					if (nPRight.compareTo(nLeft) > 0 && nPLeft.compareTo(nRight) < 0) {
						collision.quad = this;
						
						collision.px = player.getX().add(pDX);
						collision.py = bottom.add(vy.multiply(time)).subtract(player.getHHeight());
						
						collision.time = time;
						
						collision.type = Collision.Type.BOTTOM;
					}
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
			
			collision.vx = player.getVX().add(SURFACE_DRAG.multiply(player.getVX()).negate().multiply(cacheDT));
			collision.vy = BigDecimal.ZERO;
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public Color getColor(Theme theme) {
		return theme.getDefaultQuadColor();
	}
	
	@Override
	public Type getType() {
		return Type.NORMAL;
	}
	
	@Override
	public Block clone() {
		return new Block(left, right, bottom, top);
	}
}
