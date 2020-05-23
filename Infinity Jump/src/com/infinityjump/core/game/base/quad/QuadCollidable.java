package com.infinityjump.core.game.base.quad;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Theme;
import com.infinityjump.core.game.base.Player;
import com.infinityjump.core.game.level.Level;

public interface QuadCollidable extends QuadShape {

	void update(Level level, Theme theme, BigDecimal dt);
	void updateFrame(Level level, BigDecimal elapsed);
	void checkCollision(Player player, Collision collision);
	void resolveCollision(Player player, Collision collision);
}
