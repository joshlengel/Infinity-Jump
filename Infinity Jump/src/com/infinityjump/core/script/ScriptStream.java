package com.infinityjump.core.script;

public interface ScriptStream {

	boolean hasNext();
	
	Script next();
}
