package com.infinityjump.core.state;

import java.util.Map;

public interface State {

	void enter(Map<String, Object> args, Map<String, Object> resources);
	void update(double dt);
	void finish();
	void render();
}
