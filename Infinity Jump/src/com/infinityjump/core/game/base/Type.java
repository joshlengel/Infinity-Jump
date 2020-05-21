package com.infinityjump.core.game.base;

public enum Type {
	NORMAL("normal"),
	PLAYER("player"),
	BOUNDARY("boundary"),
	TARGET("target"),
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
