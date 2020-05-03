package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.api.OpenGL.OpenGLAPI;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.LevelStream;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.sound.Sounds;
import com.infinityjump.core.script.Script;
import com.infinityjump.core.script.ScriptStream;

public class GameState implements State {

	private Theme theme;
	
	private LevelStream levelStream;
	private Level level;
	
	private ScriptStream scriptStream;
	private Script script;
	
	@Override
	public void enter(Object[] args, Map<String, Object> resources) {
		this.theme = (Theme)args[0];
		
		this.levelStream = (LevelStream)args[1];
		this.level = (Level)args[2];
		
		this.scriptStream = (ScriptStream)args[3];
		this.script = (Script)args[4];
		
		script.read(level);
		script.setup(level);
	}

	@Override
	public void update(double dt) {
		script.update(dt, level);
		level.update(dt);
	}

	@Override
	public void finish() {}

	@Override
	public void render() {
		Color boundaryColor = theme.getDefaultQuadColor();
		
		OpenGLAPI api = OpenGL.getAPI();
		
		api.clearColor(boundaryColor.getRed(), boundaryColor.getGreen(), boundaryColor.getBlue(), boundaryColor.getAlpha());
		api.clear();
		
		level.render(theme);
	}
	
	public void changeState() {
		if (levelStream.hasNext() && scriptStream.hasNext()) {
			Sounds.playSound("level-finished");
			StateMachine.machine.changeState("transition", new Object[] { theme, levelStream, level, levelStream.next(), scriptStream, scriptStream.next() });
		} else {
			StateMachine.machine.changeState("end-level", null);
		}
	}
	
	public void restart() {
		StateMachine.machine.changeState("restart", new Object[] { theme, levelStream, level, scriptStream, script });
	}
	
	public Level getLevel() {
		return level;
	}
}
