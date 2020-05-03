package com.infinityjump.core.script;

import java.math.BigDecimal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.infinityjump.core.game.base.Quad;

public class SetX extends TwoArgFunction {

	private Quad quad;
	
	public SetX(Quad quad) {
		this.quad = quad;
	}
	
	@Override
	public LuaValue call(LuaValue self, LuaValue x) {
		quad.setX(new BigDecimal(x.checknumber().tofloat()));
		return null;
	}
}
