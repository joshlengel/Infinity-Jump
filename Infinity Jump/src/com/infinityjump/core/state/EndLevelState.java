package com.infinityjump.core.state;

import java.util.Map;

import com.infinityjump.core.graphics.GraphicsAssets;

public class EndLevelState implements State {

	@Override
	public void enter(Object[] args, Map<String, Object> resources) {
		GraphicsAssets.terminate();
		StateMachine.terminate();
	}

	@Override
	public void update(double dt) {}

	@Override
	public void finish() {}

	@Override
	public void render() {}
}
