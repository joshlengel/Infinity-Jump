package com.infinityjump.game.states;

import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.infinityjump.core.state.State;

public class CleanUpState implements State {

	@Override
	public void enter(Map<String, Object> args, Map<String, Object> resources) {
		GLFW.glfwDestroyWindow((long)resources.get("window-handle"));
		GLFW.glfwTerminate();
	}

	@Override
	public void update(double dt) {}

	@Override
	public void finish() {}

	@Override
	public void render() {}
}
