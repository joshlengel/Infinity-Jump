package com.infinityjump.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.api.OpenAL;
import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.LevelStream;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.script.Script;
import com.infinityjump.core.script.ScriptStream;
import com.infinityjump.core.state.StateMachine;
import com.infinityjump.core.state.StateSupplier;
import com.infinityjump.game.impl.LoggerAPIImpl;
import com.infinityjump.game.impl.OpenALAPIImpl;
import com.infinityjump.game.impl.OpenGLAPIImpl;
import com.infinityjump.game.states.CleanUpState;
import com.infinityjump.game.states.StartState;

public class Launcher {

	public static void init(String assetDir) {
		OpenGL.init(new OpenGLAPIImpl());
		OpenAL.init(new OpenALAPIImpl(assetDir));
		Logger.init(new LoggerAPIImpl());
	}
	
	private static class LevelStreamImpl implements LevelStream {

		private Level level;
		
		LevelStreamImpl(Level level) {
			this.level = level;
		}
		
		public boolean hasNext() {
			return level != null;
		}
		
		@Override
		public Level next() {
			Level temp = level;
			level = null;
			return temp;
		}
	}
	
	private static class ScriptStreamImpl implements ScriptStream {

		private Script script;
		
		ScriptStreamImpl(Script script) {
			this.script = script;
		}
		
		public boolean hasNext() {
			return script != null;
		}
		
		@Override
		public Script next() {
			Script temp = script;
			script = null;
			return temp;
		}
	}
	
	public void launch(Level level, Script script, Theme theme, String assetDir) {
		Map<String, StateSupplier> states = new HashMap<String, StateSupplier>();
		states.put("start", StartState::new);
		states.put("clean-up", CleanUpState::new);
		
		InputStream vertexSource = null;
		InputStream fragmentSource = null;
		InputStream gpSource = null;
		
		try {
			vertexSource = new FileInputStream(assetDir + "shader/quadVertex.glsl");
			fragmentSource = new FileInputStream(assetDir + "shader/quadFragment.glsl");
			gpSource = new FileInputStream(assetDir + "infinity-jump.properties");
		} catch (FileNotFoundException e) {
			System.err.println("Error reading shader files");
			return;
		}
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("theme", theme);
		args.put("levelStream", new LevelStreamImpl(level));
		args.put("scriptStream", new ScriptStreamImpl(script));
		args.put("vertexSource", vertexSource);
		args.put("fragmentSource", fragmentSource);
		args.put("gpSource", gpSource);
		
		StateMachine.init(states);
		StateMachine.machine.changeState("start", args);
		
		try {
			vertexSource.close();
			fragmentSource.close();
			gpSource.close();
		} catch (IOException e) {
			System.err.println("Error closing resources for infinity jump launcher");
		}
		
		long window = (long) StateMachine.machine.getSharedResources().get("window-handle");
		
		long time = System.nanoTime();
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			long nTime = System.nanoTime();
			double dt = (nTime - time) * 1e-9;
			time = nTime;
			
			GLFW.glfwPollEvents();
			
			StateMachine.machine.update(dt);
			
			if (StateMachine.machine.shouldExit()) return;
			
			StateMachine.machine.render();
			
			GLFW.glfwSwapBuffers(window);
		}
		
		StateMachine.terminate();
	}
}
