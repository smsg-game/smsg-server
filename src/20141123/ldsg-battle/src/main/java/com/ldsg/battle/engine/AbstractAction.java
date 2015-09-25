package com.ldsg.battle.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.config.SkillConfig;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.constant.TargetSelectType;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.event.SkillReleaseStartEvent;
import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.handle.BuffHandleFactory;
import com.ldsg.battle.handle.IBuffHandle;
import com.ldsg.battle.helper.BattleHeroHelper;
import com.ldsg.battle.helper.SkillHelper;
import com.ldsg.battle.helper.TargetSelectHelper;
import com.ldsg.battle.role.Hero;
import com.ldsg.battle.vo.AttackVO;

/// <summary>
/// 行动
/// </summary>
public abstract class AbstractAction implements Action {

	private static Logger logger = Logger.getLogger(AbstractAction.class);

	// <summary>
	// 上下文
	// </summary>
	protected Context context;

	public void doAction(Context context) {
		this.context = context;

		if (context.getPreHeroPrefix() != null) {

			List<Hero> heroList = new ArrayList<Hero>();
			for (Hero hero : context.getHeroList()) {

				if (context.getOutPosList().contains(hero.getPosition())) {// 已经出过手了
					continue;
				}
				heroList.add(hero);
			}

			List<String> startOrderList = BattleHeroHelper.getHeroStartOrder(heroList, context.getPreHeroPrefix());

			if (startOrderList.isEmpty()) {
				context.setRoundEnd(true);
				return;
			}

			context.setStartOrderList(startOrderList);
			context.setStartOrderIndex(0);
		}

		Hero hero = this.context.getCurrentHero();
		context.setPreHeroPrefix(hero.getPrefix());
		context.getOutPosList().add(hero.getPosition());

		// 行动开始
		context.switchStatus(BattleStatus.ACTION_START);

		if (hero.isDead()) {// 掉血死掉了
			context.startOrderIndexInc();
			return;
		}

		if (hero.isDizzy())// 如果中了眩晕
		{
			logger.debug("当前武将中了眩晕,本次行动跳过.name[" + hero.getLogName() + "]");
			hero.setDizzy(false);
			this.appendNotActionReport(context);
		} else {
			this._doAction(context, hero);
		}

		// 行动结束
		context.switchStatus(BattleStatus.AFTER_ACTION);

		context.setCurrentBeAttackHero(null);
	}

	public abstract Action getNextAction();

	public boolean hasNextAction() {
		// 整个战斗都已经结束了
		if (context.isFinish()) {
			logger.debug("战斗已经结束,不需要执行下一行动!");
			return false;
		}

		return this.context.isRoundEnd() == false;
	}

