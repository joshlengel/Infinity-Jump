package com.infinityjump.core.script;

import java.util.Map.Entry;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.base.Block;

public class Script {

	private static final LuaFunction modifyError = new ZeroArgFunction() {

		@Override
		public LuaValue call() {
			System.err.println("Atempting to modify read-only table. Use setters instead");
			return null;
		}
	};
	
	private static LuaTable asReadOnlyTable(LuaTable table) {
		LuaTable metatable = new LuaTable();
		metatable.set("__index", table);
		metatable.set("__newindex", modifyError);
		return (LuaTable) new LuaTable().setmetatable(metatable);
	}
	
	private String source;
	
	private Globals globals;
	
	private LuaValue chunk;
	private LuaValue setup, update;
	
	private LuaTable quads;
	
	public Script(String source) {
		this.source = source;
	}
	
	public void read(Level level) {
		globals = JsePlatform.standardGlobals();
		
		try {
			chunk = globals.load(source);
			
			reset(level);
		} catch (LuaError e) {
			System.err.println("Error compiling lua script:\n" + e.getMessage());
			return;
		}
		
		if (!chunk.isfunction()) {
			System.err.println("Invalid lua script. Must supply setup and update functions");
			return;
		}
	}
	
	public void reset(Level level) {
		quads = new LuaTable();
		
		for (Entry<Integer, Block> entry : level.getBlocks().entrySet()) {
			Block block = entry.getValue();
			
			SetX setX = new SetX(block);
			SetY setY = new SetY(block);
			SetVX setVX = new SetVX(block);
			SetVY setVY = new SetVY(block);
			
			LuaTable quadTable = new LuaTable();
			quadTable.rawset("left", block.getLeft().floatValue());
			quadTable.rawset("right", block.getRight().floatValue());
			quadTable.rawset("bottom", block.getBottom().floatValue());
			quadTable.rawset("top", block.getTop().floatValue());
			quadTable.rawset("x", block.getX().floatValue());
			quadTable.rawset("y", block.getY().floatValue());
			quadTable.rawset("width", block.getWidth().floatValue());
			quadTable.rawset("height", block.getHeight().floatValue());
			quadTable.rawset("vx", block.getVX().floatValue());
			quadTable.rawset("vy", block.getVY().floatValue());
			
			quadTable.rawset("setX", setX);
			quadTable.rawset("setY", setY);
			quadTable.rawset("setVX", setVX);
			quadTable.rawset("setVY", setVY);
			
			quadTable = asReadOnlyTable(quadTable);
			
			quads.rawset(entry.getKey(), quadTable);
		}
		
		quads = asReadOnlyTable(quads);
		
		globals.set("quads", quads);
		
		chunk.call();
		
		setup = globals.get("setup");
		update = globals.get("update");
		
		setup.checkfunction();
		update.checkfunction();
	}
	
	public void setup(Level level) {
		try {
			setup.call();
		} catch (LuaError e) {
			System.err.println("Error running setup function in lua script:\n" + e.getMessage());
		}
	}
	
	public void update(Level level, double dt) {
		try {
			// get new quads
			for (Entry<Integer, Block> entry : level.getBlocks().entrySet()) {
				Block block = entry.getValue();
				
				LuaTable quadTable = quads.get(entry.getKey()).checktable();
				quadTable.rawset("left", block.getLeft().floatValue());
				quadTable.rawset("right", block.getRight().floatValue());
				quadTable.rawset("bottom", block.getBottom().floatValue());
				quadTable.rawset("top", block.getTop().floatValue());
				quadTable.rawset("x", block.getX().floatValue());
				quadTable.rawset("y", block.getY().floatValue());
				quadTable.rawset("width", block.getWidth().floatValue());
				quadTable.rawset("height", block.getHeight().floatValue());
				quadTable.rawset("vx", block.getVX().floatValue());
				quadTable.rawset("vy", block.getVY().floatValue());
			}
			
			update.call(LuaValue.valueOf(dt));
		} catch (LuaError e) {
			System.err.println("Error running update function in lua script:\n" + e.getMessage());
		}
	}
}
