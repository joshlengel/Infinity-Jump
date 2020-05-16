package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Quad;

public class TeleportQuad extends Quad {

	public static enum EjectType {
		LEFT_BOTTOM("left-bottom"),
		RIGHT_BOTTOM("right-bottom"),
		LEFT_TOP("left-top"),
		RIGHT_TOP("right-top");
		
		private String name;
		
		EjectType(String name) { this.name = name; }
		
		public static EjectType parseType(String str) {
			for (EjectType type : EjectType.values()) {
				if (type.name.contentEquals(str)) return type;
			}
			
			return null;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private int linkID;
	private TeleportQuad cacheLinked;
	
	private boolean shouldTeleport;
	
	private EjectType ejectType;
	
	public TeleportQuad(float left, float right, float bottom, float top, int linkID, EjectType ejectType) {
		this(new BigDecimal(left), new BigDecimal(right), new BigDecimal(bottom), new BigDecimal(top), linkID, ejectType);
	}
	
	public TeleportQuad(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top, int linkID, EjectType ejectType) {
		super(left, right, bottom, top);
		
		this.type = Type.TELEPORT;
		
		this.linkID = linkID;
		this.shouldTeleport = true;
		
		this.ejectType = ejectType;
	}

	public void updateStartFrame(Level level, BigDecimal dt) {
		this.cacheLinked = null;
		
		level.getQuads().forEach((id, quad) -> {
			if (quad != this && quad instanceof TeleportQuad) {
				TeleportQuad tQuad = (TeleportQuad)quad;
				
				if (tQuad.linkID == this.linkID) {
					if (this.cacheLinked != null) {
						Logger.getAPI().error("Teleport quad with linkID '" + linkID + "' cannot link to multiple other quads");
					}
					
					this.cacheLinked = tQuad;
				}
			}
		});
		
		if (this.cacheLinked == null) {
			Logger.getAPI().error("No teleport quad with linkID '" + linkID + "' found");
		}
	}
	
	@Override
	public void update(Player player, Collision collision, BigDecimal dt) {
		if (!shouldTeleport) {
			if (player.getLeft().compareTo(right) > 0 || player.getRight().compareTo(left) < 0 || player.getBottom().compareTo(top) > 0 || player.getTop().compareTo(bottom) < 0) {
				shouldTeleport = true;
			}
			
			return;
		}
		
		super.update(player, collision, dt);
	}
	
	@Override
	public void collided(Player player, Collision collision, BigDecimal dt) {
		if (this.cacheLinked == null)
			return;
		
		if (collision.type != Collision.Type.NONE && shouldTeleport) {
			collision.px = cacheLinked.x;
			collision.py = cacheLinked.y;
			
			switch (cacheLinked.ejectType) {
				case LEFT_BOTTOM:
					collision.vx = player.getVX().abs().negate();
					collision.vy = player.getVY().abs().negate();
					break;
				case LEFT_TOP:
					collision.vx = player.getVX().abs().negate();
					collision.vy = player.getVY().abs();
					break;
				case RIGHT_BOTTOM:
					collision.vx = player.getVX().abs();
					collision.vy = player.getVY().abs().negate();
					break;
				case RIGHT_TOP:
					collision.vx = player.getVX().abs();
					collision.vy = player.getVY().abs();
					break;
			}
			
			cacheLinked.shouldTeleport = false;
		}
	}
	
	@Override
	public void render(Theme theme, float scrollX, float scrollY) {
		render(theme.getTeleportQuadColor(), scrollX, scrollY);
	}
	
	public int getChannel() {
		return linkID;
	}
	
	public void setChannel(int linkID) {
		this.linkID = linkID;
	}
	
	public EjectType getEjectType() {
		return ejectType;
	}
	
	public void setEjectType(EjectType ejectType) {
		this.ejectType = ejectType;
	}
	
	@Override
	public TeleportQuad clone() {
		return new TeleportQuad(left, right, bottom, top, linkID, ejectType);
	}
}
