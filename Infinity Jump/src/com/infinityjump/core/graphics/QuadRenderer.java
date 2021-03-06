package com.infinityjump.core.graphics;

import java.util.ArrayList;
import java.util.List;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.game.Camera;
import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Boundary;
import com.infinityjump.core.game.base.quad.QuadShape;

public class QuadRenderer {
	
	private List<QuadShape> renderables = new ArrayList<QuadShape>();
	
	public void addRenderable(QuadShape q) {
		renderables.add(q);
	}
	
	public void clear() {
		renderables.clear();
	}
	
	public void render(Camera camera, Theme theme, float shiftX) {
		GraphicsAssets.quadShader.bind();
		GraphicsAssets.quadShader.setAspectRatio(Input.getAPI().getAspectRatio());
		GraphicsAssets.quadShader.setShiftX(shiftX);
		
		for (QuadShape r : renderables) {
			Color c;
			
			if (r instanceof Boundary) {
				c = theme.getProperties("background").color;
			} else {
				c = theme.getProperties(r.getType().toString()).color;
			}
			
			GraphicsAssets.quadShader.setOffset(r.getX().floatValue() - camera.getX(), r.getY().floatValue() - camera.getY());
			GraphicsAssets.quadShader.setScale(r.getWidth().floatValue() * 0.5f, r.getHeight().floatValue() * 0.5f);
			GraphicsAssets.quadShader.setColor(c.r, c.g, c.b, c.a);
			
			GraphicsAssets.quadModel.draw();
		}
	}
}
