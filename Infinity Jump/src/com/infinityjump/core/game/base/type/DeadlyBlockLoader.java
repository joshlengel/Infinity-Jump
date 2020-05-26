package com.infinityjump.core.game.base.type;

import java.math.BigDecimal;
import java.util.Map;

import com.infinityjump.core.game.customizable.DeadlyBlock;

public class DeadlyBlockLoader extends NormalBlockLoader {

	@Override
	public DeadlyBlock parse(Map<String, String> args) {
		BigDecimal[] save = new BigDecimal[4];
		
		super.parseShape(args, save);
		
		return new DeadlyBlock(save[0], save[1], save[2], save[3]);
	}
	
	@Override
	public DeadlyBlock getDefault(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		return new DeadlyBlock(left, right, bottom, top);
	}
}