	// <summary>
	// 爆击/格挡判断
	// </summary>
	// <param name="hero"></param>
	// <param name="target"></param>
	// <param name="skill"></param>
	// <returns>1:普通 2:爆击 3:挡格</returns>
	protected int critCheck(Hero hero, Hero target, Skill skill) {
		int checkValue = skill.getCheckValue();
		int critValue = BattleConstant.ATTACK_RESULT_NORMAL;

		// 暴击区间：（自身暴击 + 自身等级 * 1）* c
		// 格挡区间：（目标格挡 + 目标等级 * 1） * d
		// 普通区间：（自身等级 + 目标等级）/2 *20 * e
		// int selfLevel = hero.getLevel();
		long selfCrit = (long) hero.getTotalAttribute(context, AttrConstant.CRIT);
		long selfPoJi = (long) hero.getTotalAttribute(context, AttrConstant.PO_JI);// 自身破击
		// int targetLevel = target.getLevel();
		long targetParry = (long) target.getTotalAttribute(context, AttrConstant.PARRY);
		long targetRenXing = (long) target.getTotalAttribute(context, AttrConstant.REN_XING);

		if (SkillConfig.isAddLifeSkill(skill)) {// 加血不考虑
			targetParry = 0;
			targetRenXing = 0;
		}

		// 暴击区间
		// 如果不做暴击判断的技能，则爆击区间为0
		double critRangeValue = 0;
		if ((checkValue & 0x04) == 0x04) {
			// double paramC = skill.GetParam(2);
			// critRangeValue = (double)(selfCrit / (selfLevel * paramC * 100));
			critRangeValue = (double) (selfCrit - targetRenXing) / 100;
			if (critRangeValue < 0) {
				critRangeValue = 0;
			}
		}
		// 格挡区间
		// 如果不做格挡判断的技能，则格挡区间为0
		double parryRangeValue = 0;
		if ((checkValue & 0x02) == 0x02) {
			// double paramD = skill.GetParam(3);
			// parryRangeValue = (double)(targetParry / (targetLevel * paramD *
			// 100));
			parryRangeValue = (double) (targetParry - selfPoJi) / 100;
		}

		if ((critRangeValue + parryRangeValue) > 1) {
			critRangeValue = (double) critRangeValue / (critRangeValue + parryRangeValue);
			parryRangeValue = 1 - critRangeValue;
		}

		int critRange = (int) (critRangeValue * 100);
		int parryRange = (int) (parryRangeValue * 100);

		// 爆击 格挡不能超过 50%
		critRange = critRange > 50 ? 50 : critRange;
		parryRange = parryRange > 50 ? 50 : parryRange;

		int normalRange = 100 - critRange - parryRange;

		logger.debug("暴击,格挡,普通区间[" + critRange + "][" + parryRange + "][" + normalRange + "]");

		int randValue = RandomUtils.nextInt(critRange + parryRange + normalRange);
		String result = "普通";
		if (randValue < critRange) {
			critValue = BattleConstant.ATTACK_RESULT_CRIT;
			result = "暴击";
		} else if (randValue < (critRange + parryRange)) {
			critValue = BattleConstant.ATTACK_RESULT_PARRY;
			result = "格挡";
		}

		logger.debug("本次攻击暴击,格挡结果.[" + result + "]");

		// if (hero.getPosition().equals("a6")) {
		// return BattleConstant.ATTACK_RESULT_CRIT;
		// }

		return critValue;
	}

	// <summary>
	// 执行一次反击(相当于做一次普通攻击)
	// </summary>
	// <param name="hero"></param>
	// <param name="target"></param>
	protected int doCounterAttack(Context context, Hero hero, Hero target) {

		context.setCurrentIsCounter(true);

		// 普通攻击技能
		Skill skill = SkillConfig.getCounterSkill();// 获取反击技能

		List<Effect> effectList = skill.getEffectList();

		if (effectList.size() != 1) {
			String message = "普通攻击的效果数不等于1!数据配置非法";
			logger.error(message);
			throw new BattleRuntimeException(message);
		}

		Effect effect = effectList.get(0);

		String funID = effect.getFunId();
		// 获取buff处理器
		IBuffHandle handler = BuffHandleFactory.getBuffHandler(funID);
		Buff buff = handler.create(effect, hero, target, context, BattleConstant.ATTACK_RESULT_NORMAL);
		buff.setReport(false);// 这种buff不需要加入战报
		handler.handle(target, buff, context);

		context.setCurrentBeAttackHero(null);
		context.setCurrentIsCounter(false);

		return (int) buff.getValue();
	}

	// <summary>
	// 处理一个效果
	// </summary>
	// <param name="hero"></param>
	// <param name="target"></param>
	// <param name="effect"></param>
	protected void handleEffect(Context context, Hero hero, Hero target, Effect effect, int critValue) {
		if (target.isImmuneDebuff() && effect.getType() == Effect.EFFECT_TYPE_DEC && effect.getEffectId() != EffectConfig.REDUCE_LIFE_EFFECT_ID) {
			logger.debug("当前被攻击者对debuff免疫.不对该buff做处理");
			return;
		}

		String funID = effect.getFunId();

		logger.debug("生成效果.主动武将[" + hero.getLogName() + "], 被动武将[" + target.getLogName() + "], 效果[" + effect.getRemark() + "]");

		// 获取buff处理器
		IBuffHandle handler = BuffHandleFactory.getBuffHandler(funID);
		Buff buff = handler.create(effect, hero, target, context, critValue);

		if (buff == null) {
			// 本次buff未命中
			logger.debug("本次Buff未命中");
			return;
		}

		buff.setSource(hero);
		buff.setTarget(target);

		if (effect.getImmediately() == 1) {// 立即生效的buff

			handler.handle(target, buff, context);
			Effect nextEffect = SkillConfig.getNextEffect(effect.getEffectUid());
			if (nextEffect != null) {
				handleEffect(context, nextEffect, buff, critValue);
			}
		} else {// 不是立即生效也要有下一效果 2013-11-26 修改
			Effect nextEffect = SkillConfig.getNextEffect(effect.getEffectUid());
			if (nextEffect != null) {
				handleEffect(context, nextEffect, buff, critValue);
			}
		}

		if (effect.getRound() > 0) {
			target.addBuff(context, buff);
		}
	}

