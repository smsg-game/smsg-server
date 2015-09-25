package com.ldsg.battle.engine.base;

import com.ldsg.battle.engine.AbstractRound;
import com.ldsg.battle.engine.Action;
import com.ldsg.battle.engine.Round;
import com.ldsg.battle.exception.BattleRuntimeException;

public class DefaultRound extends AbstractRound {

	@Override
	public Round getNextRound() {

		if (context.isFinish()) {
			throw new BattleRuntimeException("战斗已经结束,本方法不应该被调用");
		}

		return new DefaultRound();
	}

	@Override
	protected Action Action() {
		return new DefaultAction();
	}

}
