package com.ldsg.battle.engine.base;

import org.apache.log4j.Logger;

import com.ldsg.battle.engine.AbstractBattle;
import com.ldsg.battle.engine.Round;

public class DefaultBattle extends AbstractBattle {

	private static Logger logger = Logger.getLogger(DefaultBattle.class);

	@Override
	protected Round round() {

		if (context.isRoundEnd()) {
			logger.warn("本回合已经结束,不应该出现本方法的调用");
			return null;
		}
		return new DefaultRound();
	}

}
