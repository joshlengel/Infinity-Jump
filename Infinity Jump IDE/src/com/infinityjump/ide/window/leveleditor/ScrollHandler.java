package com.infinityjump.ide.window.leveleditor;

import com.infinityjump.ide.Utils;

public class ScrollHandler {

	private LevelData data;
	
	public ScrollHandler(LevelData data) {
		this.data = data;
	}
	
	public void clampScroll() {
		double cScrollX = data.scrollX * data.cacheInvWidth * 2.0;
		double cScrollY = data.scrollY * data.cacheInvWidth * 2.0;
		
		double yScrollError = (1.0 - 1.0 / data.aspectRatio) * data.cacheInvZoom;
		
		data.scrollX = Utils.clamp(cScrollX, data.boundary.getLeft().doubleValue(), data.boundary.getRight().doubleValue()) * data.cacheWidth * 0.5;
		data.scrollY = Utils.clamp(cScrollY, data.boundary.getBottom().doubleValue() - yScrollError, data.boundary.getTop().doubleValue() - yScrollError) * data.cacheWidth * 0.5;
	}
}
