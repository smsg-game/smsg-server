package com.ldsg.battle.listener;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.constant.ReportConstant;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.event.RoundStartEvent;
import com.ldsg.battle.helper.BattleHeroHelper;
import com.ldsg.battle.helper.ReportHelper;
import com.ldsg.battle.role.Hero;

public class RoundStartListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(RoundStartListener.class);

	/**
	 * 事件监听
	 */
	public void listen(IEvent e) {
		if (e.getClass() != RoundStartEvent.class) {
			return;
		}

		Context context = e.getContext();
		List<Hero> heroList = e.getHeroList();

		logger.debug("[事件]处理回合开始前事件");

		this.setUpRound(context);

		this.sortStartOrder(context);

		this.handle(context, heroList, BattleStatus.ROUND_START);

		this.sortStartOrder(context);

		if (context.isFirstAction()) {
			context.setFirstAction(false);
		} else {
			context.appendReport("atl", ReportConstant.REPORT_COMMA_SPLIT_TAG);
		}

		// 战报处理
		context.appendReport("atl", ReportConstant.EMPTY_ATTACK_REPORT);
		context.appendReport("atl", ReportHelper.BuildBuffVOReport(context.getRoundBuffVOList()));

		// ,
		context.appendReport("atl", ReportConstant.REPORT_COMMA_SPLIT_TAG);

		context.appendReport("atl", ReportHelper.BuildMoraleReport(context, context.getHeroList()));

		context.appendReport("atl", ReportConstant.REPORT_LIST_END_TAG);

		// |
		if (!context.isFinish())// 只有战斗没结束才加分隔符
		{
			// context.appendReport("atl",
			// ReportConstant.REPORT_COMMA_SPLIT_TAG);
		}

		// 将本回合buff vo列表清空
		context.cleanRoundBuffVOList();

		// 清空已出场人员
		context.getOutPosList().clear();
	}

	// <summary>
	// 调试输出出场顺序
	// </summary>
	// <param name="context"></param>
	private void OutputStartOrder(Context context) {
		if (!logger.isDebugEnabled()) {
			return;
		}

		String order = "";
		for (String index : context.getStartOrderList()) {
			if (!order.equals("")) {
				order = order + ",";
			}

			Hero hero = null;
			if (index.startsWith(BattleConstant.ATTACK_PREFIX)) {
				hero = context.getAttackHeroDict().get(index);
			} else {
				hero = context.getDefenseHeroDict().get(index);
			}

			order = order + index + "[name:" + hero.getLogName() + ", speed:" + hero.getTotalAttribute(context, AttrConstant.SPEED) + ", life:" + hero.getTotalAttribute(context, AttrConstant.LIFE) + "]";
		}
		logger.debug("武将出场顺序为[" + order + "]");
	}

	/**
	 * 设定比赛回合数
	 * 
	 * @param context
	 */
	private void setUpRound(Context context) {
		int round = context.getRound();
		round++;
		context.setRound(round);
		logger.debug("===============第" + round + "回合开始===============");
		logger.debug("进攻方存活武将数[" + context.getAttackHeroDict().size() + "], 防守方存活武将数[" + context.getDefenseHeroDict().size() + "]");
	}

	/**
	 * 排定出场顺序进行排序
	 * 
	 * @param context
	 */
	private void sortStartOrder(Context context) {
		logger.debug("开始排定武将出场顺序");

		List<Hero> list = context.getHeroList();

		String preOrderPrefix = BattleConstant.DEFENSE_PREFIX;
		if (context.getDefenseLevel() > context.getAttackLevel()) {
			preOrderPrefix = BattleConstant.ATTACK_PREFIX;
		}

		// 出场顺序
		List<String> startOrderList = BattleHeroHelper.getHeroStartOrder(list, preOrderPrefix);

		context.setStartOrderList(startOrderList);
		context.setStartOrderIndex(0);

		this.OutputStartOrder(context);

	}

}
