package com.infinityjump.ide.window.leveleditor;

import java.math.BigDecimal;
import java.util.Map.Entry;

import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.base.quad.QuadShape;
import com.infinityjump.ide.Utils;
import com.infinityjump.ide.window.leveleditor.LevelData.QuadEdge;

public class MouseHandler {

	private static double sensitivity;
	
	private LevelData data;
	
	public MouseHandler(LevelData data) {
		this.data = data;
	}
	
	public void handleLeftClick() {
		double glX = mouseToGLX();
		double glY = mouseToGLY();
		
		if (data.spawningBlock != null) {
			data.blocks.put(data.blocks.size(), data.spawningBlock);
			data.spawningBlock = null;
			
		} else if (Utils.pressed(data.player, glX, glY)) {
			data.highlighted = data.player;
			data.view.showPlayerProperties();
			
		} else {
			boolean clicked = false;
			for (Entry<Integer, Block> entry : data.blocks.entrySet()) {
				Block quad = entry.getValue();
				
				if (Utils.pressed(quad, glX, glY)) {
					data.highlighted = quad;
					clicked = true;
					data.view.showBlockProperties(entry);
				}
			}
			
			if (!clicked) {
				if (Utils.pressed(data.target, glX, glY)) {
					data.highlighted = data.target;
					data.view.showTargetProperties();
					
				} else if (Utils.boundaryPressed(data.boundary, glX, glY)) {
					data.highlighted = data.boundary;
					data.view.showBoundaryProperties();
					
				} else {
					data.highlighted = null;
					data.view.removeProperties();
				}
			}
		}
	}
	
	public void handleRightPress() {
		double glX = rMouseToGLX();
		double glY = rMouseToGLY();
		
		QuadShape pressed = null;
		
		if (Utils.pressed(data.player, glX, glY)) {
			pressed = data.player;
		} else {
			if (Utils.pressed(data.target, glX, glY)) {
				pressed = data.target;
			}
			
			for (QuadShape shape : data.blocks.values()) {
				if (Utils.pressed(shape, glX, glY)) {
					pressed = shape;
				}
			}
		}
		
		data.rightClickHighlighted = pressed;
		
		data.view.repaint();
	}
	
