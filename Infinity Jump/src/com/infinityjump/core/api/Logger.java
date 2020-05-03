package com.infinityjump.core.api;

public final class Logger {

	public static interface LoggerAPI {
		void log(String msg);
		void error(String msg);
	}
	
	private static LoggerAPI api;
	
	public static void init(LoggerAPI api) {
		Logger.api = api;
	}
	
	public static LoggerAPI getAPI() {
		return api;
	}
}
