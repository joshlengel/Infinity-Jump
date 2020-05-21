package com.infinityjump.core.state;

import java.io.InputStream;
import java.util.Map;

import com.infinityjump.core.game.sound.Sounds;
import com.infinityjump.core.graphics.GraphicsAssets;

public class StartLevelState implements State {

	@Override
	public void enter(Object[] args, Map<String, Object> resources) {
		GraphicsAssets.init((InputStream)args[5], (InputStream)args[6]);
		
		Sounds.init();
		
		if (StateMachine.machine.shouldExit()) return;
		
		StateMachine.machine.changeState("game", args);
	}

	@Override
	public void update(double dt) {}

	@Override
	public void finish() {}

	@Override
	public void render() {}
}
