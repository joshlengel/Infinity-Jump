package com.infinityjump.core.script;

import java.math.BigDecimal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.infinityjump.core.game.base.Quad;

public class SetVX extends TwoArgFunction {

	private Quad quad;
	
	public SetVX(Quad quad) {
		this.quad = quad;
	}
	
	@Override
	public LuaValue call(LuaValue self, LuaValue vx) {
		quad.setVX(new BigDecimal(vx.checknumber().tofloat()));
		return null;
	}
}
