package com.infinityjump.core.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.api.Input.InputAPI.DragEvent;
import com.infinityjump.core.game.base.Boundary;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Quad;
import com.infinityjump.core.game.base.Target;
import com.infinityjump.core.game.customizable.BouncyQuad;
import com.infinityjump.core.game.customizable.DeadlyQuad;
import com.infinityjump.core.game.customizable.StickyQuad;
import com.infinityjump.core.game.customizable.TeleportQuad;
import com.infinityjump.core.api.Logger;

public final class Level {

	public static final float SCROLL_MARGIN = 0.1f;
	
	private static Pattern pBraces = Pattern.compile("[\\[\\]]");
	private static Pattern pComma = Pattern.compile("\\s*,\\s*");
	private static Pattern pEquals = Pattern.compile("\\s*=\\s*");
	
	private Map<Integer, Quad> initQuads;
	private BigDecimal initPX, initPY;
	
	private Map<Integer, Quad> quads;
	
	private Player player;
	private Target target;
	private Boundary boundary;
	
	private float scrollX, scrollY;
	
	private boolean restart;
	
	public Level() {
		initQuads = new HashMap<Integer, Quad>();
		quads = new HashMap<Integer, Quad>();
	}
	
	public void read(InputStream input) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
			String line;
			
			boolean boundaryInitialized = false;
			boolean targetInitialized = false;
			
			while((line = reader.readLine()) != null) {
				if (line.startsWith("quad")) {
					Integer id = null;
					Float left = null;
					Float right = null;
					Float bottom = null;
					Float top = null;
					String type = null;
					
					Integer linkID = null; // only for teleportation
					
					String[] args = pComma.split(pBraces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = pEquals.split(arg);
						
						if (pair[0].contentEquals("id")) {
							id = Integer.parseInt(pair[1]);
						} else if (pair[0].contentEquals("left")) {
							left = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("right")) {
							right = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("bottom")) {
							bottom = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("top")) {
							top = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("type")) {
							type = pair[1];
						} else if (pair[0].contentEquals("channel")) {
							linkID = Integer.parseInt(pair[1]);
						}
					}
					
					if (id == null || left == null || right == null || bottom == null || top == null) {
						Logger.getAPI().error("Quad must define left, right, bottom, top and id");
						return;
					}
					
					Quad quad = null;
					
					switch (type) {
					case "normal": quad = new Quad(left, right, bottom, top); break;
					case "deadly": quad = new DeadlyQuad(left, right, bottom, top); break;
					case "bouncy": quad = new BouncyQuad(left, right, bottom, top); break;
					case "sticky": quad = new StickyQuad(left, right, bottom, top); break;
					case "teleport":
						if (linkID == null) {
							Logger.getAPI().error("Must specify channel property of teleport quad");
							return;
						}
						
						quad = new TeleportQuad(left, right, bottom, top, linkID);
						break;
					default:
						Logger.getAPI().error("Quad must define type");
						return;
					}
					
					this.initQuads.put(id, quad);
					this.quads.put(id, quad.clone()); // not the same instance
					
				} else if (line.startsWith("player")) {
					Float x = null;
					Float y = null;
					Float size = null;
					
					String[] args = pComma.split(pBraces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = pEquals.split(arg);
						
						if (pair[0].contentEquals("x")) {
							x = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("y")) {
							y = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("size")) {
							size = Float.parseFloat(pair[1]);
						}
					}
					
					if (x == null || y == null || size == null) {
						Logger.getAPI().error("Player must define x, y and size");
						return;
					}
					
					this.initPX = new BigDecimal(x);
					this.initPY = new BigDecimal(y);
					
					this.player = new Player(x, y, size);
				} else if (line.startsWith("boundary")) {
					Float left = null;
					Float right = null;
					Float bottom = null;
					Float top = null;
					
					String[] args = pComma.split(pBraces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = pEquals.split(arg);
						
						if (pair[0].contentEquals("left")) {
							left = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("right")) {
							right = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("bottom")) {
							bottom = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("top")) {
							top = Float.parseFloat(pair[1]);
						}
					}
					
					if (left == null || right == null || bottom == null || top == null) {
						Logger.getAPI().error("Boundary must define left, right, bottom and top");
						return;
					}

					this.boundary = new Boundary(left, right, bottom, top);
					
					boundaryInitialized = true;
				} else if (line.startsWith("target")) {
					Float left = null;
					Float right = null;
					Float bottom = null;
					Float top = null;
					
					String[] args = pComma.split(pBraces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = pEquals.split(arg);
						
						if (pair[0].contentEquals("left")) {
							left = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("right")) {
							right = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("bottom")) {
							bottom = Float.parseFloat(pair[1]);
						} else if (pair[0].contentEquals("top")) {
							top = Float.parseFloat(pair[1]);
						}
					}
					
					if (left == null || right == null || bottom == null || top == null) {
						Logger.getAPI().error("Target must define left, right, bottom and top");
						return;
					}

					this.target = new Target(left, right, bottom, top);
					
					targetInitialized = true;
				}
			}
			
