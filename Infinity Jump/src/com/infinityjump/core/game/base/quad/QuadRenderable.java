package com.infinityjump.core.game.base.quad;

import com.infinityjump.core.game.Color;
import com.infinityjump.core.game.Theme;

public interface QuadRenderable extends QuadShape {

	Color getColor(Theme theme);
}
