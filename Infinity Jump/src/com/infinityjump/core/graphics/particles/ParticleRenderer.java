package com.infinityjump.core.graphics.particles;

import java.util.ArrayList;
import java.util.List;

import com.infinityjump.core.game.Camera;

public class ParticleRenderer {

	private List<ParticleEmitter> emitters = new ArrayList<>();
	
	public void add(ParticleEmitter e) {
		emitters.add(e);
	}
	
	public void remove(ParticleEmitter e) {
		emitters.remove(e);
	}
	
	public void clear() {
		emitters.clear();
	}
	
	public void render(Camera camera, float shiftX) {
		for (ParticleEmitter e : emitters) {
			e.render(camera, shiftX);
		}
	}
}
