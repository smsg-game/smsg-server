package com.ldsg.battle.listener;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.event.BeforeHurtCalculateEvent;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.role.Hero;

public class BeforeHurtCalculateListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(BeforeHurtCalculateListener.class);

	/**
	 * 事件监听
	 * 
	 * @param e
	 */
	public void listen(IEvent e) {

		if (e.getClass() != BeforeHurtCalculateEvent.class) {
			return;
		}

		logger.debug("[事件]处理伤害结算前事件");

		Context context = e.getContext();
		List<Hero> heroList = e.getHeroList();

		//处理计算伤害前触发的buff
		this.handle(context, heroList, BattleStatus.CALCULATE_INJURY);
	}
}
