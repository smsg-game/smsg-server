package com.ldsg.battle.handle;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.TargetSelectType;
import com.ldsg.battle.engine.AbstractAction;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.helper.TargetSelectHelper;
import com.ldsg.battle.role.Hero;

public class AddLifeHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(AbstractAction.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {

		Buff buff = BuffHelper.Effect2Buff(effect);

		double paramA = effect.getParam("a", 0d);
		double paramB = effect.getParam("b", 0d);
		double paramTL = effect.getParam("tl", 0d);

		double value;
		if (paramTL > 0) {
			value = 0;
		} else {
			long maxLife = (long) hero.getTotalAttribute(context, AttrConstant.MAX_LIFE);
			logger.debug("本次加血的值由武将的最大生命值和策略攻击运算得到.最大生命值[" + maxLife + "], 换算系数[" + paramA + "]");
			value = hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK) * paramA + (double) maxLife * paramB;
		}

		if (critValue == BattleConstant.ATTACK_RESULT_CRIT) {// 爆击
			value = (int) ((float) value * 1.5f);
		}

		// 治疗加成
		double addLifeAddRatio = hero.getAttribute(AttrConstant.ADD_LIFE_ADD_RATIO);
		if (addLifeAddRatio > 0) {
			value = value * addLifeAddRatio;
		}

		value = value * (90 + RandomUtils.nextInt(10)) / 100;

		buff.setValue((long) value);

		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {

		logger.debug("处理加/减生命buff");

		Effect effect = buff.getEffect();

		int selectType = effect.getSelectType();

		if (selectType == TargetSelectType.SELF) {
			this.handlerForSelf(hero, buff, context);
		} else {
			this.handlerForOhter(hero, buff, context);
			buff.setReport(false);
		}
	}

	public void handleRemove(Hero hero, Buff buff) {
		// throw new NotImplementedException();
	}

	/**
	 * 给它人加buff的
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	private void handlerForOhter(Hero hero, Buff buff, Context context) {
		logger.debug("处理增其它人血量的buff.name[" + hero.getLogName() + "]");

		Effect effect = buff.getEffect();
		int selectType = effect.getSelectType();

		List<Hero> heroList = TargetSelectHelper.selectTarget(selectType, context, hero);

		for (Hero target : heroList) {
			if (target == null) {
				logger.error("获取到的武将为空.SelectType[" + selectType + "]");
				continue;
			}

			IBuffHandle handle = BuffHandleFactory.getBuffHandler(effect.getFunId());
			Effect effectA = EffectConfig.getEffect(effect.getEffectId());
			// 目标选择,自己
			effectA.setSelectType(TargetSelectType.SELF);
			Buff buffA = BuffHelper.Effect2Buff(effectA);

			double paramA = effect.getParam("a", 0d);
			double paramB = effect.getParam("b", 0d);

			double targetMaxLife = target.getTotalAttribute(context, AttrConstant.MAX_LIFE);
			double heroPhysicsAttack = hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK);

			double value = heroPhysicsAttack * paramA + targetMaxLife * paramB;

			value = value * (90 + RandomUtils.nextInt(10)) / 100;

			buffA.setValue((long) value);
			buffA.setRound(-2);
			buffA.getEffect().setShowText(1);
			buffA.getEffect().setEffectUid(buff.getEffect().getEffectUid());
			handle.handle(target, buffA, context);
		}
	}

	/**
	 * 给自己加血的buff
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	private void handlerForSelf(Hero hero, Buff buff, Context context) {
		Effect effect = buff.getEffect();

		double paramA = effect.getParam("a", 0d);
		double paramB = effect.getParam("b", 0d);
		double paramTL = effect.getParam("tl", 0d);

		double value = buff.getValue();

		if (value == 0) {
			if (paramTL > 0) {
				// 如果是有tl参数的，则读取上下文中的上一次攻击造成的伤害值 * tl参数
				logger.debug("本次加血的值由本次行动的攻击伤害值运算得到.本次攻击伤害值[" + context.getCurrentOriginalAttackHurt() + "], 换算系数[" + paramTL + "]");
				value = context.getCurrentOriginalAttackHurt() * paramTL;
			} else {
				long maxLife = (long) hero.getTotalAttribute(context, AttrConstant.MAX_LIFE);
				long phsicsAttackTotal = (long) hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK);
				logger.debug("本次加血的值由武将的最大生命值和策略攻击运算得到.最大生命值[" + maxLife + "], 换算系数[" + paramA + "]");
				value = phsicsAttackTotal * paramA + (double) maxLife * paramB;
			}
		}

		hero.addAttribute(context, AttrConstant.LIFE, value);

		buff.setValue((int) value);
	}
}
