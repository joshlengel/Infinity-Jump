package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.api.OpenGL.OpenGLAPI;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.load.Playable;
import com.infinityjump.core.game.load.PlayableBuffer;
import com.infinityjump.core.game.properties.BlockProperties;
import com.infinityjump.core.game.sound.Sounds;
import com.infinityjump.core.graphics.GraphicsAssets;

public class GameState implements State {

	private Map<String, Object> args;
	
	private Theme theme;
	
	private Playable playable;
	private PlayableBuffer playableBuffer;
	
	@Override
	public void enter(Map<String, Object> args, Map<String, Object> resources) {
		this.args = args;
		
		this.theme = (Theme)args.get("theme");
		
		this.playableBuffer = (PlayableBuffer)args.get("playableBuffer");
		this.playable = (Playable)args.get("playable");
		
		playable.init();
		
		GraphicsAssets.assertLoaded();
	}

	@Override
	public void update(double dt) {
		playable.update(theme, dt);
	}

	@Override
	public void finish() {}

	@Override
	public void render() {
		Color boundaryColor = ((BlockProperties)theme.getProperties("normal")).color;
		
		OpenGLAPI api = OpenGL.getAPI();
		
		api.clearColor(boundaryColor.r, boundaryColor.g, boundaryColor.b, boundaryColor.a);
		api.clear();
		
		playable.render(theme);
	}
	
	public void changeState() {
		if (playableBuffer.hasNext()) {
			Sounds.playSound("level-finished");
			
			args.remove("level");
			args.put("previous", playable);
			args.put("next", playableBuffer.get());
			
			StateMachine.machine.changeState("transition", args);
		} else {
			StateMachine.machine.changeState("end-level", null);
		}
	}
	
	public void restart() {
		StateMachine.machine.changeState("restart", args);
	}
	
	public Playable getPlayable() {
		return playable;
	}
}
