package com.infinityjump.ide.window;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Boundary;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Quad;
import com.infinityjump.core.game.base.Target;
import com.infinityjump.core.game.customizable.BouncyQuad;
import com.infinityjump.core.game.customizable.DeadlyQuad;
import com.infinityjump.core.game.customizable.StickyQuad;
import com.infinityjump.core.game.customizable.TeleportQuad;
import com.infinityjump.ide.Utils;
import com.infinityjump.ide.window.properties.BoundaryProperties;
import com.infinityjump.ide.window.properties.PlayerProperties;
import com.infinityjump.ide.window.properties.PropertyPane;
import com.infinityjump.ide.window.properties.QuadProperties;
import com.infinityjump.ide.window.properties.TargetProperties;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelView extends Pane {

	private static final double MOUSE_DRAG_SENSITIVITY = 1.0d;
	
	private static final float DEFAULT_QUAD_SIZE = 0.2f;
	
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int BOTTOM = 2;
	private static final int TOP = 3;
	
	private static final double EDGE_WIDTH = 0.02;
	private static final float EDGE_WIDTH_F = 0.02f;
	private static final BigDecimal EDGE_WIDTH_BD = new BigDecimal(EDGE_WIDTH);
	
	private Theme theme;
	
	private Player player;
	private Target target;
	private Boundary boundary;
	private Map<Integer, Quad> quads;
	
	private Quad spawningQuad;
	private Quad highlighted;
	
	private QuadEdge draggingEdge;
	private boolean draggingBoundary;
	
	private QuadEdge draggingOverEdge;
	private boolean draggingOverBoundary;
	
	private Canvas drawable;
	private double aspectRatio;
	
	private double mouseX, mouseY;
	private double scrollX, scrollY;
	private boolean scrolling;
	private double zoom = 1.0;
	
	private PropertyPane lastPropertyPane;
	
	private SplitPane center;
	
	private static class QuadEdge {
		Quad quad;
		int edge;
		
		QuadEdge(Quad quad, int edge) {
			this.quad = quad;
			this.edge = edge;
		}
	}
	
	public LevelView(SplitPane center, Theme theme) {
		this.center = center;
		
		this.theme = theme;
		
		drawable = new Canvas();
		
		drawable.widthProperty().bind(super.widthProperty());
		drawable.heightProperty().bind(super.heightProperty());
		
		drawable.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		
		drawable.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			if (scrolling) {
				scrolling = false;
				e.consume();
			}
		});
		
		drawable.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				double invWidth = 1.0 / drawable.getWidth();
				
				double rScrollX = scrollX * invWidth * 2.0;
				double rScrollY = scrollY * invWidth * 2.0;
				
				float glX = (float) ((mouseX * invWidth * 2.0 - 1.0) / zoom - rScrollX);
				float glY = (float) ((1.0 - mouseY * invWidth * 2.0) / zoom + rScrollY);
				
				if (spawningQuad != null) {
					quads.put(quads.size(), spawningQuad);
					spawningQuad = null;
					
				} else if (Utils.pressed(player, glX, glY)) {
					highlighted = player;
					showPlayerProperties();
					
				} else {
					boolean clicked = false;
					for (Entry<Integer, Quad> entry : quads.entrySet()) {
						Quad quad = entry.getValue();
						
						if (Utils.pressed(quad, glX, glY)) {
							highlighted = quad;
							clicked = true;
							showQuadProperties(entry);
						}
					}
					
					if (!clicked) {
						if (Utils.pressed(target, glX, glY)) {
							highlighted = target;
							showTargetProperties();
							
						} else if (Utils.boundaryPressed(boundary, glX, glY)) {
							highlighted = boundary;
							showBoundaryProperties(highlighted);
							
						} else {
							highlighted = null;
							removeProperties();
						}
					}
				}
			}
		});
		
		drawable.setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				scrolling = true;
				
				double nMouseX = e.getX();
				double nMouseY = e.getY();
				
				double dx = (nMouseX - mouseX) * MOUSE_DRAG_SENSITIVITY;
				double dy = (nMouseY - mouseY) * MOUSE_DRAG_SENSITIVITY;
				
				if (draggingEdge != null) {
					double invWidth = 1.0 / drawable.getWidth();
					
					double rScrollX = scrollX * invWidth * 2.0;
					double rScrollY = scrollY * invWidth * 2.0;
					
					double invZoom = 1.0 / zoom;
					
					float glX = (float) ((nMouseX * invWidth * 2.0 - 1.0) * invZoom - rScrollX);
					float glY = (float) ((1.0 - nMouseY * invWidth * 2.0) * invZoom + rScrollY);
					
					Quad dq = draggingEdge.quad;
					
					draggingOverEdge = null;
					draggingOverBoundary = false;
					
					findEdge: {
						if (dq != target) draggingOverEdge = checkEdgePress(target, false, glX, glY);
						
						if (draggingOverEdge != null
								&& ((draggingOverEdge.edge == LEFT || draggingOverEdge.edge == RIGHT) && (draggingEdge.edge == LEFT || draggingEdge.edge == RIGHT)
								|| (draggingOverEdge.edge == BOTTOM || draggingOverEdge.edge == TOP) && (draggingEdge.edge == BOTTOM || draggingEdge.edge == TOP))) break findEdge;
						
						if (!draggingBoundary) draggingOverEdge = checkEdgePress(boundary, true, glX, glY);
						
						if (draggingOverEdge != null
								&& ((draggingOverEdge.edge == LEFT || draggingOverEdge.edge == RIGHT) && (draggingEdge.edge == LEFT || draggingEdge.edge == RIGHT)
								|| (draggingOverEdge.edge == BOTTOM || draggingOverEdge.edge == TOP) && (draggingEdge.edge == BOTTOM || draggingEdge.edge == TOP))) {
							draggingOverBoundary = true;
							break findEdge;
						}
						
						for (Quad quad : quads.values()) {
							if (quad == dq) continue;
							
							draggingOverEdge = checkEdgePress(quad, false, glX, glY);
							
							if (draggingOverEdge != null
									&& ((draggingOverEdge.edge == LEFT || draggingOverEdge.edge == RIGHT) && (draggingEdge.edge == LEFT || draggingEdge.edge == RIGHT)
									|| (draggingOverEdge.edge == BOTTOM || draggingOverEdge.edge == TOP) && (draggingEdge.edge == BOTTOM || draggingEdge.edge == TOP)))
								break findEdge;
						}
						
						if (dq != player) draggingOverEdge = checkEdgePress(player, false, glX, glY);
						
						if (draggingOverEdge != null
								&& !(((draggingOverEdge.edge == LEFT || draggingOverEdge.edge == RIGHT) && (draggingEdge.edge == LEFT || draggingEdge.edge == RIGHT)
								|| (draggingOverEdge.edge == BOTTOM || draggingOverEdge.edge == TOP) && (draggingEdge.edge == BOTTOM || draggingEdge.edge == TOP)))) draggingOverEdge = null;
					}
					
					float qx = dq.getX().floatValue();
					float qy = dq.getY().floatValue();
					
					if (dq == player) {
						float hSize = 0.0f;
						
						switch(draggingEdge.edge) {
							case LEFT:
								if (draggingOverEdge != null) {
									hSize = qx - (draggingOverEdge.edge == LEFT? draggingOverEdge.quad.getLeft().floatValue() : draggingOverEdge.quad.getRight().floatValue());
								} else {
									hSize = qx - glX;
								}
								break;
								
							case RIGHT:
								if (draggingOverEdge != null) {
									hSize = (draggingOverEdge.edge == LEFT? draggingOverEdge.quad.getLeft().floatValue() : draggingOverEdge.quad.getRight().floatValue()) - qx;
								} else {
									hSize = glX - qx;
								}
								break;
								
							case BOTTOM:
								if (draggingOverEdge != null) {
									hSize = qy - (draggingOverEdge.edge == BOTTOM? draggingOverEdge.quad.getBottom().floatValue() : draggingOverEdge.quad.getTop().floatValue());
								} else {
									hSize = qy - glY;
								}
								break;
								
							case TOP:
								if (draggingOverEdge != null) {
									hSize = (draggingOverEdge.edge == BOTTOM? draggingOverEdge.quad.getBottom().floatValue() : draggingOverEdge.quad.getTop().floatValue()) - qy;
								} else {
									hSize = glY - qy;
								}
								break;
						}
						
						hSize = Utils.clamp(hSize, EDGE_WIDTH_F + 0.02f, 1.0f);
						
						dq.setLeft(new BigDecimal(qx - hSize));
						dq.setRight(new BigDecimal(qx + hSize));
						dq.setBottom(new BigDecimal(qy - hSize));
						dq.setTop(new BigDecimal(qy + hSize));
						
						if (highlighted == player) {
							lastPropertyPane.update();
						}
					} else {
						switch(draggingEdge.edge) {
							case LEFT:
								float nLeft = draggingOverEdge != null? draggingOverEdge.edge == LEFT? draggingOverEdge.quad.getLeft().floatValue() : draggingOverEdge.quad.getRight().floatValue() : glX;
								
								if (qx - nLeft < EDGE_WIDTH_F + 0.01f) {
									nLeft = qx - EDGE_WIDTH_F - 0.01f;
								}
								
								dq.setLeft(new BigDecimal(nLeft));
								break;
								
							case RIGHT:
								float nRight = draggingOverEdge != null? draggingOverEdge.edge == LEFT? draggingOverEdge.quad.getLeft().floatValue() : draggingOverEdge.quad.getRight().floatValue() : glX;
								
								if (nRight - qx < EDGE_WIDTH_F + 0.01f) {
									nRight = qx + EDGE_WIDTH_F + 0.01f;
								}
								
								dq.setRight(new BigDecimal(nRight));
								break;
							
							case BOTTOM:
								float nBottom = draggingOverEdge != null? draggingOverEdge.edge == BOTTOM? draggingOverEdge.quad.getBottom().floatValue() : draggingOverEdge.quad.getTop().floatValue() : glY;
								
								if (qy - nBottom < EDGE_WIDTH_F + 0.01f) {
									nBottom = qy - EDGE_WIDTH_F - 0.01f;
								}
								
								dq.setBottom(new BigDecimal(nBottom));
								break;
								
							case TOP:
								float nTop = draggingOverEdge != null? draggingOverEdge.edge == BOTTOM? draggingOverEdge.quad.getBottom().floatValue() : draggingOverEdge.quad.getTop().floatValue() : glY;
								
								if (nTop - qy < EDGE_WIDTH_F + 0.01f) {
									nTop = qy + EDGE_WIDTH_F + 0.01f;
								}
								
								dq.setTop(new BigDecimal(nTop));
								break;
						}
						
						if (highlighted == dq) {
							lastPropertyPane.update();
						}
					}
				} else {
					scrollX += dx;
					scrollY += dy;
				}
				
				clampScroll();
				
				mouseX = nMouseX;
				mouseY = nMouseY;
				
				repaint();
			}
		});
		
		drawable.setOnMouseMoved(e -> {
			mouseX = e.getX();
			mouseY = e.getY();
			
			double invWidth = 1.0 / drawable.getWidth();
			
			double rScrollX = scrollX * invWidth * 2.0;
			double rScrollY = scrollY * invWidth * 2.0;
			
			float glX = (float) ((mouseX * invWidth * 2.0 - 1.0) / zoom - rScrollX);
			float glY = (float) ((1.0 - mouseY * invWidth * 2.0) / zoom + rScrollY);
			
			draggingEdge = null;
			draggingOverEdge = null;
			
			if (spawningQuad != null) {
				float hSize = DEFAULT_QUAD_SIZE * 0.5f;
				
				spawningQuad.setLeft(new BigDecimal(glX - hSize));
				spawningQuad.setRight(new BigDecimal(glX + hSize));
				spawningQuad.setBottom(new BigDecimal(glY - hSize));
				spawningQuad.setTop(new BigDecimal(glY + hSize));
				
				repaint();
			} else {
				QuadEdge edge;
				
				edge = checkEdgePress(target, false, glX, glY);
				
				if (edge != null) {
					draggingEdge = edge;
					draggingBoundary = false;
				}
				
				edge = checkEdgePress(boundary, true, glX, glY);
				
				if (edge != null) {
					draggingEdge = edge;
					draggingBoundary = true;
				}
				
				for (Quad quad : quads.values()) {
					edge = checkEdgePress(quad, false, glX, glY);
					
					if (edge != null) {
						draggingEdge = edge;
						draggingBoundary = false;
					}
				}
				
				edge = checkEdgePress(player, false, glX, glY);
				
				if (edge != null) {
					draggingEdge = edge;
					draggingBoundary = false;
				}

				repaint();
			}
		});
		
		drawable.setOnMouseExited(e -> {
			scrolling = false;
		});
		
		super.boundsInLocalProperty().addListener((ov, o, n) -> {
			aspectRatio = drawable.getWidth() / drawable.getHeight();
			clampScroll();
			repaint();
		});
		
		super.getChildren().add(drawable);
	}
	
	public void load(InputStream levelStream) {
		Level level = new Level();
		level.read(levelStream);
		
		player = level.getPlayer();
		target = level.getTarget();
		boundary = level.getBoundary();
		
		quads = level.getQuads();
	}
	
	public void repaint() {
		GraphicsContext context = drawable.getGraphicsContext2D();
		context.clearRect(0.0, 0.0, drawable.getWidth(), drawable.getHeight());
		
		double zScrollX = scrollX * zoom;
		double zScrollY = scrollY * zoom;
		
		context.setFill(getColor(theme.getDefaultQuadColor()));
		context.fillRect(0.0d, 0.0d, drawable.getWidth(), drawable.getHeight());
		
		drawRect(context, getRect(boundary), theme.getBackgroundColor(), zScrollX, zScrollY);
		
		drawRect(context, getRect(target), theme.getTargetColor(), zScrollX, zScrollY);
		
		for (Quad quad : quads.values()) {
			drawRect(context, getRect(quad), theme.forQuad(quad.getType()), zScrollX, zScrollY);
		}
		
		if (spawningQuad != null) {
			drawRect(context, getRect(spawningQuad), theme.getDefaultQuadColor(), zScrollX, zScrollY);
		}
		
		drawRect(context, getRect(player), theme.getPlayerColor(), zScrollX, zScrollY);
		
		if (highlighted != null) {
			Rectangle rect = getRect(highlighted);
			
			context.setStroke(Color.CADETBLUE);
			context.strokeRect(rect.getX() + zScrollX, rect.getY() * aspectRatio + zScrollY, rect.getWidth(), rect.getHeight() * aspectRatio);
		}
		
		if (draggingEdge != null) {
			Quad dq = draggingEdge.quad;
			Quad quad = null;
			
			if (draggingBoundary) {
				switch(draggingEdge.edge) {
					case LEFT:   quad = new Quad(dq.getLeft().subtract(EDGE_WIDTH_BD), dq.getLeft(), dq.getBottom(), dq.getTop()); break;
					case RIGHT:  quad = new Quad(dq.getRight(), dq.getRight().add(EDGE_WIDTH_BD), dq.getBottom(), dq.getTop()); break;
					case BOTTOM: quad = new Quad(dq.getLeft(), dq.getRight(), dq.getBottom().subtract(EDGE_WIDTH_BD), dq.getBottom()); break;
					case TOP:    quad = new Quad(dq.getLeft(), dq.getRight(), dq.getTop(), dq.getTop().add(EDGE_WIDTH_BD)); break;
				}
			} else {
				switch(draggingEdge.edge) {
					case LEFT:   quad = new Quad(dq.getLeft(), dq.getLeft().add(EDGE_WIDTH_BD), dq.getBottom(), dq.getTop()); break;
					case RIGHT:  quad = new Quad(dq.getRight().subtract(EDGE_WIDTH_BD), dq.getRight(), dq.getBottom(), dq.getTop()); break;
					case BOTTOM: quad = new Quad(dq.getLeft(), dq.getRight(), dq.getBottom(), dq.getBottom().add(EDGE_WIDTH_BD)); break;
					case TOP:    quad = new Quad(dq.getLeft(), dq.getRight(), dq.getTop().subtract(EDGE_WIDTH_BD), dq.getTop()); break;
				}
			}
			
			Rectangle rect = getRect(quad);
			
			context.setFill(Color.CADETBLUE);
			context.fillRect(rect.getX() + zScrollX, rect.getY() * aspectRatio + zScrollY, rect.getWidth(), rect.getHeight() * aspectRatio);
			
			if (draggingOverEdge != null) {
				Quad doq = draggingOverEdge.quad;
				quad = null;
				
				if (draggingOverBoundary) {
					switch(draggingOverEdge.edge) {
						case LEFT:   quad = new Quad(doq.getLeft().subtract(EDGE_WIDTH_BD), doq.getLeft(), doq.getBottom(), doq.getTop()); break;
						case RIGHT:  quad = new Quad(doq.getRight(), doq.getRight().add(EDGE_WIDTH_BD), doq.getBottom(), doq.getTop()); break;
						case BOTTOM: quad = new Quad(doq.getLeft(), doq.getRight(), doq.getBottom().subtract(EDGE_WIDTH_BD), doq.getBottom()); break;
						case TOP:    quad = new Quad(doq.getLeft(), doq.getRight(), doq.getTop(), doq.getTop().add(EDGE_WIDTH_BD)); break;
					}
				} else {
					switch(draggingOverEdge.edge) {
						case LEFT:   quad = new Quad(doq.getLeft(), doq.getLeft().add(EDGE_WIDTH_BD), doq.getBottom(), doq.getTop()); break;
						case RIGHT:  quad = new Quad(doq.getRight().subtract(EDGE_WIDTH_BD), doq.getRight(), doq.getBottom(), doq.getTop()); break;
						case BOTTOM: quad = new Quad(doq.getLeft(), doq.getRight(), doq.getBottom(), doq.getBottom().add(EDGE_WIDTH_BD)); break;
						case TOP:    quad = new Quad(doq.getLeft(), doq.getRight(), doq.getTop().subtract(EDGE_WIDTH_BD), doq.getTop()); break;
					}
				}
				
				rect = getRect(quad);
				
				context.setFill(Color.AQUAMARINE);
				context.fillRect(rect.getX() + zScrollX, rect.getY() * aspectRatio + zScrollY, rect.getWidth(), rect.getHeight() * aspectRatio);
			}
		}
	}
	
	private void drawRect(GraphicsContext context, Rectangle rect, com.infinityjump.core.game.Color color, double zScrollX, double zScrollY) {
		context.setFill(getColor(color));
		context.fillRect(rect.getX() + zScrollX, rect.getY() * aspectRatio + zScrollY, rect.getWidth(), rect.getHeight() * aspectRatio);
	}
	
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
		
		for (Entry<Integer, Quad> entry : quads.entrySet()) {
			Quad quad = entry.getValue();
			
			builder.append("quad[");
			builder.append("id=").append(entry.getKey()).append(", ");
			builder.append("left=").append(quad.getLeft()).append(", ");
			builder.append("right=").append(quad.getRight()).append(", ");
			builder.append("bottom=").append(quad.getBottom()).append(", ");
			builder.append("top=").append(quad.getTop()).append(", ");
			builder.append("type=").append(quad.getType().toString());
			
			if(quad instanceof TeleportQuad) {
				builder.append(", channel=").append(((TeleportQuad)quad).getChannel());
			}
			
			builder.append("]\n");
		}
		
		return builder.toString();
	}
	
	public void setZoom(double value) {
		zoom = value;
		clampScroll();
		repaint();
	}
	
	public void spawnQuad() {
		float x = (float) mouseX;
		float y = (float) mouseY;
		
		float hSize = DEFAULT_QUAD_SIZE * 0.5f;
		
		spawningQuad = new Quad(x - hSize, x + hSize, y - hSize, y + hSize);
		
		repaint();
	}
	
	private Rectangle getRect(Quad quad) {
		return new Rectangle(
				(quad.getLeft().floatValue() * zoom + 1.0d) * 0.5d * drawable.getWidth(),
				(1.0d - quad.getTop().floatValue() * zoom) * 0.5d * drawable.getHeight(),
				(quad.getRight().floatValue() - quad.getLeft().floatValue()) * zoom * 0.5d * drawable.getWidth(),
				(quad.getTop().floatValue() - quad.getBottom().floatValue()) * zoom * 0.5d * drawable.getHeight());
	}
	
	private Color getColor(com.infinityjump.core.game.Color color) {
		return Color.color(color.getRed(), color.getGreen(), color.getBlue());
	}

	private void showQuadProperties(Entry<Integer, Quad> quad) {
		QuadProperties props = new QuadProperties(quad, this);
		
		center.getItems().remove(lastPropertyPane);
		center.getItems().add(props);
		
		lastPropertyPane = props;
	}
	
	private void showPlayerProperties() {
		PlayerProperties props = new PlayerProperties(player, this);
		
		center.getItems().remove(lastPropertyPane);
		center.getItems().add(props);
		
		lastPropertyPane = props;
	}
	
	private void showTargetProperties() {
		TargetProperties props = new TargetProperties(target, this);
		
		center.getItems().remove(lastPropertyPane);
		center.getItems().add(props);
		
		lastPropertyPane = props;
	}
	
	private void showBoundaryProperties(Quad displayQuad) {
		BoundaryProperties props = new BoundaryProperties(boundary, this);
		
		center.getItems().remove(lastPropertyPane);
		center.getItems().add(props);
		
		lastPropertyPane = props;
	}
	
	private void removeProperties() {
		center.getItems().remove(lastPropertyPane);
		
		lastPropertyPane = null;
	}
	
	public void removeCurrentQuad(Entry<Integer, Quad> entry) {
		int size = quads.size();
		
		quads.remove(entry.getKey());
		highlighted = null;
		
		removeProperties();
		
		for (int i = entry.getKey() + 1; i < size; ++i) {
			quads.put(i - 1, quads.get(i));
		}
		
		quads.remove(size - 1);
	}
	
	private void clampScroll() {
		double cScrollX = scrollX / drawable.getWidth() * 2.0;
		double cScrollY = scrollY / drawable.getWidth() * 2.0;
		
		double yScrollError = (1.0 - 1.0 / aspectRatio) / zoom;
		
		scrollX = Utils.clamp(cScrollX, boundary.getLeft().floatValue(), boundary.getRight().floatValue()) * drawable.getWidth() * 0.5;
		scrollY = Utils.clamp(cScrollY, boundary.getBottom().floatValue() - yScrollError, boundary.getTop().floatValue() - yScrollError) * drawable.getWidth() * 0.5;
	}
	
	private QuadEdge checkEdgePress(Quad quad, boolean boundary, double glX, double glY) {
		if (boundary) {
			if (glX >= quad.getLeft().floatValue() - EDGE_WIDTH
					&& glX <= quad.getLeft().floatValue()
					&& glY >= quad.getBottom().floatValue()
					&& glY <= quad.getTop().floatValue()) {
				return new QuadEdge(quad, LEFT);
			} else if (glX >= quad.getRight().floatValue()
					&& glX <= quad.getRight().floatValue() + EDGE_WIDTH
					&& glY >= quad.getBottom().floatValue()
					&& glY <= quad.getTop().floatValue()) {
				return new QuadEdge(quad, RIGHT);
			} else if (glX >= quad.getLeft().floatValue()
					&& glX <= quad.getRight().floatValue()
					&& glY >= quad.getBottom().floatValue() - EDGE_WIDTH
					&& glY <= quad.getBottom().floatValue()) {
				return new QuadEdge(quad, BOTTOM);
			} else if (glX >= quad.getLeft().floatValue()
					&& glX <= quad.getRight().floatValue()
					&& glY >= quad.getTop().floatValue()
					&& glY <= quad.getTop().floatValue() + EDGE_WIDTH) {
				return new QuadEdge(quad, TOP);
			}
		} else {
			if (glX >= quad.getLeft().floatValue()
					&& glX <= quad.getLeft().floatValue() + EDGE_WIDTH
					&& glY >= quad.getBottom().floatValue()
					&& glY <= quad.getTop().floatValue()) {
				return new QuadEdge(quad, LEFT);
			} else if (glX >= quad.getRight().floatValue() - EDGE_WIDTH
					&& glX <= quad.getRight().floatValue()
					&& glY >= quad.getBottom().floatValue()
					&& glY <= quad.getTop().floatValue()) {
				return new QuadEdge(quad, RIGHT);
			} else if (glX >= quad.getLeft().floatValue()
					&& glX <= quad.getRight().floatValue()
					&& glY >= quad.getBottom().floatValue()
					&& glY <= quad.getBottom().floatValue() + EDGE_WIDTH) {
				return new QuadEdge(quad, BOTTOM);
			} else if (glX >= quad.getLeft().floatValue()
					&& glX <= quad.getRight().floatValue()
					&& glY >= quad.getTop().floatValue() - EDGE_WIDTH
					&& glY <= quad.getTop().floatValue()) {
				return new QuadEdge(quad, TOP);
			}
		}
		
		return null;
	}
	
	public Quad setNewQuadType(int id, Quad.Type type) {
		Quad quad = quads.get(id);
		BigDecimal left = quad.getLeft();
		BigDecimal right = quad.getRight();
		BigDecimal bottom = quad.getBottom();
		BigDecimal top = quad.getTop();
		
		Quad nQuad = null;
		
		switch (type) {
			case BOUNCY: nQuad = new BouncyQuad(left, right, bottom, top); break;
			case DEADLY: nQuad = new DeadlyQuad(left, right, bottom, top); break;
			case STICKY: nQuad = new StickyQuad(left, right, bottom, top); break;
			case TELEPORT: nQuad = new TeleportQuad(left, right, bottom, top, 0); break;
			default: 
			case NORMAL: nQuad = new Quad(left, right, bottom, top); break;
		}
		
		quads.put(id, nQuad);
		
		return nQuad;
	}
}
