package com.infinityjump.core.graphics.particles;

import com.infinityjump.core.game.Camera;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.graphics.GraphicsAssets;

class Particle {

	private float gravity;
	private float x, y, vx, vy;
	
	private double elapsed = 0, lifeSpan;
	
	private boolean shouldDelete = false;
	
	private Color color;
	
	Particle(float x, float y, float vx, float vy, float gravity, double lifeSpan, Color color) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.gravity = gravity;
		this.lifeSpan = lifeSpan;
		this.color = color;
	}
	
	void update(double dt) {
		vy += gravity * dt;
		
		x += vx * dt;
		y += vy * dt;
		
		if ((elapsed += dt) > lifeSpan)
			shouldDelete = true;
	}
	
	void render(Camera camera, float size, float shiftX) {
		GraphicsAssets.quadShader.setColor(color.r, color.g, color.b, color.a);
		GraphicsAssets.quadShader.setOffset(x - camera.getX(), y - camera.getY());
		GraphicsAssets.quadShader.setScale(size, size);
		GraphicsAssets.quadShader.setShiftX(shiftX);
		
		GraphicsAssets.quadModel.draw();
	}
	
	float getX() {
		return x;
	}
	
	float getY() {
		return y;
	}
	
	boolean shouldDelete() {
		return shouldDelete;
	}
}
