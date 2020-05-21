package com.infinityjump.core.script;

import java.math.BigDecimal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.infinityjump.core.game.base.Block;

public class SetY extends TwoArgFunction {

	private Block quad;
	
	public SetY(Block quad) {
		this.quad = quad;
	}
	
	@Override
	public LuaValue call(LuaValue self, LuaValue y) {
		quad.setY(new BigDecimal(y.checknumber().tofloat()));
		return null;
	}
}
