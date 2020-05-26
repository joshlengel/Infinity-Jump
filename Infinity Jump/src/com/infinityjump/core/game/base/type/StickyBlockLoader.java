package com.infinityjump.core.game.base.type;

import java.math.BigDecimal;
import java.util.Map;

import com.infinityjump.core.game.customizable.StickyBlock;

public class StickyBlockLoader extends NormalBlockLoader {

	@Override
	public StickyBlock parse(Map<String, String> args) {
		BigDecimal[] save = new BigDecimal[4];
		
		super.parseShape(args, save);
		
		return new StickyBlock(save[0], save[1], save[2], save[3]);
	}
	
	@Override
	public StickyBlock getDefault(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		return new StickyBlock(left, right, bottom, top);
	}
}
