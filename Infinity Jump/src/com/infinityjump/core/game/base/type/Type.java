package com.infinityjump.core.game.base.type;

import java.util.function.Supplier;

import com.infinityjump.core.game.properties.QuadProperties;
import com.infinityjump.core.game.properties.BlockProperties;
import com.infinityjump.core.game.properties.PlayerProperties;
import com.infinityjump.core.game.properties.BouncyProperties;
import com.infinityjump.core.game.properties.TargetProperties;

public enum Type {
	NORMAL("normal", BlockProperties::new, new NormalBlockLoader()),
	PLAYER("player", PlayerProperties::new, null),
	BOUNDARY("boundary", BlockProperties::new, null),
	TARGET("target", TargetProperties::new, null),
	DEADLY("deadly", QuadProperties::new, new DeadlyBlockLoader()),
	BOUNCY("bouncy", BouncyProperties::new, new BouncyBlockLoader()),
	STICKY("sticky", BlockProperties::new, new StickyBlockLoader()),
	TELEPORT("teleport", QuadProperties::new, new TeleportBlockLoader()),
	ICE("ice", BlockProperties::new, new IceBlockLoader());
	
	public static final Type[] CUSTOMIZABLES;
	
	static {
		Type[] values = Type.values();
		CUSTOMIZABLES = new Type[values.length - 3];
		
		int runningCount = 0;
		
		for (Type t : values) {
			if (t.equals(PLAYER) || t.equals(BOUNDARY) || t.equals(TARGET)) continue;
			
			CUSTOMIZABLES[runningCount++] = t;
		}
	}
	
	private String name;
	private Supplier<QuadProperties> props;
	private NormalBlockLoader loader;
	
	Type(String name, Supplier<QuadProperties> generator, NormalBlockLoader loader) {
		this.name = name;
		this.props = generator;
		this.loader = loader;
	}
	
	public static Type parseType(String str) {
		for (Type type : Type.values()) {
			if (type.name.contentEquals(str)) return type;
		}
		
		return null;
	}
	
	public NormalBlockLoader getBlockLoader() {
		return loader;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public QuadProperties generateProperties() {
		return props.get();
	}
}
