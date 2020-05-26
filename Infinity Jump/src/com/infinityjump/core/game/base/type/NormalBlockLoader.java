package com.infinityjump.core.game.base.type;

import java.math.BigDecimal;
import java.util.Map;

import com.infinityjump.core.game.base.Block;

public class NormalBlockLoader {

	protected void parseShape(Map<String, String> args, BigDecimal[] save) {
		save[0] = new BigDecimal(Double.parseDouble(args.get("left")));
		save[1] = new BigDecimal(Double.parseDouble(args.get("right")));
		save[2] = new BigDecimal(Double.parseDouble(args.get("bottom")));
		save[3] = new BigDecimal(Double.parseDouble(args.get("top")));
	}
	
	public Block parse(Map<String, String> args) {
		BigDecimal[] save = new BigDecimal[4];
		
		parseShape(args, save);
		
		return new Block(save[0], save[1], save[2], save[3]);
	}
	
	public Block getDefault(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		return new Block(left, right, bottom, top);
	}
}
