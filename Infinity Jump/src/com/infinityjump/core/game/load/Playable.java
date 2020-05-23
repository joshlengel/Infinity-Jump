package com.infinityjump.core.game.load;

import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.level.Level;
import com.infinityjump.core.game.script.Script;

public class Playable {

	private Level level;
	private Script script;
	
	Playable(Level level, Script script) {
		this.level = level;
		this.script = script;
	}
	
	public void init() {
		script.read(level);
		script.setup(level);
	}
	
	public void update(Theme theme, double dt) {
		script.update(level, dt);
		level.update(theme, dt);
	}
	
	public void render(Theme theme) {
		level.render(theme);
	}
	
	public void render(Theme theme, float shiftX, float playerAlpha) {
		level.render(theme, shiftX, playerAlpha);
	}
	
	public void restart() {
		level.restart();
	}
	
	public void reset() {
		level.reset();
		script.read(level);
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Script getScript() {
		return script;
	}
}
