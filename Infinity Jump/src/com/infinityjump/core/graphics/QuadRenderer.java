package com.infinityjump.core.graphics;

import java.util.ArrayList;
import java.util.List;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.game.Camera;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.quad.QuadRenderable;

public class QuadRenderer {
	
	private List<QuadRenderable> renderables = new ArrayList<QuadRenderable>();
	
	public void addRenderable(QuadRenderable q) {
		renderables.add(q);
	}
	
	public void clear() {
		renderables.clear();
	}
	
	public void render(Camera camera, Theme theme, float shiftX) {
		GraphicsAssets.quadShader.bind();
		GraphicsAssets.quadShader.setAspectRatio(Input.getAPI().getAspectRatio());
		GraphicsAssets.quadShader.setShiftX(shiftX);
		
		for (QuadRenderable r : renderables) {
			Color c = r.getColor(theme);
			
			GraphicsAssets.quadShader.setOffset(r.getX().floatValue() - camera.getX(), r.getY().floatValue() - camera.getY());
			GraphicsAssets.quadShader.setScale(r.getWidth().floatValue() * 0.5f, r.getHeight().floatValue() * 0.5f);
			GraphicsAssets.quadShader.setColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
			
			GraphicsAssets.quadModel.draw();
		}
	}
}
