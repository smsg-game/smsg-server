package com.ldsg.battle.listener;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.constant.ReportConstant;
import com.ldsg.battle.event.ActionEndEvent;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.helper.ReportHelper;
import com.ldsg.battle.role.Hero;

public class ActionEndListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(ActionEndListener.class);

	public void listen(IEvent e) {
		if (e.getClass() != ActionEndEvent.class) {
			return;
		}

		logger.debug("[事件]处理行动结束后事件");

		Context context = e.getContext();

		// buff 处理
		this.handle(context, e.getHeroList(), BattleStatus.AFTER_ACTION);

		// 战报封装

		if (context.isFirstAction()) {
			context.setFirstAction(false);
		} else {
			context.appendReport("atl", ReportConstant.REPORT_COMMA_SPLIT_TAG);
		}

		context.appendReport("atl", ReportConstant.REPORT_LIST_START_TAG);

		context.appendReport("atl", ReportHelper.BuildAttackReport(context, context.getCurrentHero(), context.getCurrentSkill(), context.getAttackVOList()));
		context.appendReport("atl", ReportHelper.BuildBuffVOReport(context.getActionBuffVOList()));

		// ,
		context.appendReport("atl", ReportConstant.REPORT_COMMA_SPLIT_TAG);

		context.appendReport("atl", ReportHelper.BuildMoraleReport(context, context.getHeroList()));

		context.appendReport("atl", ReportConstant.REPORT_LIST_END_TAG);

		/*
		 * if (!context.isFinish() && context.getRound() <=
		 * BattleConstant.MAX_ROUND) { // , context.appendReport("atl",
		 * ReportConstant.REPORT_COMMA_SPLIT_TAG); } else {
		 * logger.debug("is finish"); }
		 */

		Hero hero = context.getCurrentHero();

		if (hero != null) {
			logger.debug("hero:" + hero.getLogName());
		}

		// 武将当前出场顺序值+1
		context.startOrderIndexInc();

		// 清除当前出场武将值
		context.cleanCurrentHero();

		// 清除战报用到的一些数据
		context.cleanAttackVOList();
		context.cleanActionBuffVOList();
	}

}
