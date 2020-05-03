package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.script.Script;

public class RestartState implements State {

	private static final double FADE_TIME = 1.0d;
	
	private Object[] args;
	
	private Theme theme;
	private Level level;
	private Script script;
	
	private double timer = 0;
	
	@Override
	public void enter(Object[] args, Map<String, Object> resources) {
		this.args = args;
		
		theme = (Theme)args[0];
		level = (Level)args[2];
		script = (Script)args[4];
		
		level.restart(); // break out of update loop
	}

	@Override
	public void update(double dt) {
		Input.getAPI().getDragEvent(); // swallow any drag events
		
		timer += dt;
		
		if (timer > FADE_TIME) {
			level.reset();
			script.reset(level);
			StateMachine.machine.changeState("game", args);
		}
	}

	@Override
	public void finish() {}

	@Override
	public void render() {
		level.render(theme, 0.0f, (float)(1.0f - timer / FADE_TIME));
	}
}