	// <summary>
	// 处理一个效果，用于后续效果处理
	// </summary>
	// <param name="hero"></param>
	// <param name="target"></param>
	// <param name="effect"></param>
	protected void handleEffect(Context context, Effect effect, Buff prevBuff, int critValue) {
		Hero hero = prevBuff.getSource();
		Hero target = prevBuff.getTarget();

		if (target.isImmuneDebuff() && effect.getType() == Effect.EFFECT_TYPE_DEC && effect.getEffectId() != EffectConfig.REDUCE_LIFE_EFFECT_ID) {
			logger.debug("当前被攻击者对debuff免疫.不对该buff做处理");
			return;
		}

		String funID = effect.getFunId();

		logger.debug("生成效果.主动武将[" + hero.getLogName() + "], 被动武将[" + target.getLogName() + "], 效果[" + effect.getRemark() + "]");

		// 获取buff处理器
		IBuffHandle handler = BuffHandleFactory.getBuffHandler(funID);
		Buff buff = handler.create(effect, prevBuff, hero, target, context, critValue);

		if (buff == null) {
			// 本次buff未命中
			logger.debug("本次Buff未命中");
			return;
		}

		buff.setSource(hero);
		buff.setTarget(target);

		if (effect.getImmediately() == 1) {// 立即生效的buff

			if (prevBuff.getEffect().getNextSelectType() == Effect.NEXT_SELECT_TYPE_SOURCE) {
				handler.handle(hero, buff, context);
			} else {
				handler.handle(target, buff, context);
			}
			Effect nextEffect = SkillConfig.getNextEffect(effect.getEffectUid());
			if (nextEffect != null) {
				handleEffect(context, nextEffect, buff, critValue);
			}
		}
		// 后续效果不支持多回合的效果，只支持立即生效的效果
		// if (effect.getRound() > 0) {
		// target.addBuff(context, buff);
		// }
	}

	// <summary>
	// 是否反击的判断
	// </summary>
	// <param name="skill"></param>
	// <returns></returns>
	protected boolean isCounter(Hero hero, Hero target, Skill skill) {

		int checkValue = skill.getCheckValue();
		if ((checkValue & 0x01) != 0x01) {
			// 如果不做反击判断的,则默认是不反击
			return false;
		}

		double paramF = skill.getParam(5);
		// 固定反击率 + 武将的反击加成值
		double rangeValue = paramF + target.getTotalAttribute(context, AttrConstant.COUNTER_RATE) - hero.getTotalAttribute(context, AttrConstant.YIN_NI);

		logger.debug("反击概率.hero[" + target.getLogName() + "], range[" + rangeValue + "]");

		int randValue = RandomUtils.nextInt(100);

		if (randValue < rangeValue) {
			return true;
		} else {
			return false;
		}
	}

