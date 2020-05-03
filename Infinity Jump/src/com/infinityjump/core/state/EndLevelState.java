package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.game.base.Quad;

public class EndLevelState implements State {

	@Override
	public void enter(Object[] args, Map<String, Object> resources) {
		Quad.terminate();
		StateMachine.terminate();
	}

	@Override
	public void update(double dt) {}

	@Override
	public void finish() {}

	@Override
	public void render() {}
}
