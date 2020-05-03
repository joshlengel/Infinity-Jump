package com.infinityjump.core.game.base;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.graphics.Model;
import com.infinityjump.core.graphics.QuadShader;

public class Quad {
	
	private static final BigDecimal SURFACE_DRAG = new BigDecimal(5.0);
	
	public static final BigDecimal point5 = new BigDecimal(0.5);
	public static final BigDecimal one = new BigDecimal(1);

	protected static Model model;
	public static QuadShader shader;
	
	public static void init(InputStream vertexSource, InputStream fragmentSource) {
		ByteBuffer vBuffer = ByteBuffer.allocateDirect(Float.BYTES * 2 * 4).order(ByteOrder.nativeOrder());
		vBuffer.putFloat(-1.0f);
		vBuffer.putFloat(-1.0f);
		
		vBuffer.putFloat( 1.0f);
		vBuffer.putFloat(-1.0f);
		
		vBuffer.putFloat(-1.0f);
		vBuffer.putFloat( 1.0f);
		
		vBuffer.putFloat( 1.0f);
		vBuffer.putFloat( 1.0f);
		
		vBuffer.position(0);
		
		ByteBuffer iBuffer = ByteBuffer.allocateDirect(6).order(ByteOrder.nativeOrder());
		iBuffer.put((byte)0);
		iBuffer.put((byte)1);
		iBuffer.put((byte)3);
		
		iBuffer.put((byte)0);
		iBuffer.put((byte)3);
		iBuffer.put((byte)2);
		
		iBuffer.position(0);
	
		model = new Model(1);
		model.setBufferData(0, 2, vBuffer);
		model.setElementBufferData(iBuffer, 6);
		model.unbind();
		
		QuadShader.init(vertexSource, fragmentSource);
		shader = new QuadShader();
	}
	
	public static void terminate() {
		shader.destroy();
		QuadShader.terminate();
		
		model.destroy();
	}
	
	public enum Type {
		NORMAL("normal"),
		DEADLY("deadly"),
		BOUNCY("bouncy"),
		STICKY("sticky"),
		TELEPORT("teleport");
		
		private String name;
		
		Type(String name) { this.name = name; }
		