	// <summary>
	// 命中判断
	// </summary>
	// <param name="hero"></param>
	// <param name="?"></param>
	// <param name="skill"></param>
	protected boolean isHit(Hero hero, Hero target, Skill skill) {
		int checkValue = skill.getCheckValue();
		// 不做命中判断的话,都是命中
		if ((checkValue & 0x08) != 0x08) {
			return true;
		}

		// 命中 = 1 + (命中 / 命中系数 * 自身等级 ) - 闪避机率
		// 闪避机率 = 闪避 / ( 闪避系数 * 自身等级)

		int selfLevel = hero.getLevel();
		long selfHit = (long) hero.getTotalAttribute(context, AttrConstant.HIT);
		int targetLevel = target.getLevel();
		long targetDodge = (long) target.getTotalAttribute(context, AttrConstant.DODGE);

		double hitRangeValue = 1 + (double) selfHit / (100 + 0 * selfLevel);
		double notHitRangeValue = (double) targetDodge / (100 + 0 * targetLevel);

		double hitRange = (hitRangeValue - notHitRangeValue) * 100;

		int randValue = RandomUtils.nextInt(100);

		logger.debug("[" + skill.getName() + "].命中区间[0-" + hitRange + "],不命中区间[" + hitRange + "-100],本次随机值[" + randValue + "]");

		if (randValue < hitRange) {
			return true;
		} else {
			return false;
		}
	}

	// <summary>
	// 发放技能
	// </summary>
	// <param name="hero"></param>
	// <param name="skill"></param>
	protected boolean releaseSkill(Hero hero, Skill skill) {

		boolean success = false;

		int selectType = skill.getSelectType();

		// 获取目标选择对象
		List<Hero> targetHeroList = null;

		// 不同目标选择的效果映射
		Map<Integer, List<Effect>> selectEffectMap = new HashMap<Integer, List<Effect>>();
		List<Effect> effectList = skill.getEffectList();
		for (Effect effect : effectList) {

			int targetSelect = effect.getTargetSelect() == 0 ? skill.getSelectType() : effect.getTargetSelect();

			List<Effect> list = null;
			if (selectEffectMap.containsKey(targetSelect)) {
				list = selectEffectMap.get(targetSelect);
			} else {
				list = new ArrayList<Effect>();
			}
			list.add(effect);

			selectEffectMap.put(targetSelect, list);
		}

		if (hero.isChaos()) {// 中了混乱

			// 如果是混乱的话，攻击目标改为本方随机
			// 同时本次攻击的技能改为普通攻击
			targetHeroList = TargetSelectHelper.selectTarget(TargetSelectType.SELF_TEAM_RANDOM, this.context, hero);
			skill = SkillConfig.getNormalAttack(hero.getCareer());
			context.setCurrentSkill(skill);

			if (targetHeroList.size() == 0) {// 中了反间后只有自己的情况，因为这时武将不再行动

				this.appendNotActionReport(context);
				return false;
			}
		} else {
			targetHeroList = TargetSelectHelper.selectTarget(selectType, this.context, hero, skill);
		}

		if (targetHeroList.size() == 0) {
			logger.warn("技能发放对象列表为空");
		}

		// 被攻击者索引
		int index = 0;

		// 本回合击杀
		hero.setKillTarget(false);

		for (Hero target : targetHeroList) {
			logger.debug("[技能][" + skill.getName() + "]技能发放.name[" + skill.getName() + "],hero[" + hero.getLogName() + "], target[" + target.getLogName() + "]");

			// 将当前被攻击武将加上到上下文中
			context.setCurrentBeAttackHero(target);

			// 广播技能发放前的事件
			hero.setTarget(target);
			IEvent et = new SkillReleaseStartEvent(context, hero);
			context.dispatchEvent(et);

			if (!selectEffectMap.containsKey(selectType)) {
				logger.error("技能没有任何效果.skill_id[" + skill.getSkillId() + "]");
				continue;
			}

			boolean result = this.releaseSkillForOneTarget(hero, target, skill, selectEffectMap.get(selectType), ++index);
			success |= result;

			if (target.isDead()) {
				hero.setKillTarget(true);
			}

			if (result == true) {
				if (SkillConfig.isAddMoraleSkill(skill)) {// 如果是普通攻击而且造成伤害,则对方的士气增加

					if (success && hero.isNotMorale() == false) {
						if (target.isCanNotAddMorale()) {
							logger.debug("目标中了不能加士气的buff,被攻击时不产生士气.被攻击武将[" + target.getLogName() + "]");
						} else {
							long morale = (long) target.getAttribute(AttrConstant.MORALE);
							morale += BattleConstant.MORALE_INC_VALUE;
							target.setAttribute(AttrConstant.MORALE, morale);
						}
					}
				}
			}

			// 重置
			hero.reset();

			// 置当前被攻击武将为空
			// context.CurrentBeAttackHero = null;
		}

		if (!SkillConfig.isNoramlAttack(skill)) {// 如果是发放了技能,则怒气清零

			hero.setAttribute(AttrConstant.MORALE, 0);
		}

		if (!success) {
			// 没命中，返回
			return success;
		}

		// 目标和技能目标选择不一样的武将
		for (Entry<Integer, List<Effect>> entry : selectEffectMap.entrySet()) {

			int targetSelect = entry.getKey();
			if (targetSelect == selectType) {
				continue;
			}
			List<Hero> otherTargetHeroList = TargetSelectHelper.selectTarget(targetSelect, this.context, hero);
			for (Hero targetHero : otherTargetHeroList) {
				if (!hero.isDead() || !StringUtils.equalsIgnoreCase(targetHero.getPosition(), hero.getPosition())) {
					for (Effect effect : entry.getValue()) {
						this.handleEffect(context, hero, targetHero, effect, BattleConstant.ATTACK_RESULT_NORMAL);
					}
				}
			}
		}

		return success;
	}

