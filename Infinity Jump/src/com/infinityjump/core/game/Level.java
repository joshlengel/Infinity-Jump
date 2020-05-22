package com.infinityjump.core.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.api.Input.InputAPI.DragEvent;
import com.infinityjump.core.game.base.Boundary;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.base.Target;
import com.infinityjump.core.game.customizable.BouncyBlock;
import com.infinityjump.core.game.customizable.DeadlyBlock;
import com.infinityjump.core.game.customizable.StickyBlock;
import com.infinityjump.core.game.customizable.TeleportBlock;
import com.infinityjump.core.graphics.particles.ParticleRenderer;
import com.infinityjump.core.graphics.quads.QuadRenderer;
import com.infinityjump.core.utils.Patterns;
import com.infinityjump.core.api.Logger;

public final class Level {
	
	private Map<Integer, Block> initBlocks;
	private BigDecimal initPX, initPY;
	
	private Map<Integer, Block> blocks;
	
	private Player player;
	private Target target;
	private Boundary boundary;
	
	private Camera camera;
	
	private QuadRenderer quadRenderer;
	private ParticleRenderer particleRenderer;
	
	private boolean restart;
	
	public Level() {
		initBlocks = new HashMap<Integer, Block>();
		blocks = new HashMap<Integer, Block>();
		
		quadRenderer = new QuadRenderer();
		particleRenderer = new ParticleRenderer();
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
					String ejectType = null; // only for teleportation
					
					String[] args = Patterns.commaWithSpaces.split(Patterns.braces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = Patterns.equals.split(arg);
						
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
						} else if (pair[0].contentEquals("eject-type")) {
							ejectType = pair[1];
						}
					}
					
					if (id == null || left == null || right == null || bottom == null || top == null) {
						Logger.getAPI().error("Quad must define left, right, bottom, top and id");
						return;
					}
					
					BigDecimal dLeft = new BigDecimal(left);
					BigDecimal dRight = new BigDecimal(right);
					BigDecimal dBottom = new BigDecimal(bottom);
					BigDecimal dTop = new BigDecimal(top);
					
					Block quad = null;
					
					switch (type) {
					case "normal": quad = new Block(dLeft, dRight, dBottom, dTop); break;
					case "deadly": quad = new DeadlyBlock(dLeft, dRight, dBottom, dTop); break;
					case "bouncy": quad = new BouncyBlock(dLeft, dRight, dBottom, dTop); break;
					case "sticky": quad = new StickyBlock(dLeft, dRight, dBottom, dTop); break;
					case "teleport":
						TeleportBlock.EjectType eT = TeleportBlock.EjectType.parseType(ejectType);
						
						if (linkID == null || eT == null) {
							Logger.getAPI().error("Must specify channel and eject-type properties of teleport quad");
							return;
						}
						
						quad = new TeleportBlock(dLeft, dRight, dBottom, dTop, linkID, eT);
						break;
					default:
						Logger.getAPI().error("Quad must define type");
						return;
					}
					
					Block cloned = quad.clone();
					
					this.initBlocks.put(id, quad);
					this.blocks.put(id, cloned); // not the same instance
					
				} else if (line.startsWith("player")) {
					Float x = null;
					Float y = null;
					Float size = null;
					
					String[] args = Patterns.commaWithSpaces.split(Patterns.braces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = Patterns.equals.split(arg);
						
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
					
					this.player = new Player(this.initPX, this.initPY, new BigDecimal(size * 0.5f));
				} else if (line.startsWith("boundary")) {
					Float left = null;
					Float right = null;
					Float bottom = null;
					Float top = null;
					
					String[] args = Patterns.commaWithSpaces.split(Patterns.braces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = Patterns.equals.split(arg);
						
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

					this.boundary = new Boundary(new BigDecimal(left), new BigDecimal(right), new BigDecimal(bottom), new BigDecimal(top));
					
					boundaryInitialized = true;
				} else if (line.startsWith("target")) {
					Float left = null;
					Float right = null;
					Float bottom = null;
					Float top = null;
					
					String[] args = Patterns.commaWithSpaces.split(Patterns.braces.split(line)[1]);
					
					for(String arg : args) {
						String[] pair = Patterns.equals.split(arg);
						
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

					this.target = new Target(new BigDecimal(left), new BigDecimal(right), new BigDecimal(bottom), new BigDecimal(top));
					
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
		
		camera = new Camera(player);
		
		quadRenderer.addRenderable(boundary);
		quadRenderer.addRenderable(target);
		
		for (Block block : blocks.values()) quadRenderer.addRenderable(block);
		
		quadRenderer.addRenderable(player);
	}
	
	public void restart() {
		restart = true;
	}
	
	public void reset() {
		blocks.clear();
		quadRenderer.clear();
		particleRenderer.clear();
		
		quadRenderer.addRenderable(boundary);
		quadRenderer.addRenderable(target);
		
		initBlocks.forEach((id, quad) -> {
			Block cloned = quad.clone();
			blocks.put(id, cloned);
			quadRenderer.addRenderable(cloned);
		}); // fill quads with initial quads
		
		quadRenderer.addRenderable(player);
		
		player.setX(initPX);
		player.setY(initPY);
		
		player.setVX(BigDecimal.ZERO);
		player.setVY(BigDecimal.ZERO);
		
		camera.reset();
	}
	
	public void update(Theme theme, double dt) {
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
		
		player.update(this, theme, deltaT);
		
		if (!target.update(this, theme, deltaT)) return;
		
		for (Block block : blocks.values()) {
			block.update(this, theme, deltaT);
		}
		
		boundary.update(this, theme, deltaT);
		
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
			
			for (Block quad : blocks.values()) {
				quad.checkCollision(player, collision);
			}
			
			boundary.checkCollision(player, collision);
			
			if (collision.quad != null) {
				collision.quad.resolveCollision(player, collision);
				
				if (collision.quad instanceof TeleportBlock) {
					Block linked = ((TeleportBlock)collision.quad).getCacheLinked();
					camera.requestJump(linked.getX().floatValue(), linked.getY().floatValue());
				}
			}
			
			if (restart) {
				restart = false;
				return;
			}
			
			timeLeft = timeLeft.subtract(collision.time);
			
			player.resolveCollision(collision);
			
			for (Block quad : blocks.values())
				quad.updateFrame(this, collision.time);
		}
		
		camera.update(dt);
	}
	
	public void render(Theme theme) {
		render(theme, 0.0f, 1.0f);
	}
	
	public void render(Theme theme, float shiftX, float playerAlpha) {
		quadRenderer.render(camera, theme, shiftX);
		particleRenderer.render(camera, shiftX);
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
	
	public Map<Integer, Block> getBlocks() {
		return blocks;
	}
	
	public ParticleRenderer getParticleRenderer() {
		return particleRenderer;
	}
	
	public float getScrollDistToLeft() {
		return camera.getX() - boundary.getLeft().floatValue();
	}
	
	public float getScrollDistToRight() {
		return boundary.getRight().floatValue() - camera.getX();
	}
}
