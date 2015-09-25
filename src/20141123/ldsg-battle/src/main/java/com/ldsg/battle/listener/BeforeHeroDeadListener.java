package com.ldsg.battle.listener;

import org.apache.log4j.Logger;

import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.event.BeforeHeroDeadEvent;
import com.ldsg.battle.event.IEvent;

/**
 * 武将死亡前事件监听
 * 
 * @author jacky
 * 
 */
public class BeforeHeroDeadListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(BeforeHeroDeadListener.class);

	public void listen(IEvent e) {

		if (e.getClass() != BeforeHeroDeadEvent.class) {
			return;
		}

		logger.debug("[事件]处理武将死亡前事件");

		//处理死亡前触发的buff
		this.handle(e.getContext(), e.getHeroList(), BattleStatus.BEFORE_DEAD);
	}

}
