package com.infinityjump.core.script;

import java.math.BigDecimal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.infinityjump.core.game.base.Block;

public class SetX extends TwoArgFunction {

	private Block quad;
	
	public SetX(Block quad) {
		this.quad = quad;
	}
	
	@Override
	public LuaValue call(LuaValue self, LuaValue x) {
		quad.setX(new BigDecimal(x.checknumber().tofloat()));
		return null;
	}
}
