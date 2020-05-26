package com.infinityjump.core.game.base.quad;

import java.math.BigDecimal;

import com.infinityjump.core.game.base.type.Type;
import com.infinityjump.core.utils.BigConstants;

public abstract class QuadShapeImpl implements QuadShape {

	protected BigDecimal x, y, width, height;
	protected BigDecimal hWidth, hHeight;
	protected BigDecimal left, right, bottom, top;
	protected BigDecimal vx, vy;
	
	public QuadShapeImpl(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		
		this.x = left.add(right).multiply(BigConstants.point5);
		this.y = bottom.add(top).multiply(BigConstants.point5);
		this.width = right.subtract(left);
		this.height = top.subtract(bottom);
		
		this.hWidth = width.multiply(BigConstants.point5);
		this.hHeight = height.multiply(BigConstants.point5);
		
		this.vx = BigDecimal.ZERO;
		this.vy = BigDecimal.ZERO;
	}
	
	@Override
	public abstract Type getType();

	@Override public BigDecimal getX() { return x; }
	@Override public BigDecimal getY() { return y; }
	@Override public BigDecimal getWidth() { return width; }
	@Override public BigDecimal getHeight() { return height; }
	
	public BigDecimal getHWidth() { return hWidth; }
	public BigDecimal getHHeight() { return hHeight; }

	@Override
	public void setX(BigDecimal x) {
		this.x = x;
		
		this.hWidth = width.multiply(BigConstants.point5);
		
		this.left = x.subtract(hWidth);
		this.right = x.add(hWidth);
	}
	
	@Override
	public void setY(BigDecimal y) {
		this.y = y;
		
		this.hHeight = height.multiply(BigConstants.point5);
		
		this.bottom = y.subtract(hHeight);
		this.top = y.add(hHeight);
	}
	
	@Override
	public void setWidth(BigDecimal width) {
		this.width = width;
		
		this.hWidth = width.multiply(BigConstants.point5);
		
		this.left = x.subtract(hWidth);
		this.right = x.add(hWidth);
	}
	
	@Override
	public void setHeight(BigDecimal height) {
		this.height = height;
		
		this.hHeight = height.multiply(BigConstants.point5);
		
		this.bottom = y.subtract(hHeight);
		this.top = y.add(hHeight);
	}
	
	@Override public BigDecimal getLeft() { return left; }
	@Override public BigDecimal getRight() { return right; }
	@Override public BigDecimal getBottom() { return bottom; }
	@Override public BigDecimal getTop() { return top; }
	
	@Override
	public void setLeft(BigDecimal left) {
		this.left = left;
		
		x = left.add(right).multiply(BigConstants.point5);
		width = right.subtract(left);
		hWidth = width.multiply(BigConstants.point5);
	}
	
	@Override
	public void setRight(BigDecimal right) {
		this.right = right;
		
		x = left.add(right).multiply(BigConstants.point5);
		width = right.subtract(left);
		hWidth = width.multiply(BigConstants.point5);
	}
	
	@Override
	public void setBottom(BigDecimal bottom) {
		this.bottom = bottom;
		
		y = bottom.add(top).multiply(BigConstants.point5);
		height = top.subtract(bottom);
		hHeight = height.multiply(BigConstants.point5);
	}
	
	@Override
	public void setTop(BigDecimal top) {
		this.top = top;
		
		y = bottom.add(top).multiply(BigConstants.point5);
		height = top.subtract(bottom);
		hHeight = height.multiply(BigConstants.point5);
	}
	
	@Override public BigDecimal getVX() { return vx; }
	@Override public BigDecimal getVY() { return vy; }
	
	@Override
	public void setVX(BigDecimal vx) {
		this.vx = vx;
	}
	
	@Override
	public void setVY(BigDecimal vy) {
		this.vy = vy;
	}
}
