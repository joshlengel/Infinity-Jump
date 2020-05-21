package com.infinityjump.ide.window.leveleditor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.base.Boundary;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Target;
import com.infinityjump.core.game.base.quad.QuadShape;
import com.infinityjump.core.game.customizable.TeleportBlock;
import com.infinityjump.ide.window.properties.PropertyPane;

import javafx.scene.control.SplitPane;

public class LevelData {
	
	public static final float DEFAULT_QUAD_SIZE = 0.2f;
	
	public static final double EDGE_WIDTH = 0.02;
	public static final float EDGE_WIDTH_F = (float)EDGE_WIDTH;
	public static final BigDecimal EDGE_WIDTH_BD = new BigDecimal(EDGE_WIDTH);

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int TOP = 3;
	
	public Theme theme;
	
	public Player player;
	public Target target;
	public Boundary boundary;
	public Map<Integer, Block> blocks;
	
	public Block spawningBlock;
	public QuadShape highlighted;
	
	public static class QuadEdge {
		QuadShape quad;
		int edge;
		
		QuadEdge(QuadShape quad, int edge) {
			this.quad = quad;
			this.edge = edge;
		}
	}
	
	public QuadEdge draggingEdge;
	public boolean draggingBoundary;
	
	public QuadEdge draggingOverEdge;
	public boolean draggingOverBoundary;
	
	public double mouseX, mouseY;
	public double scrollX, scrollY;
	public boolean scrolling;
	public double zoom = 1.0;
	
	public PropertyPane lastPropertyPane;
	
	public SplitPane center;
	
	public double cacheWidth;
	public double cacheInvWidth;
	
	public double cacheZoom = zoom;
	public double cacheInvZoom = 1.0 / zoom;
	
	public LevelView view;
	public double aspectRatio;
	
	public String makeLevelSource() {
		StringBuilder builder = new StringBuilder();
		builder.append("player[");
		builder.append("size=").append(player.getWidth()).append(", ");
		builder.append("x=").append(player.getX()).append(", ");
		builder.append("y=").append(player.getY());
		builder.append("]\n");
		
		builder.append("target[");
		builder.append("left=").append(target.getLeft()).append(", ");
		builder.append("right=").append(target.getRight()).append(", ");
		builder.append("bottom=").append(target.getBottom()).append(", ");
		builder.append("top=").append(target.getTop());
		builder.append("]\n");
		
		builder.append("\n");
		
		builder.append("boundary[");
		builder.append("left=").append(boundary.getLeft()).append(", ");
		builder.append("right=").append(boundary.getRight()).append(", ");
		builder.append("bottom=").append(boundary.getBottom()).append(", ");
		builder.append("top=").append(boundary.getTop());
		builder.append("]\n");
		
		builder.append("\n");
		
		for (Entry<Integer, Block> entry : blocks.entrySet()) {
			Block block = entry.getValue();
			
			builder.append("quad[");
			builder.append("id=").append(entry.getKey()).append(", ");
			builder.append("left=").append(block.getLeft()).append(", ");
			builder.append("right=").append(block.getRight()).append(", ");
			builder.append("bottom=").append(block.getBottom()).append(", ");
			builder.append("top=").append(block.getTop()).append(", ");
			builder.append("type=").append(block.getType().toString());
			
			if(block instanceof TeleportBlock) {
				builder.append(", channel=").append(((TeleportBlock)block).getChannel()).append(", ");
				builder.append("eject-type=").append(((TeleportBlock)block).getEjectType().toString());
			}
			
			builder.append("]\n");
		}
		
		return builder.toString();
	}
}
