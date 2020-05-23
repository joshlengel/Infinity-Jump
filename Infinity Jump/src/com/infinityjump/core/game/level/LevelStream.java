package com.infinityjump.core.game.level;

public interface LevelStream {
	
	boolean hasNext();
	
	Level next();
}
