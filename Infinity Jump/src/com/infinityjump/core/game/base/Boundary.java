package com.infinityjump.core.game.base;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;

public class Boundary extends Quad {

	public Boundary(float left, float right, float bottom, float top) {
		super(left, right, bottom, top);
	}

	@Override
	public void render(Theme theme, float scrollX, float scrollY) {
		Color backgroundColor = theme.getBackgroundColor();
		
		render(backgroundColor, scrollX, scrollY);
	}
	
	@Override
	public void update(Player player, Collision collision, BigDecimal dt) {
		// calculate collision
		if (player.left.compareTo(left) >= 0 && player.ivx.compareTo(BigDecimal.ZERO) < 0) {
			// ix = left
			// ix = player.vx * t + player.left
			// t = (left - player.left) / player.vx
			
			if (player.ivx.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = left.subtract(player.left).divide(player.ivx, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = left.add(player.hWidth);
					collision.py = player.y.add(player.ivy.multiply(time));
					
					collision.time = time;
					
					collision.type = Collision.Type.LEFT;
				}
			}
		} else if (right.compareTo(player.right) >= 0 && player.ivx.compareTo(BigDecimal.ZERO) > 0) {
			// ix = right
			// ix = player.vx * t + player.right
			// t = (right - player.right) / player.vx
			
			if (player.ivx.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = right.subtract(player.right).divide(player.ivx, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = right.subtract(player.hWidth);
					collision.py = player.y.add(player.ivy.multiply(time));
					
					collision.time = time;
					
					collision.type = Collision.Type.RIGHT;
				}
			}
		}
		
		if (player.bottom.compareTo(bottom) >= 0 && player.ivy.compareTo(BigDecimal.ZERO) < 0) {
			// ix = bottom
			// ix = player.vy * t + player.bottom
			// t = (bottom - player.bottom) / player.vy
			
			if (player.ivy.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = bottom.subtract(player.bottom).divide(player.ivy, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = player.x.add(player.ivx.multiply(time));
					collision.py = bottom.add(player.hHeight);
					
					collision.time = time;
					
					collision.type = Collision.Type.TOP;
				}
			}
		} else if (top.compareTo(player.top) >= 0 && player.ivy.compareTo(BigDecimal.ZERO) > 0) {
			// ix = top
			// ix = player.vy * t + player.top
			// t = (top - player.top) / player.vy
			
			if (player.ivy.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = top.subtract(player.top).divide(player.ivy, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					collision.quad = this;
					
					collision.px = player.x.add(player.ivx.multiply(time));
					collision.py = top.subtract(player.hWidth);
					
					collision.time = time;
					
					collision.type = Collision.Type.BOTTOM;
				}
			}
		}
	}
}
