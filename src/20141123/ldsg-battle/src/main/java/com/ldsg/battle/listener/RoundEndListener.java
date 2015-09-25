package com.ldsg.battle.listener;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.event.RoundEndEvent;
import com.ldsg.battle.role.Hero;

public class RoundEndListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(BeforeHeroDeadListener.class);

	// <summary>
	// 事件监听
	// </summary>
	// <param name="e"></param>
	public void listen(IEvent e) {
		if (e.getClass() != RoundEndEvent.class) {
			return;
		}

		logger.debug("[事件]处理回合结束事件");

		Context context = e.getContext();
		List<Hero> heroList = e.getHeroList();

		this.cleanBuff(context, heroList);

		if (logger.isDebugEnabled()) {
			this.outputBuffList(context);
		}
	}

	// <summary>
	// buff清理
	// </summary>
	// <param name="context"></param>
	// <param name="heroList"></param>
	private void cleanBuff(Context context, List<Hero> heroList) {
		for (Hero hero : heroList) {
			// 标记哪些buff已经失效
			this.markDelete(hero, hero.getBuffList());
			// 清除已经标记为删除的buff
			hero.cleanBuff(context);

		}
	}

	// <summary>
	// buff计算
	// </summary>
	// <param name="hero"></param>
	// <param name="buffList"></param>
	private void markDelete(Hero hero, List<Buff> buffList) {
		for (Buff buff : buffList) {
			if (buff.getRound() == Buff.ROUND_CONTINUED || buff.getRound() == Buff.ROUND_IMMEDIATELY)// 一直跟随的buff
			{
				continue;
			}

			buff.setRound(buff.getRound() - 1);
			if (buff.getRound() == 0) {
				hero.addDeleteBuff(buff);
			}

		}
	}

	// <summary>
	// debug输出武将的buff列表
	// </summary>
	// <param name="context"></param>
	private void outputBuffList(Context context) {
		for (Hero hero : context.getHeroList()) {
			logger.debug("武将buff列表.name[" + hero.getLogName() + "]");
			for (Buff buff : hero.getBuffList()) {
				logger.debug("buff.Remark[" + buff.getEffect().getRemark() + "], buff.Round[" + buff.getRound() + "], buff.TriggerType[" + buff.getEffect().getTriggerType() + "], buff.SelectType["
						+ buff.getEffect().getSelectType() + "]");
			}

		}
	}
}
