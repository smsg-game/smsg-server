package com.ldsg.battle.handle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.event.AfterHeroDeadEvent;
import com.ldsg.battle.event.BeforeHeroDeadEvent;
import com.ldsg.battle.event.BeforeHurtCalculateEvent;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.helper.SkillHelper;
import com.ldsg.battle.role.Hero;

public class ReduceLifeHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ReduceLifeHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		// 伤害值
		int value = 0;

		// 物攻的计算参数
		double paramA = effect.getParam("a", 0d);
		// 物防的计算参数
		double paramB = effect.getParam("b", 0d);

		// 固定掉血参数
		double paramC = effect.getParam("c", 0d);

		// 按最大生命掉血
		double paramD = effect.getParam("d", 0d);

		logger.debug("伤害效果.进攻方武将[" + hero.getLogName() + "], 防守方武将[" + target.getLogName() + "], 进攻方物攻[" + hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK) + "], 防守方物防["
				+ target.getTotalAttribute(context, AttrConstant.PHYSICS_DEFENSE) + "], paramA[" + paramA + "], paramsB[" + paramB + "], paramC[" + paramC + "], paramD[" + paramD + "]");

		if (paramC > 0) {// c参数为固定掉血

			value = (int) paramC;

		} else if (paramD > 0) {
			value = (int) (target.getTotalAttribute(context, AttrConstant.MAX_LIFE) * paramD);
		} else {
			float ratio = SkillHelper.isIgnoreDefense(context, hero, target);
			if (ratio == 0) {
				ratio = 1;
			} else {
				logger.debug("无视对方的防守.Hero[" + hero.getLogName() + "] Target[" + target.getLogName() + "], Ratio[" + ratio + "]");
			}

			float minValue = 1;

			if (paramA > 0) {
				// 攻击方物理伤害
				float heroPhyAttack = (float) (hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK));

				minValue = heroPhyAttack / 10;

				// 防守方物理防御
				float targetPhyDefense = (float) (target.getTotalAttribute(context, AttrConstant.PHYSICS_DEFENSE)) * ratio;

				value += (int) ((heroPhyAttack - targetPhyDefense) * paramA);
			}

			if (paramB > 0) {
				// 攻击方策略伤害
				float heroStrategyAttack = (float) (hero.getTotalAttribute(context, AttrConstant.STRATEGY_ATTACK));

				value += (int) (heroStrategyAttack * heroStrategyAttack * paramB);
			}

			if (critValue == BattleConstant.ATTACK_RESULT_CRIT) {// 爆击
				value = (int) ((float) value * 1.5f);
			} else if (critValue == BattleConstant.ATTACK_RESULT_PARRY) {// 格挡
				value = (int) ((float) value * 0.5f);
			}

			// 士气影响
			if (hero.getTotalAttribute(context, AttrConstant.MORALE) > BattleConstant.SKILL_MIN_MORALE) {
				value = (int) (value * (1 + (hero.getTotalAttribute(context, AttrConstant.MORALE) - BattleConstant.SKILL_MIN_MORALE) * 0.005));
			}

			if (value < minValue) {
				value = (int) minValue;
			}

			if (value < 1) {
				value = 1;
			}
		}

		// TODO
		List<Buff> buffList = new ArrayList<Buff>();
		for (Buff buffB : hero.getBuffList()) {

			double pl = buffB.getEffect().getParam("pl", 0d);
			if (pl == 1f) {
				logger.debug("本次buff生效需要目标物攻比自己低");
				long seflPhysicsAttack = (long) hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK);
				long targetPhysicsAttack = (long) target.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK);
				logger.debug("目标武将的物攻[" + targetPhysicsAttack + "]不低于当前武将的物攻[" + seflPhysicsAttack + "]");
				if (seflPhysicsAttack <= targetPhysicsAttack) {
					logger.debug("本次buff不生效,目标武将的物攻[" + targetPhysicsAttack + "]不低于当前武将的物攻[" + seflPhysicsAttack + "]");
					continue;
				}
			}

			buffList.add(buffB);
		}

		// 对方属性对伤害的加成
		target.setAttack(false);
		double addRatioTarget = BuffHelper.getBuffAddRatio(context, target, buffList, AttrConstant.HURT);

		// 自己属性对伤害的加成
		hero.setAttack(true);
		double addRatioSelf = BuffHelper.getBuffAddRatio(context, hero, buffList, AttrConstant.HURT);

		double tootalAddRatio = addRatioTarget + addRatioSelf;
		int beforeValue = value;
		value = (int) (value * (1 + tootalAddRatio));

		logger.debug("被攻击方属性对伤害的加成.addRatioTarget[" + addRatioTarget + "],攻击方属性对伤害的加成.addRatioSelf[" + addRatioSelf + "], 总的加成.[" + tootalAddRatio + "]");
		logger.debug("加成前伤害值.before hurt[" + beforeValue + "], 加成后伤害[" + value + "]");

		value = (int) (value * (float) 100 / (95 + RandomUtils.nextInt(10)));

		if (value < BattleConstant.MIN_LIFE_DES) {
			value = BattleConstant.MIN_LIFE_DES;
		}

		// 将当前的攻击伤害写到上下文
		context.setCurrentAttackHurt(value);

		// 将当前攻击伤害写到上下文(没分担，免伤之前的伤害值)
		if (!context.isCurrentIsCounter()) {
			context.setCurrentOriginalAttackHurt(value);
		}

		if (paramC == 0 && paramD == 0)// 只有不是固定掉血的才可以分担伤害和免伤
		{

			// 触发攻击者和被攻击者的伤害结算前事件
			List<Hero> heroList = new ArrayList<Hero>();
			heroList.add(hero);
			heroList.add(target);
			IEvent et = new BeforeHurtCalculateEvent(context, heroList);
			context.dispatchEvent(et);
		}

		buff.setValue(context.getCurrentAttackHurt());

		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {

		String position = hero.getPosition();
		long oldLife = (long) hero.getTotalAttribute(context, AttrConstant.LIFE);
		long value = buff.getValue();
		long newLife = oldLife - value;
		newLife = newLife < 0 ? 0 : newLife;

		if (oldLife <= 0) {
			return;
		}

		logger.debug("生命减少.武将名称[" + hero.getLogName() + "], 武将站位[" + position + "], 减少前生命[" + oldLife + "], 生命减少值[" + value + "], 减少后生命[" + newLife + "]");

		// 死亡前事件发生前就设定？有可能会有问题,再考虑
		hero.setAttribute(AttrConstant.LIFE, newLife);

		// 小于0的话,触发死亡前事件
		if (newLife <= 0 && oldLife > 0) {
			hero.setPreDead(true);
			logger.debug("武将血量为负,广播武将死亡前事件.name[" + hero.getLogName() + "]");
			IEvent et = new BeforeHeroDeadEvent(context, hero);
			// 事件广播
			context.dispatchEvent(et);
		}

		// 如果没有回血,就真的死了
		if (hero.getTotalAttribute(context, AttrConstant.LIFE) <= 0) {
			logger.debug("武将阵亡.name[" + hero.getLogName() + "]");

			hero.setDead(true);

			if (hero.isAttackHero()) {
				context.getAttackHeroDict().remove(hero.getPosition());
			} else {
				context.getDefenseHeroDict().remove(hero.getPosition());
			}

			// 死亡的武将的出场顺序
			int orderIndex = context.getStartOrderList().indexOf(position);

			// 如果挂掉的是已经出过场的武将,则当前出场索引要-1
			if (orderIndex <= context.getStartOrderIndex() && context.getStartOrderIndex() >= 0) {
				context.setStartOrderIndex(context.getStartOrderIndex() - 1);
			}

			// 从出场列表里删除掉死亡的武将
			context.getStartOrderList().remove(position);

			// 没出过场的

			logger.debug("[死亡]武将阵亡,广播武将阵亡后事件.name[" + hero.getLogName() + "]");
			IEvent et = new AfterHeroDeadEvent(context, hero);
			context.dispatchEvent(et);
		} else if (hero.isPreDead()) {// 如果没死，但死过，则置为1
			buff.setHeroLife(1);
		}
	}

	public void handleRemove(Hero hero, Buff buff) {
		// throw new NotImplementedException();
	}
}
