package com.infinityjump.core.game.base;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.quad.QuadShapeImpl;
import com.infinityjump.core.game.properties.PlayerProperties;
import com.infinityjump.core.game.sound.Sounds;

public class Player extends QuadShapeImpl {

	protected BigDecimal cacheAirDrag;
	
	protected boolean jumping;
	
	protected BigDecimal ivx, ivy;
	
	public Player(BigDecimal x, BigDecimal y, BigDecimal hSize) {
		super(x.subtract(hSize), x.add(hSize), y.subtract(hSize), y.add(hSize));
		
		this.ivx = BigDecimal.ZERO;
		this.ivy = BigDecimal.ZERO;
		
		this.jumping = true;
	}
	
	@Override
	public Type getType() {
		return Type.PLAYER;
	}
	
	public void update(Level level, Theme theme, BigDecimal dt) {
		PlayerProperties props = (PlayerProperties)theme.getProperties("player");
		
		cacheAirDrag = props.airDrag;
		
		vy = vy.add(dt.multiply(props.gravity));
	}
	
	public void resolveCollision(Collision collision) {
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
			BigDecimal airDragEffect = BigDecimal.ONE.subtract(cacheAirDrag.multiply(collision.time));
			collision.vx = collision.vx.multiply(airDragEffect);
			collision.vy = collision.vy.multiply(airDragEffect);
		}
		
		vx = collision.vx;
		vy = collision.vy;
	}
	
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
	
	public void setSize(BigDecimal size) {
		super.setWidth(size);
		super.setHeight(size);
	}
	
	public BigDecimal getSize() {
		return super.getWidth();
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
