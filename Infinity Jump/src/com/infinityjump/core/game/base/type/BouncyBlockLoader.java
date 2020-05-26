package com.infinityjump.core.game.base.type;

import java.math.BigDecimal;
import java.util.Map;

import com.infinityjump.core.game.customizable.BouncyBlock;

public class BouncyBlockLoader extends NormalBlockLoader {

	@Override
	public BouncyBlock parse(Map<String, String> args) {
		BigDecimal[] save = new BigDecimal[4];
		
		super.parseShape(args, save);
		
		return new BouncyBlock(save[0], save[1], save[2], save[3]);
	}
	
	@Override
	public BouncyBlock getDefault(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		return new BouncyBlock(left, right, bottom, top);
	}
}