	// <summary>
	// 对单个目标发放技能
	// </summary>
	// <param name="attackHero">技能发放者</param>
	// <param name="defenseHero">技能接收者</param>
	// <param name="skill">技能</param>
	// <param name="skill">当前被攻击者次序</param>
	protected boolean releaseSkillForOneTarget(Hero hero, Hero target, Skill skill, List<Effect> effectList, int index) {
		AttackVO vo = new AttackVO();
		vo.setPosition(target.getPosition());

		// 命中|闪避
		if (!this.isHit(hero, target, skill)) {
			// 如果不命中，本次攻击就算完了
			logger.debug("本次攻击不命中.攻击武将[" + hero.getLogName() + "], 被攻击武将[" + target.getLogName());
			vo.setHit(0);
			this.context.appendAttackVO(vo);

			return false;
		} else {
			vo.setHit(1);
		}

		// 暴击|格挡
		int critValue = this.critCheck(hero, target, skill);
		vo.setCritValue(critValue);

		for (Effect effect : effectList) {

			if (this.context.isFinish()) {
				// 战斗已经结束,不再处理剩下的buff了
				break;
			}
			// 处理效果
			this.handleEffect(context, hero, target, effect, critValue);
		}

		// 是否反击(执行一次普通攻击)
		boolean isCounter = false;
		if (target.isPreDead() == false && index == 1) {// 没死才会有反击&第一个受攻者才会反击
			isCounter = this.isCounter(hero, target, skill);
		}
		target.setPreDead(false);

		if (isCounter) {
			logger.debug("执行一次反击.反击武将[" + target.getLogName() + "], 被反击武将[" + hero.getLogName() + "]");
			vo.setCounter(this.doCounterAttack(context, target, hero));
		}
		this.context.appendAttackVO(vo);

		return true;
	}

	// <summary>
	// 行动为空时的战报处理
	// </summary>
	// <param name="context"></param>
	private void appendNotActionReport(Context context) {
		context.setCurrentSkill(SkillConfig.getEmptySkill());
		AttackVO vo = new AttackVO();
		vo.setHit(0);
		context.appendAttackVO(vo);
	}

	private void _doAction(Context context, Hero hero) {
		// 获取当前攻击武将的技能
		Skill skill = SkillHelper.selectSkill(hero, this.context);
		this.context.setCurrentSkill(skill);

		boolean success = this.releaseSkill(hero, skill);

		if (SkillConfig.isNoramlAttack(skill)) {// 如果是普通攻击而且造成伤害,则士气值增加

			if (success) {
				if (hero.isCanNotAddMorale()) {
					logger.debug("用户不能加士气，用户中了不能加士气的bug.[" + hero.getLogName() + "]");
				} else {
					hero.addAttribute(context, AttrConstant.MORALE, BattleConstant.MORALE_INC_VALUE);
				}
			}
		}
	}
}
