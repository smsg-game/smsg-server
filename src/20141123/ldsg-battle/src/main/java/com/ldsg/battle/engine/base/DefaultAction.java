package com.ldsg.battle.engine.base;

import org.apache.log4j.Logger;

import com.ldsg.battle.engine.AbstractAction;
import com.ldsg.battle.engine.Action;

public class DefaultAction extends AbstractAction {

	private static Logger logger = Logger.getLogger(DefaultAction.class);

	@Override
	public Action getNextAction() {

		if (context.isRoundEnd()) {
			logger.warn("本回合已经结束,不应该出现本方法的调用");
			return null;
		}
		return new DefaultAction();
	}

}
