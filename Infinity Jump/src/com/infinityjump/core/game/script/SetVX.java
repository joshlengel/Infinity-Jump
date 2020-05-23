package com.infinityjump.core.game.script;

import java.math.BigDecimal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.infinityjump.core.game.base.Block;

public class SetVX extends TwoArgFunction {

	private Block quad;
	
	public SetVX(Block quad) {
		this.quad = quad;
	}
	
	@Override
	public LuaValue call(LuaValue self, LuaValue vx) {
		quad.setVX(new BigDecimal(vx.checknumber().tofloat()));
		return null;
	}
}
