package com.infinityjump.ide.window.leveleditor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map.Entry;

import com.infinityjump.core.game.level.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Block;
import com.infinityjump.core.game.base.Type;
import com.infinityjump.core.game.base.quad.QuadShape;
import com.infinityjump.core.game.customizable.BouncyBlock;
import com.infinityjump.core.game.customizable.DeadlyBlock;
import com.infinityjump.core.game.customizable.StickyBlock;
import com.infinityjump.core.game.customizable.TeleportBlock;
import com.infinityjump.core.game.properties.QuadProperties;
import com.infinityjump.ide.Utils;
import com.infinityjump.ide.window.properties.BlockPropertiesPanel;
import com.infinityjump.ide.window.properties.BoundaryPropertiesPanel;
import com.infinityjump.ide.window.properties.PlayerPropertiesPanel;
import com.infinityjump.ide.window.properties.TargetPropertiesPanel;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelView extends Pane {
	
	private LevelData data;
	private MouseHandler mouseHandler;
	private ScrollHandler scrollHandler;
	
	private Canvas drawable;
	
	public LevelView(SplitPane center, Theme theme) {
		data = new LevelData();
		data.view = this;
		
		mouseHandler = new MouseHandler(data);
		scrollHandler = new ScrollHandler(data);
		
		data.center = center;
		
		data.theme = theme;
		
		drawable = new Canvas();
		
		drawable.widthProperty().bind(super.widthProperty());
		drawable.heightProperty().bind(super.heightProperty());
		
		drawable.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				data.mouseX = e.getX();
				data.mouseY = e.getY();
			} else if (e.getButton() == MouseButton.SECONDARY) {
				data.rMouseX = e.getX();
				data.rMouseY = e.getY();
				mouseHandler.handleRightPress();
			}
		});
		
		drawable.setOnMouseReleased(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				mouseHandler.handleRightRelease();
			}
		});
		
		drawable.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			if (data.scrolling) {
				data.scrolling = false;
				e.consume();
			}
		});
		
		drawable.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				mouseHandler.handleLeftClick();
			}
		});
		
		drawable.setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				data.scrolling = true;
				
				double nMouseX = e.getX();
				double nMouseY = e.getY();
				
				mouseHandler.handleLeftDrag(nMouseX, nMouseY);
				
				scrollHandler.clampScroll();
				
				data.mouseX = nMouseX;
				data.mouseY = nMouseY;
				
				repaint();
			} else if (e.getButton() == MouseButton.SECONDARY) {
				double nRMouseX = e.getX();
				double nRMouseY = e.getY();
				
				mouseHandler.handleRightDrag(nRMouseX, nRMouseY);
				
				data.rMouseX = nRMouseX;
				data.rMouseY = nRMouseY;
				
				repaint();
			}
		});
		
		drawable.setOnMouseMoved(e -> {
			data.mouseX = e.getX();
			data.mouseY = e.getY();
			
			mouseHandler.handleMoved();
		});
		
		drawable.setOnMouseExited(e -> {
			data.scrolling = false;
		});
		
		super.boundsInLocalProperty().addListener((ov, o, n) -> {
			data.aspectRatio = drawable.getWidth() / drawable.getHeight();
			
			if (data.cacheWidth != drawable.getWidth()) {
				data.cacheWidth = drawable.getWidth();
				data.cacheInvWidth = 1.0 / data.cacheWidth;
			}
			
			scrollHandler.clampScroll();
			repaint();
		});
		
		super.getChildren().add(drawable);
	}
	
	public void load(InputStream levelStream) {
		Level level = new Level();
		level.read(levelStream);
		
		data.player = level.getPlayer();
		data.target = level.getTarget();
		data.boundary = level.getBoundary();
		
		data.blocks = level.getBlocks();
	}
	
	public void repaint() {
		GraphicsContext context = drawable.getGraphicsContext2D();
		context.clearRect(0.0, 0.0, drawable.getWidth(), drawable.getHeight());
		
		double zScrollX = data.scrollX * data.zoom;
		double zScrollY = data.scrollY * data.zoom;
		
		QuadProperties backgroundProps = data.theme.getProperties("background");
		QuadProperties normalProps = data.theme.getProperties("normal");
		QuadProperties targetProps = data.theme.getProperties("target");
		QuadProperties playerProps = data.theme.getProperties("player");
		
		context.setFill(getColor(normalProps.color));
		context.fillRect(0.0d, 0.0d, drawable.getWidth(), drawable.getHeight());
		
		drawRect(context, getRect(data.boundary), getColor(backgroundProps.color), zScrollX, zScrollY);
		
		drawRect(context, getRect(data.target), getColor(targetProps.color), zScrollX, zScrollY);
		
		for (Block block : data.blocks.values()) {
			Rectangle dRect = getRect(block);
			
			QuadProperties props = data.theme.getProperties(block.getType().toString());
			
			drawRect(context, dRect, getColor(props.color), zScrollX, zScrollY);
			
			if (block instanceof TeleportBlock) {
				TeleportBlock tBlock = (TeleportBlock)block;
				
				double x, y;
				
				final int arrowSize = (int) (0.05 * drawable.getWidth() * 0.5 * data.zoom);
				final int wingSize = (int) (0.0125 * drawable.getWidth() * 0.5 * data.zoom);
				
				switch(tBlock.getEjectType()) {
					case LEFT_BOTTOM:
						x = dRect.getX() - zScrollX + 2;
						y = (dRect.getY() + dRect.getHeight()) * data.aspectRatio + zScrollY - 2;
						
						// draw arrow
						context.setStroke(Color.BLACK);
						context.strokeLine(x, y, x + arrowSize, y - arrowSize);
						context.strokeLine(x, y, x, y - wingSize);
						context.strokeLine(x, y, x + wingSize, y);
						break;
						
					case LEFT_TOP:
						x = dRect.getX() - zScrollX + + 2;
						y = dRect.getY() * data.aspectRatio + zScrollY + 2;
						
						// draw arrow
						context.setStroke(Color.BLACK);
						context.strokeLine(x, y, x + arrowSize, y + arrowSize);
						context.strokeLine(x, y, x, y + wingSize);
						context.strokeLine(x, y, x + wingSize, y);
						break;
						
					case RIGHT_BOTTOM:
						x = dRect.getX() - zScrollX + dRect.getWidth() - 2;
						y = (dRect.getY() + dRect.getHeight()) * data.aspectRatio + zScrollY - 2;
						
						// draw arrow
						context.setStroke(Color.BLACK);
						context.strokeLine(x, y, x - arrowSize, y - arrowSize);
						context.strokeLine(x, y, x, y - wingSize);
						context.strokeLine(x, y, x - wingSize, y);
						break;
					case RIGHT_TOP:
						x = dRect.getX() - zScrollX + dRect.getWidth() - 2;
						y = dRect.getY() * data.aspectRatio + zScrollY + 2;
						
						// draw arrow
						context.setStroke(Color.BLACK);
						context.strokeLine(x, y, x - arrowSize, y + arrowSize);
						context.strokeLine(x, y, x, y + wingSize);
						context.strokeLine(x, y, x - wingSize, y);
						break;
				}
			}
		}
		
		if (data.spawningBlock != null) {
			drawRect(context, getRect(data.spawningBlock), getColor(normalProps.color), zScrollX, zScrollY);
		}
		
		drawRect(context, getRect(data.player), getColor(playerProps.color), zScrollX, zScrollY);
		
		if (data.highlighted != null) {
			Rectangle rect = getRect(data.highlighted);
			
			context.setStroke(Color.CADETBLUE);
			context.strokeRect(rect.getX() - zScrollX, rect.getY() * data.aspectRatio + zScrollY, rect.getWidth(), rect.getHeight() * data.aspectRatio);
		}
		
		if (data.rightClickHighlighted != null) {
			Rectangle rect = getRect(data.rightClickHighlighted);
			
			context.setStroke(Color.DARKBLUE);
			context.strokeRect(rect.getX() - zScrollX, rect.getY() * data.aspectRatio + zScrollY, rect.getWidth(), rect.getHeight() * data.aspectRatio);
		}
		
		if (data.draggingEdge != null) {
			QuadShape dq = data.draggingEdge.quad;
			QuadShape quad = null;
			
			if (data.draggingBoundary) {
				switch(data.draggingEdge.edge) {
					case LevelData.LEFT:   quad = new Block(dq.getLeft().subtract(LevelData.EDGE_WIDTH_BD), dq.getLeft(), dq.getBottom(), dq.getTop()); break;
					case LevelData.RIGHT:  quad = new Block(dq.getRight(), dq.getRight().add(LevelData.EDGE_WIDTH_BD), dq.getBottom(), dq.getTop()); break;
					case LevelData.BOTTOM: quad = new Block(dq.getLeft(), dq.getRight(), dq.getBottom().subtract(LevelData.EDGE_WIDTH_BD), dq.getBottom()); break;
					case LevelData.TOP:    quad = new Block(dq.getLeft(), dq.getRight(), dq.getTop(), dq.getTop().add(LevelData.EDGE_WIDTH_BD)); break;
				}
			} else {
				switch(data.draggingEdge.edge) {
					case LevelData.LEFT:   quad = new Block(dq.getLeft(), dq.getLeft().add(LevelData.EDGE_WIDTH_BD), dq.getBottom(), dq.getTop()); break;
					case LevelData.RIGHT:  quad = new Block(dq.getRight().subtract(LevelData.EDGE_WIDTH_BD), dq.getRight(), dq.getBottom(), dq.getTop()); break;
					case LevelData.BOTTOM: quad = new Block(dq.getLeft(), dq.getRight(), dq.getBottom(), dq.getBottom().add(LevelData.EDGE_WIDTH_BD)); break;
					case LevelData.TOP:    quad = new Block(dq.getLeft(), dq.getRight(), dq.getTop().subtract(LevelData.EDGE_WIDTH_BD), dq.getTop()); break;
				}
			}
			
			drawRect(context, getRect(quad), Color.CADETBLUE, zScrollX, zScrollY);
			
			if (data.draggingOverEdge != null) {
				QuadShape doq = data.draggingOverEdge.quad;
				quad = null;
				
				if (data.draggingOverBoundary) {
					switch(data.draggingOverEdge.edge) {
						case LevelData.LEFT:   quad = new Block(doq.getLeft().subtract(LevelData.EDGE_WIDTH_BD), doq.getLeft(), doq.getBottom(), doq.getTop()); break;
						case LevelData.RIGHT:  quad = new Block(doq.getRight(), doq.getRight().add(LevelData.EDGE_WIDTH_BD), doq.getBottom(), doq.getTop()); break;
						case LevelData.BOTTOM: quad = new Block(doq.getLeft(), doq.getRight(), doq.getBottom().subtract(LevelData.EDGE_WIDTH_BD), doq.getBottom()); break;
						case LevelData.TOP:    quad = new Block(doq.getLeft(), doq.getRight(), doq.getTop(), doq.getTop().add(LevelData.EDGE_WIDTH_BD)); break;
					}
				} else {
					switch(data.draggingOverEdge.edge) {
						case LevelData.LEFT:   quad = new Block(doq.getLeft(), doq.getLeft().add(LevelData.EDGE_WIDTH_BD), doq.getBottom(), doq.getTop()); break;
						case LevelData.RIGHT:  quad = new Block(doq.getRight().subtract(LevelData.EDGE_WIDTH_BD), doq.getRight(), doq.getBottom(), doq.getTop()); break;
						case LevelData.BOTTOM: quad = new Block(doq.getLeft(), doq.getRight(), doq.getBottom(), doq.getBottom().add(LevelData.EDGE_WIDTH_BD)); break;
						case LevelData.TOP:    quad = new Block(doq.getLeft(), doq.getRight(), doq.getTop().subtract(LevelData.EDGE_WIDTH_BD), doq.getTop()); break;
					}
				}
				
				drawRect(context, getRect(quad), Color.AQUAMARINE, zScrollX, zScrollY);
			}
		}
	}
	
	private void drawRect(GraphicsContext context, Rectangle rect, Color color, double zScrollX, double zScrollY) {
		context.setFill(color);
		context.fillRect(rect.getX() - zScrollX, rect.getY() * data.aspectRatio + zScrollY, rect.getWidth(), rect.getHeight() * data.aspectRatio);
	}
	
	public void reset() {
		load(new ByteArrayInputStream(Utils.getDefaultLevel().getBytes()));
	}
	
	public String makeLevelSource() {
		return data.makeLevelSource();
	}
	
	public void setZoom(double value) {
		data.zoom = value;
		
		if (value != data.cacheZoom) {
			data.cacheZoom = value;
			data.cacheInvZoom = 1.0 / value;
		}
		
		scrollHandler.clampScroll();
		repaint();
	}
	
	private Rectangle getRect(QuadShape quad) {
		return new Rectangle(
				(quad.getLeft().floatValue() * data.zoom + 1.0d) * 0.5d * drawable.getWidth(),
				(1.0d - quad.getTop().floatValue() * data.zoom) * 0.5d * drawable.getHeight(),
				(quad.getRight().floatValue() - quad.getLeft().floatValue()) * data.zoom * 0.5d * drawable.getWidth(),
				(quad.getTop().floatValue() - quad.getBottom().floatValue()) * data.zoom * 0.5d * drawable.getHeight());
	}
	
	private Color getColor(com.infinityjump.core.game.Color color) {
		return Color.color(color.r, color.g, color.b);
	}

	void showBlockProperties(Entry<Integer, Block> block) {
		BlockPropertiesPanel props = new BlockPropertiesPanel(block, this);
		
		data.center.getItems().remove(data.lastPropertyPane);
		data.center.getItems().add(props);
		
		data.lastPropertyPane = props;
	}
	
	void showPlayerProperties() {
		PlayerPropertiesPanel props = new PlayerPropertiesPanel(data.player, this);
		
		data.center.getItems().remove(data.lastPropertyPane);
		data.center.getItems().add(props);
		
		data.lastPropertyPane = props;
	}
	
	void showTargetProperties() {
		TargetPropertiesPanel props = new TargetPropertiesPanel(data.target, this);
		
		data.center.getItems().remove(data.lastPropertyPane);
		data.center.getItems().add(props);
		
		data.lastPropertyPane = props;
	}
	
	void showBoundaryProperties() {
		BoundaryPropertiesPanel props = new BoundaryPropertiesPanel(data.boundary, this);
		
		data.center.getItems().remove(data.lastPropertyPane);
		data.center.getItems().add(props);
		
		data.lastPropertyPane = props;
	}
	
	void removeProperties() {
		data.center.getItems().remove(data.lastPropertyPane);
		
		data.lastPropertyPane = null;
	}
	
	public Block setNewBlockType(int id, Type type) {
		Block quad = data.blocks.get(id);
		BigDecimal left = quad.getLeft();
		BigDecimal right = quad.getRight();
		BigDecimal bottom = quad.getBottom();
		BigDecimal top = quad.getTop();
		
		Block nBlock = null;
		
		switch (type) {
			case BOUNCY: nBlock = new BouncyBlock(left, right, bottom, top); break;
			case DEADLY: nBlock = new DeadlyBlock(left, right, bottom, top); break;
			case STICKY: nBlock = new StickyBlock(left, right, bottom, top); break;
			case TELEPORT: nBlock = new TeleportBlock(left, right, bottom, top, 0, TeleportBlock.EjectType.LEFT_BOTTOM); break;
			default: 
			case NORMAL: nBlock = new Block(left, right, bottom, top); break;
		}
		
		data.blocks.put(id, nBlock);
		
		return nBlock;
	}
	
	public void removeCurrentBlock(Entry<Integer, Block> entry) {
		int size = data.blocks.size();
		
		data.blocks.remove(entry.getKey());
		data.highlighted = null;
		
		data.view.removeProperties();
		
		for (int i = entry.getKey() + 1; i < size; ++i) {
			data.blocks.put(i - 1, data.blocks.get(i));
		}
		
		data.blocks.remove(size - 1);
	}
	
	public void spawnQuad() {
		float x = (float) data.mouseX;
		float y = (float) data.mouseY;
		
		float hSize = LevelData.DEFAULT_QUAD_SIZE * 0.5f;
		
		data.spawningBlock = new Block(new BigDecimal(x - hSize), new BigDecimal(x + hSize), new BigDecimal(y - hSize), new BigDecimal(y + hSize));
		
		data.view.repaint();
	}
}
