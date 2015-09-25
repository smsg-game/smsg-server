package com.ldsg.battle.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.ldsg.battle.ASObject;
import com.ldsg.battle.Context;
import com.ldsg.battle.bo.BattleInfo;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.constant.ReportConstant;
import com.ldsg.battle.helper.BattleHeroHelper;
import com.ldsg.battle.listener.ActionEndListener;
import com.ldsg.battle.listener.ActionStartListener;
import com.ldsg.battle.listener.AfterHeroDeadListener;
import com.ldsg.battle.listener.BattleStartListener;
import com.ldsg.battle.listener.BeforeHeroDeadListener;
import com.ldsg.battle.listener.BeforeHurtCalculateListener;
import com.ldsg.battle.listener.RoundEndListener;
import com.ldsg.battle.listener.RoundStartListener;
import com.ldsg.battle.listener.SkillReleaseStartListener;
import com.ldsg.battle.role.Hero;

/**
 * 战斗处理的基本实现,只完成公用逻辑，不同的战斗逻辑由子类实现
 * 
 * @author jacky
 * 
 */
public abstract class AbstractBattle implements Battle {

	private static Logger logger = Logger.getLogger(AbstractAction.class);

	private static Logger reportLogger = Logger.getLogger("report");

	/**
	 * 比赛上下文,所以比赛状态只允许保存在该上下文里, 其它模块只处理逻辑，不允许保存比赛状态
	 */
	protected Context context;

	public Map<String, Object> execute(BattleInfo attackInfo, BattleInfo defenseInfo) {

		context = new Context();

		// 添加比赛开始事件监听器
		context.addEventListener(new BattleStartListener());
		// 添加回合开始事件监听器
		context.addEventListener(new RoundStartListener());
		// 添加回合结束事件监听器
		context.addEventListener(new RoundEndListener());
		// 添加武将发放技能前事件监听器
		context.addEventListener(new SkillReleaseStartListener());
		// 添加伤害结算前事件监听器
		context.addEventListener(new BeforeHurtCalculateListener());
		// 添加行动开始事件监听器
		context.addEventListener(new ActionStartListener());
		// 添加行动结束事件监听器
		context.addEventListener(new ActionEndListener());
		// 添加武将死亡前事件监听器
		context.addEventListener(new BeforeHeroDeadListener());
		// 添加武将死亡后事件监听器
		context.addEventListener(new AfterHeroDeadListener());

		// 初始化
		this.init(attackInfo, defenseInfo);

		// 计算开始血量
		this.calculateTotalLife();

		Round round = this.round();

		// 切换到战斗开始
		context.switchStatus(BattleStatus.BATTLE_START);

		// 回合战报的开始
		context.appendReport("atl", ReportConstant.REPORT_LIST_START_TAG);

		boolean go = true;
		while (go) {
			// 执行一回合的战斗
			round.execute(context);

			// 由round去判断是否还有下一回合比赛
			boolean hasNextRound = round.hashNextRound();
			if (hasNextRound) {
				round = round.getNextRound();
			} else {
				go = false;
			}
		}

		context.appendReport("atl", ReportConstant.REPORT_LIST_END_TAG);

		// 计算剩余血量
		this.calculateTotaLeftlLife();

		return this.formatResult();
	}

	/**
	 * 格式化输出战斗结果
	 * 
	 * @return
	 */
	protected Map<String, Object> formatResult() {

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put(BattleConstant.BATTLE_INFO_RESULT_FLG_KEY, this.context.getResult());

		String str = this.context.getReport();

		obj.put(BattleConstant.BATTLE_INFO_REPORT_KEY, str);

		obj.put("A1", context.getAttackTotalLife());
		obj.put("A2", context.getAttackTotalLeftLife());
		obj.put("D1", context.getDefenseTotalLife());
		obj.put("D2", context.getDefenseTotalLeftLife());

		for (Hero hero : context.getHeroList()) {
			obj.put("L_" + hero.getPosition(), hero.getAttribute(AttrConstant.LIFE));
		}

		reportLogger.info(this.context.getReport());

		return obj;
	}

	/**
	 * 返回回合处理对象,子类可以实现自己的回合处理逻辑
	 * 
	 * @return
	 */
	protected abstract Round round();

	/**
	 * 加成系数的加成计算
	 * 
	 * @param addRatio
	 * @param heroDict
	 */
	private void calculateAddRatio(double addRatio, Map<String, Hero> heroDict) {

		if (addRatio == 0) {
			logger.debug("加成系数为0");
			return;
		}

		for (Entry<String, Hero> entry : heroDict.entrySet()) {
			Hero hero = entry.getValue();
			BattleHeroHelper.calculateAddRation(hero, addRatio);
		}
	}

	/**
	 * 计算剩余血量
	 */
	private void calculateTotaLeftlLife() {
		long attackTotalLeftLife = 0;

		for (Entry<String, Hero> entry : context.getAttackHeroDict().entrySet()) {
			Hero hero = entry.getValue();
			attackTotalLeftLife += hero.getAttribute(AttrConstant.LIFE);
		}

		context.setAttackTotalLeftLife(attackTotalLeftLife);

		long defenseTotalLeftLife = 0;

		for (Entry<String, Hero> entry : context.getDefenseHeroDict().entrySet()) {
			Hero hero = entry.getValue();
			defenseTotalLeftLife += hero.getAttribute(AttrConstant.LIFE);
		}

		context.setDefenseTotalLeftLife(defenseTotalLeftLife);
	}

	/**
	 * 计算血量
	 */
	private void calculateTotalLife() {
		long attackTotalLife = 0;

		for (Entry<String, Hero> entry : context.getAttackHeroDict().entrySet()) {
			Hero hero = entry.getValue();
			attackTotalLife += hero.getAttribute(AttrConstant.LIFE);
		}

		context.setAttackTotalLife(attackTotalLife);

		long defenseTotalLife = 0;

		for (Entry<String, Hero> entry : context.getDefenseHeroDict().entrySet()) {
			Hero hero = entry.getValue();
			defenseTotalLife += hero.getAttribute(AttrConstant.LIFE);
		}

		context.setDefenseTotalLife(defenseTotalLife);
	}

	/**
	 * 初始化战斗双方数据
	 * 
	 * @param attackInfo
	 * @param defenseInfo
	 */
	private void init(BattleInfo attackInfo, BattleInfo defenseInfo) {
		// 进攻者等级
		context.setAttackLevel(attackInfo.getLevel());

		// 防守者等级
		context.setDefenseLevel(defenseInfo.getLevel());

		// 初始化进攻方的武将信息
		List<ASObject> attackHeroList = attackInfo.getHeroList();

		Map<String, Hero> attackHeroDict = BattleHeroHelper.parseHeroList(attackHeroList, BattleConstant.ATTACK_PREFIX);

		// 加成系数加成
		this.calculateAddRatio(attackInfo.getAddRatio(), attackHeroDict);

		this.context.setAttackHeroDict(attackHeroDict);

		// 初始化防守方的武将信息
		List<ASObject> defenseHeroList = defenseInfo.getHeroList();

		Map<String, Hero> defenseHeroDict = BattleHeroHelper.parseHeroList(defenseHeroList, BattleConstant.DEFENSE_PREFIX);

		// 加成系数加成
		this.calculateAddRatio(defenseInfo.getAddRatio(), defenseHeroDict);

		this.context.setDefenseHeroDict(defenseHeroDict);
	}

}
