package com.infinityjump.core.game.base.quad;

import java.math.BigDecimal;

import com.infinityjump.core.game.Collision;
import com.infinityjump.core.game.Level;
import com.infinityjump.core.game.base.Player;

public interface QuadCollidable extends QuadShape {

	void update(Level level, BigDecimal dt);
	void updateFrame(Level level, BigDecimal elapsed);
	void checkCollision(Player player, Collision collision);
	void resolveCollision(Player player, Collision collision);
}
