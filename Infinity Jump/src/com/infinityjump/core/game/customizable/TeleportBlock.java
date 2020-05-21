package com.infinityjump.core.game.customizable;

import java.math.BigDecimal;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.base.Type;
import com.infinityjump.core.game.base.Block;

public class TeleportBlock extends Block {

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
	private TeleportBlock cacheLinked;
	
	private boolean shouldTeleport;
	
	private EjectType ejectType;
	
	public TeleportBlock(BigDecimal left, BigDecimal right, BigDecimal bottom, BigDecimal top, int linkID, EjectType ejectType) {
		super(left, right, bottom, top);
		
		this.linkID = linkID;
		this.shouldTeleport = true;
		
		this.ejectType = ejectType;
	}
	
	@Override
	public Color getColor(Theme theme) {
		return theme.getTeleportQuadColor();
	}
	
	@Override
	public Type getType() {
		return Type.TELEPORT;
	}

	@Override
	public void update(Level level, BigDecimal dt) {
		this.cacheLinked = null;
		
		level.getBlocks().forEach((id, block) -> {
			if (block != this && block instanceof TeleportBlock) {
				TeleportBlock tBlock = (TeleportBlock)block;
				
				if (tBlock.linkID == this.linkID) {
					if (this.cacheLinked != null) {
						Logger.getAPI().error("Teleport block with linkID '" + linkID + "' cannot link to multiple other blocks");
					}
					
					this.cacheLinked = tBlock;
				}
			}
		});
		
		if (this.cacheLinked == null) {
			Logger.getAPI().error("No teleport block with linkID '" + linkID + "' found");
		}
	}
	
	@Override
	public void checkCollision(Player player, Collision collision) {
		if (!shouldTeleport) {
			if (player.getLeft().compareTo(right) > 0 || player.getRight().compareTo(left) < 0 || player.getBottom().compareTo(top) > 0 || player.getTop().compareTo(bottom) < 0) {
				shouldTeleport = true;
			}
			
			return;
		}
		
		super.checkCollision(player, collision);
	}
	
	@Override
	public void resolveCollision(Player player, Collision collision) {
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
	
	public TeleportBlock getCacheLinked() {
		return cacheLinked;
	}
	
	@Override
	public TeleportBlock clone() {
		return new TeleportBlock(left, right, bottom, top, linkID, ejectType);
	}
}
