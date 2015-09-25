package com.ldsg.battle.listener;

import org.apache.log4j.Logger;

import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.event.SkillReleaseStartEvent;

public class SkillReleaseStartListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(SkillReleaseStartListener.class);

	public void listen(IEvent e) {
		if (e.getClass() != SkillReleaseStartEvent.class) {
			return;
		}

		logger.debug("[事件]处理技能发放前事件");

		//处理技能发放前触发的buff
		this.handle(e.getContext(), e.getHeroList(), BattleStatus.BEFORE_RELEASE);
	}

}