			if (player == null || !boundaryInitialized || !targetInitialized) {
				Logger.getAPI().error("Must define player, boundary and target");
			}
			
		} catch (IOException e) {
			Logger.getAPI().error("Error reading level file");
			return;
		}
		
		scrollX = player.getX().floatValue();
		scrollY = player.getY().floatValue();
	}
	
	public void restart() {
		restart = true;
	}
	
	public void reset() {
		quads.clear();
		
		initQuads.forEach((id, quad) -> quads.put(id, quad.clone())); // fill quads with initial quads
		
		player.setX(initPX);
		player.setY(initPY);
		
		player.setVX(BigDecimal.ZERO);
		player.setVY(BigDecimal.ZERO);
		
		scrollX = initPX.floatValue();
		scrollY = initPY.floatValue();
	}
	
	public void update(double dt) {
		BigDecimal deltaT = new BigDecimal(dt);
		
		DragEvent dragEvent = Input.getAPI().getDragEvent();
		
		if (dragEvent != null && dragEvent.getTime() < Input.maxThresholdDragTime) {
			int dx = dragEvent.getDX();
			int dy = dragEvent.getDY();
			
			if ((dx < 0? -dx : dx) > Input.minThresholdDrag) {
				BigDecimal boostX = new BigDecimal(dx * Input.dragToSpeed);
				
				player.boostX(boostX);
			}
			
			if (-dy > Input.minThresholdDrag) {
				BigDecimal boostY = new BigDecimal(-dy * Input.dragToSpeed);
				
				player.jump(boostY);
			}
		}
		
		player.updateStartFrame(this, deltaT);
		
		for (Quad quad : quads.values()) {
			quad.updateStartFrame(this, deltaT);
		}
		
		Collision collision = new Collision();
		
		player.setIVX(player.getVX());
		player.setIVY(player.getVY());
		
		collision.ivx = player.getIVX();
		collision.ivy = player.getIVY();
		
		BigDecimal timeLeft = deltaT;
		
		while (timeLeft.compareTo(BigDecimal.ZERO) > 0) {
			collision.quad = null;
			
			collision.vx = player.getVX();
			collision.vy = player.getVY();
			
			collision.time = timeLeft;
			
			collision.type = Collision.Type.NONE;
			
			for (Quad quad : quads.values()) {
				quad.update(player, collision, deltaT);
			}
			
			boundary.update(player, collision, deltaT);
			
			if (collision.quad != null) {
				collision.quad.collided(player, collision, deltaT);
			}
			
			if (restart) {
				restart = false;
				return;
			}
			
			timeLeft = timeLeft.subtract(collision.time);
			
			player.update(player, collision, deltaT);
			
			player.updateEndFrame(this, deltaT);
			
			for (Quad quad : quads.values())
				quad.updateEndFrame(this, collision.time);
		}
		
		target.update(player, null, deltaT);
		
		updateScroll();
	}
	
	private void updateScroll() {
		if (player.getLeft().floatValue() - scrollX < SCROLL_MARGIN - 1.0f) {
			scrollX = player.getLeft().floatValue() - (SCROLL_MARGIN - 1.0f);
			
		} else if (player.getRight().floatValue() - scrollX > 1.0f - SCROLL_MARGIN) {
			scrollX = player.getRight().floatValue() - (1.0f - SCROLL_MARGIN);
		}
		
		if (player.getBottom().floatValue() - scrollY < SCROLL_MARGIN - 1.0f) {
			scrollY = player.getBottom().floatValue() - (SCROLL_MARGIN - 1.0f);
			
		} else if (player.getTop().floatValue() - scrollY > 1.0f - SCROLL_MARGIN) {
			scrollY = player.getTop().floatValue() - (1.0f - SCROLL_MARGIN);
		}
	}
	
	public void render(Theme theme) {
		render(theme, 0.0f, 1.0f);
	}
	
	public void render(Theme theme, float shiftX, float playerAlpha) {
		float sScrollX = scrollX + shiftX;
		
		Quad.shader.bind();
		Quad.shader.setAspectRatio(Input.getAPI().getAspectRatio());
		
		boundary.render(theme, sScrollX, scrollY);
		
		target.render(theme, sScrollX, scrollY);
		
		for (Quad quad : quads.values()) {
			quad.render(theme, sScrollX, scrollY);
		}
		
		player.render(theme, sScrollX, scrollY, playerAlpha);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Target getTarget() {
		return target;
	}
	
	public Boundary getBoundary() {
		return boundary;
	}
	
	public Map<Integer, Quad> getQuads() {
		return quads;
	}
	
	public float getScrollDistToLeft() {
		return scrollX - boundary.getLeft().floatValue();
	}
	
	public float getScrollDistToRight() {
		return boundary.getRight().floatValue() - scrollX;
	}
}
