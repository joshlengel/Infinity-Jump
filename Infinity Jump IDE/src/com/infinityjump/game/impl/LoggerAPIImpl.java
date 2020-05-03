package com.infinityjump.game.impl;

import com.infinityjump.core.api.Logger.LoggerAPI;

public class LoggerAPIImpl implements LoggerAPI {

	@Override
	public void log(String msg) {
		System.out.println(msg);
	}

	@Override
	public void error(String msg) {
		System.err.println(msg);
	}
}
