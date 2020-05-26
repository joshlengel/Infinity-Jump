package com.infinityjump.core.game.base.quad;

import java.math.BigDecimal;

import com.infinityjump.core.game.base.type.Type;

public interface QuadShape {

	Type getType();
	
	BigDecimal getX();
	BigDecimal getY();
	BigDecimal getWidth();
	BigDecimal getHeight();
	
	void setX(BigDecimal x);
	void setY(BigDecimal y);
	void setWidth(BigDecimal width);
	void setHeight(BigDecimal height);
	
	BigDecimal getLeft();
	BigDecimal getRight();
	BigDecimal getBottom();
	BigDecimal getTop();
	
	void setLeft(BigDecimal left);
	void setRight(BigDecimal right);
	void setBottom(BigDecimal bottom);
	void setTop(BigDecimal top);
	
	BigDecimal getVX();
	BigDecimal getVY();
	
	void setVX(BigDecimal vx);
	void setVY(BigDecimal vy);
}