	public void handleLeftDrag(double mx, double my) {
		double dx = (mx - data.mouseX) * sensitivity;
		double dy = (my - data.mouseY) * sensitivity;
		
		if (data.draggingEdge != null) {
			float glX = (float) mouseToGLX();
			float glY = (float) mouseToGLY();
			
			QuadShape dq = data.draggingEdge.quad;
			
			data.draggingOverEdge = null;
			data.draggingOverBoundary = false;
			
			findEdge: {
				if (dq != data.target) data.draggingOverEdge = checkEdgePress(data.target, false, glX, glY);
				
				if (isDragOverEligable(data.draggingEdge, data.draggingOverEdge))
					break findEdge;
				
				if (!data.draggingBoundary)
					data.draggingOverEdge = checkEdgePress(data.boundary, true, glX, glY);
				
				if (isDragOverEligable(data.draggingEdge, data.draggingOverEdge)) {
					data.draggingOverBoundary = true;
					break findEdge;
				}
				
				for (Block block : data.blocks.values()) {
					if (block == dq) continue;
					
					data.draggingOverEdge = checkEdgePress(block, false, glX, glY);
					
					if (isDragOverEligable(data.draggingEdge, data.draggingOverEdge))
						break findEdge;
				}
				
				if (dq != data.player) data.draggingOverEdge = checkEdgePress(data.player, false, glX, glY);
				
				if (isDragOverEligable(data.draggingEdge, data.draggingOverEdge)) data.draggingOverEdge = null;
			}
			
			float qx = dq.getX().floatValue();
			float qy = dq.getY().floatValue();
			
			if (dq == data.player) {
				float hSize = 0.0f;
				
				switch(data.draggingEdge.edge) {
					case LevelData.LEFT:
						if (data.draggingOverEdge != null) {
							hSize = qx - (data.draggingOverEdge.edge == LevelData.LEFT? data.draggingOverEdge.quad.getLeft().floatValue() : data.draggingOverEdge.quad.getRight().floatValue());
						} else {
							hSize = qx - glX;
						}
						break;
						
					case LevelData.RIGHT:
						if (data.draggingOverEdge != null) {
							hSize = (data.draggingOverEdge.edge == LevelData.LEFT? data.draggingOverEdge.quad.getLeft().floatValue() : data.draggingOverEdge.quad.getRight().floatValue()) - qx;
						} else {
							hSize = glX - qx;
						}
						break;
						
					case LevelData.BOTTOM:
						if (data.draggingOverEdge != null) {
							hSize = qy - (data.draggingOverEdge.edge == LevelData.BOTTOM? data.draggingOverEdge.quad.getBottom().floatValue() : data.draggingOverEdge.quad.getTop().floatValue());
						} else {
							hSize = qy - glY;
						}
						break;
						
					case LevelData.TOP:
						if (data.draggingOverEdge != null) {
							hSize = (data.draggingOverEdge.edge == LevelData.BOTTOM? data.draggingOverEdge.quad.getBottom().floatValue() : data.draggingOverEdge.quad.getTop().floatValue()) - qy;
						} else {
							hSize = glY - qy;
						}
						break;
				}
				
				hSize = Utils.clamp(hSize, LevelData.EDGE_WIDTH_F + 0.02f, 1.0f);
				
				dq.setLeft(new BigDecimal(qx - hSize));
				dq.setRight(new BigDecimal(qx + hSize));
				dq.setBottom(new BigDecimal(qy - hSize));
				dq.setTop(new BigDecimal(qy + hSize));
				
				if (data.highlighted == data.player) {
					data.lastPropertyPane.update();
				}
			} else {
				switch(data.draggingEdge.edge) {
					case LevelData.LEFT:
						float nLeft = data.draggingOverEdge != null? data.draggingOverEdge.edge == LevelData.LEFT? data.draggingOverEdge.quad.getLeft().floatValue() : data.draggingOverEdge.quad.getRight().floatValue() : glX;
						
						if (qx - nLeft < LevelData.EDGE_WIDTH_F + 0.01f) {
							nLeft = qx - LevelData.EDGE_WIDTH_F - 0.01f;
						}
						
						dq.setLeft(new BigDecimal(nLeft));
						break;
						
					case LevelData.RIGHT:
						float nRight = data.draggingOverEdge != null? data.draggingOverEdge.edge == LevelData.LEFT? data.draggingOverEdge.quad.getLeft().floatValue() : data.draggingOverEdge.quad.getRight().floatValue() : glX;
						
						if (nRight - qx < LevelData.EDGE_WIDTH_F + 0.01f) {
							nRight = qx + LevelData.EDGE_WIDTH_F + 0.01f;
						}
						
						dq.setRight(new BigDecimal(nRight));
						break;
					
					case LevelData.BOTTOM:
						float nBottom = data.draggingOverEdge != null? data.draggingOverEdge.edge == LevelData.BOTTOM? data.draggingOverEdge.quad.getBottom().floatValue() : data.draggingOverEdge.quad.getTop().floatValue() : glY;
						
						if (qy - nBottom < LevelData.EDGE_WIDTH_F + 0.01f) {
							nBottom = qy - LevelData.EDGE_WIDTH_F - 0.01f;
						}
						
						dq.setBottom(new BigDecimal(nBottom));
						break;
						
					case LevelData.TOP:
						float nTop = data.draggingOverEdge != null? data.draggingOverEdge.edge == LevelData.BOTTOM? data.draggingOverEdge.quad.getBottom().floatValue() : data.draggingOverEdge.quad.getTop().floatValue() : glY;
						
						if (nTop - qy < LevelData.EDGE_WIDTH_F + 0.01f) {
							nTop = qy + LevelData.EDGE_WIDTH_F + 0.01f;
						}
						
						dq.setTop(new BigDecimal(nTop));
						break;
				}
				
				if (data.highlighted == dq) {
					data.lastPropertyPane.update();
				}
			}
		} else {
			data.scrollX -= dx;
			data.scrollY += dy;
		}
	}
	
	public void handleRightDrag(double mx, double my) {
		if (data.rightClickHighlighted == null) return;
		
		double dx = ((mx - data.rMouseX) * data.cacheInvWidth * 2.0) * data.cacheInvZoom;
		double dy = -((my - data.rMouseY) * data.cacheInvWidth * 2.0) * data.cacheInvZoom;
		
		data.rightClickHighlighted.setX(data.rightClickHighlighted.getX().add(new BigDecimal(dx)));
		data.rightClickHighlighted.setY(data.rightClickHighlighted.getY().add(new BigDecimal(dy)));
	}
	
	public void handleMoved() {
		float glX = (float) mouseToGLX();
		float glY = (float) mouseToGLY();
		
		data.draggingEdge = null;
		data.draggingOverEdge = null;
		
		if (data.spawningBlock != null) {
			float hSize = LevelData.DEFAULT_QUAD_SIZE * 0.5f;
			
			data.spawningBlock.setLeft(new BigDecimal(glX - hSize));
			data.spawningBlock.setRight(new BigDecimal(glX + hSize));
			data.spawningBlock.setBottom(new BigDecimal(glY - hSize));
			data.spawningBlock.setTop(new BigDecimal(glY + hSize));
			
			data.view.repaint();
		} else {
			QuadEdge edge;
			
			edge = checkEdgePress(data.target, false, glX, glY);
			
			if (edge != null) {
				data.draggingEdge = edge;
				data.draggingBoundary = false;
			}
			
			edge = checkEdgePress(data.boundary, true, glX, glY);
			
			if (edge != null) {
				data.draggingEdge = edge;
				data.draggingBoundary = true;
			}
			
			for (Block block : data.blocks.values()) {
				edge = checkEdgePress(block, false, glX, glY);
				
				if (edge != null) {
					data.draggingEdge = edge;
					data.draggingBoundary = false;
				}
			}
			
			edge = checkEdgePress(data.player, false, glX, glY);
			
			if (edge != null) {
				data.draggingEdge = edge;
				data.draggingBoundary = false;
			}

			data.view.repaint();
		}
	}
	
