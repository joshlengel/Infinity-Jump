package com.infinityjump.core.state;

import java.io.InputStream;
import java.util.Map;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.game.Camera;
import com.infinityjump.core.game.level.LevelStream;
import com.infinityjump.core.game.load.PlayableBuffer;
import com.infinityjump.core.game.script.ScriptStream;
import com.infinityjump.core.game.sound.Sounds;
import com.infinityjump.core.graphics.GraphicsAssets;
import com.infinityjump.core.utils.GlobalProperties;

public class StartLevelState implements State {

	@Override
	public void enter(Map<String, Object> args, Map<String, Object> resources) {
		InputStream gpSource = (InputStream)args.get("gpSource");
		InputStream vertexSource = (InputStream)args.get("vertexSource");
		InputStream fragmentSource = (InputStream)args.get("fragmentSource");
		
		if (gpSource == null) {
			Logger.getAPI().error("Must supply StartLevelState with global properties source");
			StateMachine.terminate();
			return;
		}
		
		if (vertexSource == null) {
			Logger.getAPI().error("Must supply StartLevelState with vertex shader source");
			StateMachine.terminate();
			return;
		}
		
		if (fragmentSource == null) {
			Logger.getAPI().error("Must supply StartLevelState with fragment shader source");
			StateMachine.terminate();
			return;
		}
		
		GlobalProperties.init(gpSource);
		GraphicsAssets.init(vertexSource, fragmentSource);
		
		Sounds.init();
		
		Camera.init();
		PlayableBuffer.init();
		
		if (StateMachine.machine.shouldExit()) return;
		
		args.remove("vertexSource");
		args.remove("fragmentSource");
		
		PlayableBuffer buffer = new PlayableBuffer((LevelStream)args.get("levelStream"), (ScriptStream)args.get("scriptStream"));
		
		args.remove("levelStream");
		args.remove("scriptStream");
		
		if (buffer.hasNext()) {
		
			buffer.start();
			
			args.put("playableBuffer", buffer);
			args.put("playable", buffer.get());
			
			StateMachine.machine.changeState("game", args);
		} else {
			StateMachine.machine.changeState("end-level", null);
		}
	}

	@Override
	public void update(double dt) {}

	@Override
	public void finish() {}

	@Override
	public void render() {}
}
