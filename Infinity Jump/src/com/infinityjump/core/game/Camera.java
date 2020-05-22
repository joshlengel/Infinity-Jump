package com.infinityjump.core.game;

import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.utils.GlobalProperties;

public class Camera {

	private static float SCROLL_SPEED, SCROLL_MARGIN;
	
	public static void init() {
		SCROLL_SPEED = Float.parseFloat((String) GlobalProperties.properties.get("camera-scroll-speed"));
		SCROLL_MARGIN = Float.parseFloat((String) GlobalProperties.properties.get("camera-scroll-margin"));
	}
	
	private Player player;
	private float x, y, tX, tY;
	
	public Camera(Player player) {
		this.player = player;
		this.x = player.getX().floatValue();
		this.y = player.getY().floatValue();
	}
	
	public void requestJump(float x, float y) {
		tX = x;
		tY = y;
	}
	
	public void update(double dt) {
		if (tX == 0 && tY == 0) {
			if (player.getLeft().floatValue() - x < SCROLL_MARGIN - 1.0f) {
				x = player.getLeft().floatValue() - (SCROLL_MARGIN - 1.0f);
			} else if (player.getRight().floatValue() - x > 1.0f - SCROLL_MARGIN) {
				x = player.getRight().floatValue() - (1.0f - SCROLL_MARGIN);
			}
			
			if (player.getBottom().floatValue() - y < SCROLL_MARGIN - 1.0f) {
				y = player.getBottom().floatValue() - (SCROLL_MARGIN - 1.0f);
			} else if (player.getTop().floatValue() - y > 1.0f - SCROLL_MARGIN) {
				y = player.getTop().floatValue() - (1.0f - SCROLL_MARGIN);
			}
		} else {
		
			/*
			tX = player.getX().floatValue();
			tY = player.getY().floatValue();
			*/
	
			float dx = tX - x;
			float dy = tY - y;
			
			float speedX = (float) (dx / (SCROLL_SPEED * dt) * SCROLL_SPEED);
			float speedY = (float) (dy / (SCROLL_SPEED * dt) * SCROLL_SPEED);
			
			if (speedX > SCROLL_SPEED) speedX = SCROLL_SPEED;
			else if (speedX < -SCROLL_SPEED) speedX = -SCROLL_SPEED;
			else tX = 0;
			
			if (speedY > SCROLL_SPEED) speedY = SCROLL_SPEED;
			else if (speedY < -SCROLL_SPEED) speedY = -SCROLL_SPEED;
			else tY = 0;
			
			x += speedX * dt;
			y += speedY * dt;
		}
	}
	
	public void reset() {
		this.x = player.getX().floatValue();
		this.y = player.getY().floatValue();
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
