package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.api.OpenGL.OpenGLAPI;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.load.Playable;
import com.infinityjump.core.game.properties.BlockProperties;

public class RestartState implements State {

	private static final double FADE_TIME = 1.0d;
	
	private Map<String, Object> args;
	
	private Theme theme;
	private Playable playable;
	
	private double timer = 0;
	
	@Override
	public void enter(Map<String, Object> args, Map<String, Object> resources) {
		this.args = args;
		
		theme = (Theme)args.get("theme");
		playable = (Playable)args.get("playable");
		
		playable.restart(); // break out of update loop
	}

	@Override
	public void update(double dt) {
		Input.getAPI().getDragEvent(); // swallow any drag events
		
		timer += dt;
		
		if (timer > FADE_TIME) {
			playable.reset();
			StateMachine.machine.changeState("game", args);
		}
	}

	@Override
	public void finish() {}

	@Override
	public void render() {
		Color boundaryColor = ((BlockProperties)theme.getProperties("normal")).color;
		
		OpenGLAPI api = OpenGL.getAPI();
		
		api.clearColor(boundaryColor.r, boundaryColor.g, boundaryColor.b, boundaryColor.a);
		api.clear();
		
		playable.render(theme, 0.0f, (float)(1.0f - timer / FADE_TIME));
	}
}
