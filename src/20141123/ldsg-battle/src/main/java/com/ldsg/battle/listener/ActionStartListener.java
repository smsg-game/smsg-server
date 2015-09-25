package com.ldsg.battle.listener;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.event.ActionStartEvent;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.role.Hero;

public class ActionStartListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(BeforeHurtCalculateListener.class);

	public void listen(IEvent e) {

		if (e.getClass() != ActionStartEvent.class) {
			return;
		}

		Context context = e.getContext();
		List<Hero> heroList = e.getHeroList();

		// 清除战报用到的一些数据
		context.cleanAttackVOList();
		context.cleanActionBuffVOList();

		logger.debug("[事件]处理行动开始事件");
		this.handle(context, heroList, BattleStatus.ACTION_START);
	}

}
