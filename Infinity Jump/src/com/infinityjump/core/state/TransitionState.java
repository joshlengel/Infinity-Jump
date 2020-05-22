package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.api.OpenGL.OpenGLAPI;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.properties.BlockProperties;

public class TransitionState implements State {

	public static final double TRANSITION_LENGTH = 2.0;
	
	private Map<String, Object> args;
	
	private Theme theme;
	private Level previous, next;
	
	private double timer;
	
	private float totalShiftDist;
	
	@Override
	public void enter(Map<String, Object> args, Map<String, Object> resources) {
		this.args = args;
		
		theme    = (Theme)args.get("theme");
		
		previous = (Level)args.get("previous");
		next     = (Level)args.get("next");
		
		totalShiftDist = previous.getScrollDistToRight() + 1.0f + next.getScrollDistToLeft();
	}

	@Override
	public void update(double dt) {
		Input.getAPI().getDragEvent(); // swallow any drag events
		
		timer += dt;
		
		if (timer > TRANSITION_LENGTH) {
			args.remove("previous");
			args.remove("next");
			args.put("level", next);
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
		
		float shiftX = (float) (timer / TRANSITION_LENGTH * totalShiftDist);
		
		previous.render(theme, shiftX, 1.0f);
		next.render(theme, shiftX - totalShiftDist, 1.0f);
	}
}
