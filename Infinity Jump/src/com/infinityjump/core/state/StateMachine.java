package com.infinityjump.core.state;

import java.util.HashMap;
import java.util.Map;

public class StateMachine {

	public static StateMachine machine;
	
	public static void init(Map<String, StateSupplier> states) {
		states.put("start-level", StartLevelState::new);
		states.put("game", GameState::new);
		states.put("transition", TransitionState::new);
		states.put("restart", RestartState::new);
		states.put("end-level", EndLevelState::new);
		machine = new StateMachine(states);
	}
	
	public static void terminate() {
		machine.changeState("clean-up", null);
		
		machine.shouldExit = true;
	}
	
	private Map<String, StateSupplier> states;
	private State current;
	
	private Map<String, Object> resources;
	
	private boolean shouldExit;
	
	private StateMachine(Map<String, StateSupplier> states) {
		this.states = states;
		this.resources = new HashMap<>();
	}
	
	public void changeState(String newState, Object[] args) {
		if (current != null) current.finish();
		
		current = states.get(newState).get();
		current.enter(args, resources);
	}
	
	public State getCurrentState() {
		return current;
	}
	
	public void update(double dt) {
		current.update(dt);
	}
	
	public void render() {
		current.render();
	}
	
	public boolean shouldExit() {
		return shouldExit;
	}
	
	public Map<String, Object> getSharedResources() {
		return resources;
	}
}