		public static Type parseType(String str) {
			for (Type type : Type.values()) {
				if (type.name.contentEquals(str)) return type;
			}
			
			return null;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	protected BigDecimal left, right, bottom, top;
	protected BigDecimal x, y, width, height;
	
	protected BigDecimal vx, vy;
	
	protected Type type;
	
	public Quad(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		
		x = this.left.add(this.right).multiply(point5);
		y = this.bottom.add(this.top).multiply(point5);
		width = this.right.subtract(this.left);
		height = this.top.subtract(this.bottom);
		
		vx = BigDecimal.ZERO;
		vy = BigDecimal.ZERO;
		
		this.type = Type.NORMAL;
	}
	
	public Quad(float left, float right, float bottom, float top) {
		this(new BigDecimal(left), new BigDecimal(right), new BigDecimal(bottom), new BigDecimal(top));
	}
	
	public void updateStartFrame(Level level, BigDecimal dt) {}
	
	public void updateEndFrame(Level level, BigDecimal dt) {
		BigDecimal dx = dt.multiply(vx);
		BigDecimal dy = dt.multiply(vy);
		
		x = x.add(dx);
		y = y.add(dy);
		
		left = left.add(dx);
		right = right.add(dx);
		bottom = bottom.add(dy);
		top = top.add(dy);
	}
	
	public void update(Player player, Collision collision, BigDecimal dt) {
		// calculate collision
		if (player.left.compareTo(right) >= 0 && player.ivx.compareTo(vx) < 0) {
			// ix = vx * t + right
			// ix = player.vx * t + player.left
			// t = (player.left - right) / (vx - player.vx)
			
			BigDecimal dv = vx.subtract(player.ivx);
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.left.subtract(right).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dY = vy.multiply(time);
					BigDecimal pDY = player.ivy.multiply(time);
					
					BigDecimal nBottom = bottom.add(dY);
					BigDecimal nTop = top.add(dY);
					
					BigDecimal nPBottom = player.bottom.add(pDY);
					BigDecimal nPTop = player.top.add(pDY);
					
					if (nPTop.compareTo(nBottom) > 0 && nPBottom.compareTo(nTop) < 0) {
						collision.quad = this;
						
						collision.px = right.add(vx.multiply(time)).add(player.hWidth);
						collision.py = player.y.add(pDY);
						
						collision.time = time;
						
						collision.type = Collision.Type.RIGHT;
					}
				}
			}
		} else if (left.compareTo(player.right) >= 0 && player.ivx.compareTo(vx) > 0) {
			// ix = vx * t + left
			// ix = player.vx * t + player.right
			// t = (player.right - left) / (vx - player.vx)
			
			BigDecimal dv = vx.subtract(player.ivx);
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.right.subtract(left).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dY = vy.multiply(time);
					BigDecimal pDY = player.ivy.multiply(time);
					
					BigDecimal nBottom = bottom.add(dY);
					BigDecimal nTop = top.add(dY);
					
					BigDecimal nPBottom = player.bottom.add(pDY);
					BigDecimal nPTop = player.top.add(pDY);
					
					if (nPTop.compareTo(nBottom) > 0 && nPBottom.compareTo(nTop) < 0) {
						collision.quad = this;
						
						collision.px = left.add(vx.multiply(time)).subtract(player.hWidth);
						collision.py = player.y.add(pDY);
						
						collision.time = time;
						
						collision.type = Collision.Type.LEFT;
					}
				}
			}
		} else if (player.bottom.compareTo(top) >= 0 && player.ivy.compareTo(vy) < 0) {
			// iy = vy * t + top
			// iy = player.vy * t + player.bottom
			// t = (player.bottom - top) / (vy - player.vy)
			
			BigDecimal dv = vy.subtract(player.ivy);
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.bottom.subtract(top).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dX = vx.multiply(time);
					BigDecimal pDX = player.ivx.multiply(time);
					
					BigDecimal nLeft = left.add(dX);
					BigDecimal nRight = right.add(dX);
					
					BigDecimal nPLeft = player.left.add(pDX);
					BigDecimal nPRight = player.right.add(pDX);
					
					if (nPRight.compareTo(nLeft) > 0 && nPLeft.compareTo(nRight) < 0) {
						collision.quad = this;
						
						collision.px = player.x.add(pDX);
						collision.py = top.add(vy.multiply(time)).add(player.hHeight);
						
						collision.time = time;
						
						collision.type = Collision.Type.TOP;
					}
				}
			}
		} else if (bottom.compareTo(player.top) >= 0 && player.ivy.compareTo(vy) > 0) {
			// iy = vy * t + bottom
			// iy = player.vy * t + player.top
			// t = (player.top - bottom) / (vy - player.vy)
			
			BigDecimal dv = vy.subtract(player.ivy);
			
			if (dv.compareTo(BigDecimal.ZERO) != 0) {
				BigDecimal time = player.top.subtract(bottom).divide(dv, 10, RoundingMode.DOWN);
				
				if (time.compareTo(collision.time) <= 0) {
					BigDecimal dX = vx.multiply(time);
					BigDecimal pDX = player.ivx.multiply(time);
					
					BigDecimal nLeft = left.add(dX);
					BigDecimal nRight = right.add(dX);
					
					BigDecimal nPLeft = player.left.add(pDX);
					BigDecimal nPRight = player.right.add(pDX);
					
					if (nPRight.compareTo(nLeft) > 0 && nPLeft.compareTo(nRight) < 0) {
						collision.quad = this;
						
						collision.px = player.x.add(pDX);
						collision.py = bottom.add(vy.multiply(time)).subtract(player.hHeight);
						
						collision.time = time;
						
						collision.type = Collision.Type.BOTTOM;
					}
				}
			}
		}
	}
	
	public void collided(Player player, Collision collision, BigDecimal dt) {
		switch (collision.type) {
		case LEFT:
			if (vx.compareTo(BigDecimal.ZERO) < 0) {
				collision.ivx = vx;
			} else {
				collision.ivx = BigDecimal.ZERO;
			}
			
			collision.vx = BigDecimal.ZERO;
			collision.vy = player.vy;
			
			break;
		case RIGHT:
			if (vx.compareTo(BigDecimal.ZERO) > 0) {
				collision.ivx = vx;
			} else {
				collision.ivx = BigDecimal.ZERO;
			}
			
			collision.vx = BigDecimal.ZERO;
			collision.vy = player.vy;
			break;
			
		case BOTTOM:
			if (vy.compareTo(BigDecimal.ZERO) < 0) {
				collision.ivy = vy;
			} else {
				collision.ivy = BigDecimal.ZERO;
			}
			
			collision.vx = player.vx;
			collision.vy = BigDecimal.ZERO;
			break;
			
		case TOP:
			if (vy.compareTo(BigDecimal.ZERO) > 0) {
				collision.ivy = vy;
			} else {
				collision.ivy = BigDecimal.ZERO;
			}
			
			collision.vx = player.vx.multiply(one.subtract(SURFACE_DRAG.multiply(dt)));
			collision.vy = BigDecimal.ZERO;
			break;
			
		default:
			break;
		}
	}
	
	protected void render(Color color, float scrollX, float scrollY) {
		shader.setOffset(x.floatValue() - scrollX, y.floatValue() - scrollY);
		shader.setScale(width.floatValue() * 0.5f, height.floatValue() * 0.5f);
		shader.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		
		model.draw();
	}
	
	public void render(Theme theme, float scrollX, float scrollY) {
		render(theme.getDefaultQuadColor(), scrollX, scrollY);
	}
	
	public BigDecimal getLeft() {
		return left;
	}
	
	public BigDecimal getRight() {
		return right;
	}
	
	public BigDecimal getBottom() {
		return bottom;
	}
	
	public BigDecimal getTop() {
		return top;
	}
	
	public BigDecimal getX() {
		return x;
	}
	
	public BigDecimal getY() {
		return y;
	}
	
	public BigDecimal getWidth() {
		return width;
	}
	
	public BigDecimal getHeight() {
		return height;
	}
	
	public Type getType() {
		return type;
	}
	
	public BigDecimal getVX() {
		return vx;
	}
	
	public BigDecimal getVY() {
		return vy;
	}
	
	public void setLeft(BigDecimal left) {
		this.left = left;
		
		x = left.add(right).multiply(point5);
		width = right.subtract(left);
	}
	
	public void setRight(BigDecimal right) {
		this.right = right;
		
		x = left.add(right).multiply(point5);
		width = right.subtract(left);
	}
	
	public void setBottom(BigDecimal bottom) {
		this.bottom = bottom;
		
		y = bottom.add(top).multiply(point5);
		height = top.subtract(bottom);
	}
	
	public void setTop(BigDecimal top) {
		this.top = top;
		
		y = bottom.add(top).multiply(point5);
		height = top.subtract(bottom);
	}
	
	public void setX(BigDecimal x) {
		this.x = x;
		
		final BigDecimal hWidth = width.multiply(point5);
		
		this.left = x.subtract(hWidth);
		this.right = x.add(hWidth);
	}
	
	public void setY(BigDecimal y) {
		this.y = y;
		
		final BigDecimal hHeight = height.multiply(point5);
		
		this.bottom = y.subtract(hHeight);
		this.top = y.add(hHeight);
	}
	
	public void setWidth(BigDecimal width) {
		this.width = width;
		
		final BigDecimal hWidth = width.multiply(point5);
		
		this.left = x.subtract(hWidth);
		this.right = x.add(hWidth);
	}
	
	public void setHeight(BigDecimal height) {
		this.height = height;
		
		final BigDecimal hHeight = height.multiply(point5);
		
		this.bottom = y.subtract(hHeight);
		this.top = y.add(hHeight);
	}
	
	// Only use in level editor !!!
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setVX(BigDecimal vx) {
		this.vx = vx;
	}
	
	public void setVY(BigDecimal vy) {
		this.vy = vy;
	}
	
	@Override
	public Quad clone() {
		return new Quad(left.floatValue(), right.floatValue(), bottom.floatValue(), top.floatValue());
	}
}
