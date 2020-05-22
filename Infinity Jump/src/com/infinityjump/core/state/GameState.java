package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.api.OpenGL.OpenGLAPI;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.LevelStream;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.properties.BlockProperties;
import com.infinityjump.core.game.sound.Sounds;
import com.infinityjump.core.graphics.GraphicsAssets;
import com.infinityjump.core.script.Script;
import com.infinityjump.core.script.ScriptStream;

public class GameState implements State {

	private Map<String, Object> args;
	
	private Theme theme;
	
	private LevelStream levelStream;
	private Level level;
	
	private ScriptStream scriptStream;
	private Script script;
	
	@Override
	public void enter(Map<String, Object> args, Map<String, Object> resources) {
		this.args = args;
		
		this.theme = (Theme)args.get("theme");
		
		this.levelStream = (LevelStream)args.get("levelStream");
		this.level = (Level)args.get("level");
		
		this.scriptStream = (ScriptStream)args.get("scriptStream");
		this.script = (Script)args.get("script");
		
		script.read(level);
		script.setup(level);
		
		GraphicsAssets.assertLoaded();
	}

	@Override
	public void update(double dt) {
		script.update(level, dt);
		level.update(theme, dt);
	}

	@Override
	public void finish() {}

	@Override
	public void render() {
		Color boundaryColor = ((BlockProperties)theme.getProperties("normal")).color;
		
		OpenGLAPI api = OpenGL.getAPI();
		
		api.clearColor(boundaryColor.r, boundaryColor.g, boundaryColor.b, boundaryColor.a);
		api.clear();
		
		level.render(theme);
	}
	
	public void changeState() {
		if (levelStream.hasNext() && scriptStream.hasNext()) {
			Sounds.playSound("level-finished");
			
			args.remove("level");
			args.put("previous", level);
			args.put("next", levelStream.next());
			args.put("script", scriptStream.next());
			
			StateMachine.machine.changeState("transition", args);
		} else {
			StateMachine.machine.changeState("end-level", null);
		}
	}
	
	public void restart() {
		StateMachine.machine.changeState("restart", args);
	}
	
	public Level getLevel() {
		return level;
	}
}
