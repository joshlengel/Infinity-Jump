package com.infinityjump.core.game.base;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.sound.Sounds;

public class Player extends Quad {

	public static final BigDecimal AIR_DRAG = new BigDecimal(3.0);
	
	private static final BigDecimal gravity = new BigDecimal(-9.81);
	
	protected boolean jumping;
	
	protected BigDecimal hWidth, hHeight;
	protected BigDecimal ivx, ivy;
	
	public Player(float x, float y, float size) {
		super(0.0f, 0.0f, 0.0f, 0.0f);
		
		this.x = new BigDecimal(x);
		this.y = new BigDecimal(y);
		
		this.width = new BigDecimal(size);
		this.height = new BigDecimal(size);
		
		this.hWidth = width.multiply(point5);
		this.hHeight = height.multiply(point5);
		
		this.left = this.x.subtract(hWidth);
		this.right = this.x.add(hWidth);
		this.bottom = this.y.subtract(hHeight);
		this.top = this.y.add(hHeight);
		
		this.ivx = BigDecimal.ZERO;
		this.ivy = BigDecimal.ZERO;
		
		this.jumping = true;
	}
	
	public void render(Theme theme, float scrollX, float scrollY, float alpha) {
		Color playerColor = theme.getPlayerColor();
		Color nPlayerColor = new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), playerColor.getAlpha() * alpha);
		
		render(nPlayerColor, scrollX, scrollY);
	}

	@Override
	public void render(Theme theme, float scrollX, float scrollY) {
		render(theme.getPlayerColor(), scrollX, scrollY);
	}
	
	@Override
	public void updateStartFrame(Level level, BigDecimal dt) {
		vy = vy.add(dt.multiply(gravity));
	}
	
	@Override
	public void update(Player player, Collision collision, BigDecimal dt) {
		if (collision.type == Collision.Type.NONE) {
			BigDecimal dx = ivx.multiply(collision.time);
			BigDecimal dy = ivy.multiply(collision.time);
			
			x = x.add(dx);
			y = y.add(dy);
			
			left = left.add(dx);
			right = right.add(dx);
			
			bottom = bottom.add(dy);
			top = top.add(dy);
		} else {
		
			setX(collision.px);
			setY(collision.py);
			
			ivx = collision.ivx;
			ivy = collision.ivy;
		}
		
		if (collision.type == Collision.Type.TOP) {
			jumping = false;
		} else if (jumping) {
			BigDecimal airDragEffect = one.subtract(AIR_DRAG.multiply(collision.time));
			collision.vx = collision.vx.multiply(airDragEffect);
			collision.vy = collision.vy.multiply(airDragEffect);
		}
		
		vx = collision.vx;
		vy = collision.vy;
	}
	
	@Override
	public void updateEndFrame(Level level, BigDecimal dt) {}
	
	public void boostX(BigDecimal speed) {
		this.vx = speed;
	}
	
	public void jump(BigDecimal speed) {
		if (!jumping) {
			jumping = true;
			
			this.vy = speed;
			
			Sounds.playSound("jump");
		}
	}
	
	public BigDecimal getHWidth() {
		return hWidth;
	}
	
	public BigDecimal getHHeight() {
		return hHeight;
	}
	
	@Override
	public void setX(BigDecimal x) {
		this.x = x;
		
		this.left = this.x.subtract(hWidth);
		this.right = this.x.add(hWidth);
	}
	
	@Override
	public void setY(BigDecimal y) {
		this.y = y;
		
		this.bottom = this.y.subtract(hHeight);
		this.top = this.y.add(hHeight);
	}
	
	@Override
	public void setWidth(BigDecimal width) {
		this.width = width;
		this.hWidth = width.multiply(point5);
		
		this.left = this.x.subtract(hWidth);
		this.right = this.x.add(hWidth);
	}
	
	@Override
	public void setHeight(BigDecimal height) {
		this.height = height;
		this.hHeight = height.multiply(point5);
		
		this.bottom = this.y.subtract(hHeight);
		this.top = this.y.add(hHeight);
	}
	
	@Override
	public void setLeft(BigDecimal left) {
		super.setLeft(left);
		
		hWidth = width.multiply(point5);
	}
	
	@Override
	public void setRight(BigDecimal right) {
		super.setRight(right);
		
		hWidth = width.multiply(point5);
	}
	
	@Override
	public void setBottom(BigDecimal bottom) {
		super.setBottom(bottom);
		
		hHeight = height.multiply(point5);
	}
	
	@Override
	public void setTop(BigDecimal top) {
		super.setTop(top);
		
		hHeight = height.multiply(point5);
	}
	
	public void setIVX(BigDecimal ivx) {
		this.ivx = ivx;
	}
	
	public void setIVY(BigDecimal ivy) {
		this.ivy = ivy;
	}
	
	public BigDecimal getIVX() {
		return ivx;
	}
	
	public BigDecimal getIVY() {
		return ivy;
	}
}
