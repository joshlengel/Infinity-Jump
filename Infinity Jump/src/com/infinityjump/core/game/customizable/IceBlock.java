package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.base.type.Type;
import com.infinityjump.core.game.level.Level;
import com.infinityjump.core.game.properties.BlockProperties;

public class IceBlock extends Block {

	public IceBlock(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		super(left, right, bottom, top);
	}

	@Override
	public Type getType() {
		return Type.ICE;
	}
	
	@Override
	public void update(Level level, Theme theme, BigDecimal dt) {
		cacheDT = dt;
		cacheSurfaceDrag = ((BlockProperties)theme.getProperties("ice")).surfaceDrag;
	}
	
	@Override
	public IceBlock clone() {
		return new IceBlock(left, right, bottom, top);
	}
}
