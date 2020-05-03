package com.infinityjump.core.script;

import java.math.BigDecimal;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.infinityjump.core.game.base.Quad;

public class SetVY extends TwoArgFunction {

	private Quad quad;
	
	public SetVY(Quad quad) {
		this.quad = quad;
	}
	
	@Override
	public LuaValue call(LuaValue self, LuaValue vy) {
		quad.setVY(new BigDecimal(vy.checknumber().tofloat()));
		return null;
	}
}
