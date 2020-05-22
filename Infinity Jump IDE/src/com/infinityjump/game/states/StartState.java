package com.infinityjump.game.states;

import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.game.LevelStream;
import com.infinityjump.core.script.ScriptStream;
import com.infinityjump.core.state.State;
import com.infinityjump.core.state.StateMachine;
import com.infinityjump.game.impl.InputAPIImpl;

public class StartState implements State {

	@Override
	public void enter(Map<String, Object> args, Map<String, Object> resources) {
		if (!GLFW.glfwInit()) {
			System.err.println("Error initializing GLFW");
		}
		
		long window = GLFW.glfwCreateWindow(400, 650, "Infinity Jump", 0l, 0l);
		
		resources.put("window-handle", window);
		
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		Input.init(new InputAPIImpl(window));
		
		LevelStream levelStream = (LevelStream) args.get("levelStream");
		ScriptStream scriptStream = (ScriptStream) args.get("scriptStream");
		
		GLFW.glfwShowWindow(window);
		
		args.put("level", levelStream.next());
		args.put("script", scriptStream.next());
		
		StateMachine.machine.changeState("start-level", args);
	}

	@Override
	public void update(double dt) {}

	@Override
	public void finish() {}

	@Override
	public void render() {}
}
