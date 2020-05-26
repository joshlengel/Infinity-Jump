package com.infinityjump.core.game.base.type;

import java.math.BigDecimal;
import java.util.Map;

import com.infinityjump.core.game.customizable.TeleportBlock;

public class TeleportBlockLoader extends NormalBlockLoader {

	@Override
	public TeleportBlock parse(Map<String, String> args) {
		BigDecimal[] save = new BigDecimal[4];
		
		super.parseShape(args, save);
		
		TeleportBlock.EjectType ejectType = TeleportBlock.EjectType.parseType(args.get("eject-type"));
		int linkID = Integer.parseInt(args.get("channel"));
		
		return new TeleportBlock(save[0], save[1], save[2], save[3], linkID, ejectType);
	}
	
	@Override
	public TeleportBlock getDefault(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		return new TeleportBlock(left, right, bottom, top, 0, TeleportBlock.EjectType.LEFT_BOTTOM);
	}
}
