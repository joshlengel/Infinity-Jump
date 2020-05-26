package com.infinityjump.core.game.base.type;

import java.math.BigDecimal;
import java.util.Map;

import com.infinityjump.core.game.customizable.IceBlock;

public class IceBlockLoader extends NormalBlockLoader {

	@Override
	public IceBlock parse(Map<String, String> args) {
		BigDecimal[] save = new BigDecimal[4];
		
		super.parseShape(args, save);
		
		return new IceBlock(save[0], save[1], save[2], save[3]);
	}
	
	@Override
	public IceBlock getDefault(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		return new IceBlock(left, right, bottom, top);
	}
}
