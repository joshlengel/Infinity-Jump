package com.infinityjump.core.game.base;

import java.util.function.Supplier;

import com.infinityjump.core.game.properties.QuadProperties;
import com.infinityjump.core.game.properties.BlockProperties;
import com.infinityjump.core.game.properties.PlayerProperties;
import com.infinityjump.core.game.properties.BouncyProperties;
import com.infinityjump.core.game.properties.TargetProperties;

public enum Type {
	NORMAL("normal", BlockProperties::new),
	PLAYER("player", PlayerProperties::new),
	BOUNDARY("boundary", BlockProperties::new),
	TARGET("target", TargetProperties::new),
	DEADLY("deadly", QuadProperties::new),
	BOUNCY("bouncy", BouncyProperties::new),
	STICKY("sticky", BlockProperties::new),
	TELEPORT("teleport", QuadProperties::new),
	ICE("ice", BlockProperties::new);
	
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
	
	Type(String name, Supplier<QuadProperties> generator) {
		this.name = name;
		this.props = generator;
	}
	
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
	
	public QuadProperties generateProperties() {
		return props.get();
	}
}
