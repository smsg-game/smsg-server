package com.ldsg.battle.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.event.AfterHeroDeadEvent;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.role.Hero;

public class AfterHeroDeadListener extends BaseListener implements IEventListener {
	private static Logger logger = Logger.getLogger(AfterHeroDeadListener.class);

	public void listen(IEvent e) {

		if (e.getClass() != AfterHeroDeadEvent.class) {
			return;
		}

		logger.debug("[事件]处理武将死亡后事件");

		// 处理死亡后触发的buff
		this.handle(e.getContext(), e.getHeroList(), BattleStatus.AFTER_DEAD);

		if (e.getHeroList().size() == 1) {

			Hero deadHero = e.getHeroList().get(0);

			Context context = e.getContext();

			for (Hero hero : context.getHeroList()) {

				List<Buff> removeList = new ArrayList<Buff>();

				for (Buff buff : hero.getBuffList()) {
					if (buff.getEffect().getRemoveType() == 1 && buff.getSource() != null && StringUtils.equalsIgnoreCase(buff.getSource().getPosition(), deadHero.getPosition())) {
						removeList.add(buff);
					}
				}

				for (Buff removeBuff : removeList) {
					hero.removeBuff(context, removeBuff);
				}
			}

		}

	}
}