	public void handleRightRelease() {
		if (data.rightClickHighlighted == null) return;
		
		data.rightClickHighlighted = null;
		
		data.view.repaint();
	}
	
	private static boolean isDragOverEligable(QuadEdge draggingEdge, QuadEdge draggingOverEdge) {
		return draggingOverEdge != null &&
				((draggingOverEdge.edge == LevelData.LEFT || draggingOverEdge.edge == LevelData.RIGHT)
				&& (draggingEdge.edge == LevelData.LEFT || draggingEdge.edge == LevelData.RIGHT)
				|| (draggingOverEdge.edge == LevelData.BOTTOM || draggingOverEdge.edge == LevelData.TOP)
				&& (draggingEdge.edge == LevelData.BOTTOM || draggingEdge.edge == LevelData.TOP));
	}
	
	private QuadEdge checkEdgePress(QuadShape quad, boolean boundary, double glX, double glY) {
		if (boundary) {
			if (glX >= quad.getLeft().doubleValue() - LevelData.EDGE_WIDTH
					&& glX <= quad.getLeft().doubleValue()
					&& glY >= quad.getBottom().doubleValue()
					&& glY <= quad.getTop().doubleValue()) {
				return new QuadEdge(quad, LevelData.LEFT);
			} else if (glX >= quad.getRight().doubleValue()
					&& glX <= quad.getRight().doubleValue() + LevelData.EDGE_WIDTH
					&& glY >= quad.getBottom().doubleValue()
					&& glY <= quad.getTop().doubleValue()) {
				return new QuadEdge(quad, LevelData.RIGHT);
			} else if (glX >= quad.getLeft().doubleValue()
					&& glX <= quad.getRight().doubleValue()
					&& glY >= quad.getBottom().doubleValue() - LevelData.EDGE_WIDTH
					&& glY <= quad.getBottom().doubleValue()) {
				return new QuadEdge(quad, LevelData.BOTTOM);
			} else if (glX >= quad.getLeft().doubleValue()
					&& glX <= quad.getRight().doubleValue()
					&& glY >= quad.getTop().doubleValue()
					&& glY <= quad.getTop().doubleValue() + LevelData.EDGE_WIDTH) {
				return new QuadEdge(quad, LevelData.TOP);
			}
		} else {
			if (glX >= quad.getLeft().doubleValue()
					&& glX <= quad.getLeft().doubleValue() + LevelData.EDGE_WIDTH
					&& glY >= quad.getBottom().doubleValue()
					&& glY <= quad.getTop().doubleValue()) {
				return new QuadEdge(quad, LevelData.LEFT);
			} else if (glX >= quad.getRight().doubleValue() - LevelData.EDGE_WIDTH
					&& glX <= quad.getRight().doubleValue()
					&& glY >= quad.getBottom().doubleValue()
					&& glY <= quad.getTop().doubleValue()) {
				return new QuadEdge(quad, LevelData.RIGHT);
			} else if (glX >= quad.getLeft().doubleValue()
					&& glX <= quad.getRight().doubleValue()
					&& glY >= quad.getBottom().doubleValue()
					&& glY <= quad.getBottom().doubleValue() + LevelData.EDGE_WIDTH) {
				return new QuadEdge(quad, LevelData.BOTTOM);
			} else if (glX >= quad.getLeft().doubleValue()
					&& glX <= quad.getRight().doubleValue()
					&& glY >= quad.getTop().doubleValue() - LevelData.EDGE_WIDTH
					&& glY <= quad.getTop().doubleValue()) {
				return new QuadEdge(quad, LevelData.TOP);
			}
		}
		
		return null;
	}
	
	private double mouseToGLX() {
		return getGLX(data.mouseX);
	}
	
	private double mouseToGLY() {
		return getGLY(data.mouseY);
	}
	
	private double rMouseToGLX() {
		return getGLX(data.rMouseX);
	}
	
	private double rMouseToGLY() {
		return getGLY(data.rMouseY);
	}
	
	private double getGLX(double mx) {
		return (mx * data.cacheInvWidth * 2.0 - 1.0) * data.cacheInvZoom + data.scrollX * data.cacheInvWidth * 2.0;
	}
	
	private double getGLY(double my) {
		return (1.0 - my * data.cacheInvWidth * 2.0) * data.cacheInvZoom + data.scrollY * data.cacheInvWidth * 2.0;
	}
	
	public static void setSensitivity(double sensitivity) {
		MouseHandler.sensitivity = sensitivity;
	}
}
