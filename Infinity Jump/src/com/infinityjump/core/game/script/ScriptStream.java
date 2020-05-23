package com.infinityjump.core.game.script;

public interface ScriptStream {

	boolean hasNext();
	
	Script next();
}
