package com.infinityjump.game.states;

import java.io.InputStream;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.game.LevelStream;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.script.ScriptStream;
import com.infinityjump.core.state.State;
import com.infinityjump.core.state.StateMachine;
import com.infinityjump.game.impl.InputAPIImpl;

public class StartState implements State {

	@Override
	public void enter(Object[] args, Map<String, Object> resources) {
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
		
		LevelStream levelStream = (LevelStream) args[0];
		ScriptStream scriptStream = (ScriptStream) args[1];
		
		GLFW.glfwShowWindow(window);
		
		StateMachine.machine.changeState("start-level", new Object[] { (Theme)args[2], levelStream, levelStream.next(), scriptStream, scriptStream.next(), (InputStream)args[3], (InputStream)args[4] });
	}

	@Override
	public void update(double dt) {}

	@Override
	public void finish() {}

	@Override
	public void render() {}
}
