package com.infinityjump.core.graphics.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.infinityjump.core.game.Camera;
import com.infinityjump.core.game.Color;

public class ParticleEmitter {
	
	private List<Particle> particles;
	
	private float x, y;
	private float gravity;
	private float speed;
	
	private double maxAngle;
	
	private double lifeSpan;
	
	private double elapsedTime;
	private float spawnRate;
	
	private Color from, to;
	
	private float size;
	
	public ParticleEmitter(float x, float y, float gravity, double spread, float speed, double lifeSpan, float spawnRate, Color from, Color to, float size) {
		particles = new ArrayList<>();
		
		this.x = x;
		this.y = y;
		this.gravity = gravity;
		this.speed = speed;
		
		this.maxAngle = spread * 0.5;
		
		this.lifeSpan = lifeSpan;
		
		this.spawnRate = spawnRate;
		
		this.from = from;
		this.to = to;
		
		this.size = size;
		
		this.elapsedTime = 1.0 / spawnRate;
	}
	
	public void update(double dt) {
		// check for dead particles and update
		
		for (Iterator<Particle> itr = particles.iterator(); itr.hasNext();) {
			Particle next = itr.next();
			
			next.update(dt);
			
			if (next.shouldDelete()) itr.remove();
		}
		
		elapsedTime += dt;
		
		int toSpawn = (int) (spawnRate * elapsedTime);
		
		if (toSpawn > 0) {
			elapsedTime -= toSpawn / spawnRate;
		}
		
		for (int i = 0; i < toSpawn; ++i) {
			double angle = (Math.random() * 2 - 1) * maxAngle;
			
			float weight = (float) Math.random();
			Color result = new Color();
			result.r = (to.r - from.r) * weight + from.r;
			result.g = (to.g - from.g) * weight + from.g;
			result.b = (to.b - from.b) * weight + from.b;
			result.a = 1.0f;
			
			Particle p = new Particle(x, y, (float)Math.sin(angle) * speed, (float)Math.cos(angle) * speed, gravity, lifeSpan, result);
			
			particles.add(p);
		}
	}
	
	public void render(Camera camera, float shiftX) {
		for (Particle p : particles) {
			p.render(camera, size, shiftX);
		}
	}
	
	public void stopSpawning() {
		spawnRate = 0.0f;
	}
}
