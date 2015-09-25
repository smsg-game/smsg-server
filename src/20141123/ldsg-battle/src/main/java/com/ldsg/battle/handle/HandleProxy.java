package com.ldsg.battle.handle;

import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.role.Hero;
import com.ldsg.battle.vo.BuffVO;

public class HandleProxy extends BaseHandle {

	private static Logger logger = Logger.getLogger(HandleProxy.class);

	/**
	 * 被代理的处理器
	 */
	private IBuffHandle handle;

	public HandleProxy(IBuffHandle handle) {
		this.handle = handle;
	}

	/**
	 * 创建buf
	 */
	@Override
	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {

		// buf的命中率,默认为100
		double addRange = effect.getParam("ar", 100d);
		// addRange = addRange * 100;
		int randValue = RandomUtils.nextInt(100);
		if (addRange != 100) {
			logger.debug("该效果[" + effect.getName() + "]的命中区间为[0-" + addRange + "], 当前随机值为[" + randValue + "]");
		}
		if (randValue <= addRange) {
			Buff buff = this.handle.create(effect, hero, target, context, critValue);
			buff.setSource(hero);
			return buff;
		}

		return null;
	}

	/**
	 * 创建buf，用于拥有后续效果的效果使用
	 */
	@Override
	public Buff create(Effect effect, Buff prevBuff, Hero hero, Hero target, Context context, int critValue) {

		// buf的命中率,默认为100
		double addRange = effect.getParam("ar", 100d);
		// addRange = addRange * 100;
		int randValue = RandomUtils.nextInt(100);
		if (addRange != 100) {
			logger.debug("该效果[" + effect.getName() + "]的命中区间为[0-" + addRange + "], 当前随机值为[" + randValue + "]");
		}
		if (randValue <= addRange) {
			Buff buff = this.handle.create(effect, hero, target, context, critValue);
			if (prevBuff != null) {
				buff.setPrevValue(prevBuff.getValue());
				buff.setZeroVal(prevBuff.isZeroVal());
			}
			return buff;
		}

		return null;
	}

	/**
	 * 获取属性值
	 */
	public double getBuffValue(Context context, Buff buff, String attribute) {

		if (!StringUtils.equalsIgnoreCase(this.handle.getAttribute(), attribute)) {
			return 0;
		}

		return this.handle.getBuffValue(context, buff, attribute);
	}

	@Override
	public double getBuffAddRatio(Context context, Hero hero, Buff buff, String attribute) {

		if (!StringUtils.equalsIgnoreCase(this.handle.getAttribute(), attribute)) {
			return 0;
		}

		return this.handle.getBuffAddRatio(context, hero, buff, attribute);
	}

	/**
	 * 处理buff
	 */
	public void handle(Hero hero, Buff buff, Context context) {

		logger.debug("处理效果.hero.Name[" + hero.getLogName() + "], effect.Remark[" + buff.getEffect().getRemark() + "], effect.SelectType[" + buff.getEffect().getSelectType() + "], effect.TriggerType["
				+ buff.getEffect().getTriggerType() + "]");

		Effect effect = buff.getEffect();

		if (logger.isDebugEnabled()) {
			String param = "";
			for (Entry<String, Double> entry : effect.getParams().entrySet()) {
				param += entry.getKey() + "=" + entry.getValue();
				param += ",";

			}
			logger.debug("当前效果参数列表[" + param + "]");
		}

		// 击杀对手条件判断
		if (!this.killConditionCheck(hero, effect, context)) {
			return;
		}

		// 兵符条件判断
		if (!this.commandConditionCheck(hero, effect, context)) {
			return;
		}

		// 随机因素条件判断
		if (!this.randConditionCheck(hero, effect, context)) {
			return;
		}

		// 物攻比自身低条件判断
		if (!this.physicsAttackConditionCheck(hero, effect, context)) {
			return;
		}

		// 策攻比 自身低条件判断
		if (!this.strategyAttackConditionCheck(hero, effect, context)) {
			return;
		}

		// 速度比自身低条件判断
		if (!this.speedConditionCheck(hero, effect, context)) {
			return;
		}

		this.handle.handle(hero, buff, context);

		if (effect.getShowIcons() > 0 || effect.getShowText() > 0) {
			// 战报所用的数据
			if (buff.isReport()) {
				BuffVO buffVO = new BuffVO();
				buffVO.setPosition(hero.getPosition());
				buffVO.setEffectUID(effect.getEffectUid());
				buffVO.setReduceLife(effect.getEffectId() == EffectConfig.REDUCE_LIFE_EFFECT_ID);

				boolean isAddLife = buff.getEffect().getEffectId() == EffectConfig.ADD_LIFE_EFFECT_ID_1 || buff.getEffect().getEffectId() == EffectConfig.ADD_LIFE_EFFECT_ID_2;

				buffVO.setAddLife(isAddLife);

				buffVO.setRound(buff.getRound());
				buffVO.setValue(buff.getValue());
				if (buff.getHeroLife() == 0) {
					buffVO.setLife((long) hero.getTotalAttribute(context, AttrConstant.LIFE));
				} else {
					buffVO.setLife(buff.getHeroLife());
				}
				buffVO.setTriggerType(effect.getTriggerType());
				if (effect.getType() == Buff.BUFF_TYPE_DEC && buffVO.getValue() > 0) {
					buffVO.setValue(buffVO.getValue() * -1);
				}

				if (context.getStatus() == BattleStatus.BATTLE_START) {
					context.appendMergeSkillBuffVO(buffVO);
				} else if (context.getStatus() == BattleStatus.ROUND_START) {
					context.appendRoundBuffVO(buffVO);
				} else {
					context.appendActionBuffVO(buffVO);
				}
			}

		}
		// 重置为report
		buff.setReport(true);

		logger.debug("effectUID[" + effect.getEffectUid() + "], showIcons[" + effect.getShowIcons() + "], showText[" + effect.getShowText() + "], report[" + buff.isReport() + "]");

	}

	public void handleRemove(Hero hero, Buff buff) {
		this.handle.handleRemove(hero, buff);
	}

	/**
	 * 兵符条件判断
	 * 
	 * @param hero
	 * @param effect
	 * @param context
	 * @return
	 */
	private boolean commandConditionCheck(Hero hero, Effect effect, Context context) {
		// 兵符条件判断
		double command = effect.getParam("cm", 0d);
		// 如果兵符条件>0
		if (command > 0) {
			if (hero.getSoliderToken() != command) {
				logger.debug("本次buff效果不生效.要求兵符为[" + command + "], 用户携带的兵符为[" + hero.getSoliderToken() + "]");
				return false;
			}
		}

		return true;
	}

	private boolean killConditionCheck(Hero hero, Effect effect, Context context) {
		// 击杀条件判断
		double kill = effect.getParam("kill", 0d);
		// 如果击杀条件>0
		if (kill > 0) {
			if (hero.isKillTarget() == false) {
				logger.debug("本次buff效果不生效.要求用户要本次行必须有一次击杀");
				return false;
			}
		}
		return true;
	}

	/**
	 * 物攻条件判断
	 * 
	 * @param hero
	 * @param effect
	 * @param context
	 * @return
	 */
	private boolean physicsAttackConditionCheck(Hero hero, Effect effect, Context context) {
		// 物攻比自己低判断
		double pl = effect.getParam("pl", 0d);
		if (pl == 1f && hero.getTarget() != null) {
			logger.debug("本次buff生效需要目标物攻比自己低");
			Hero target = hero.getTarget();
			long seflPhysicsAttack = (long) hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK);
			long targetPhysicsAttack = (long) target.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK);
			if (seflPhysicsAttack <= targetPhysicsAttack) {
				logger.debug("本次buff不生效,目标武将的物攻[" + targetPhysicsAttack + "]不低于当前武将的物攻[" + seflPhysicsAttack + "]");
				return false;
			}
		}

		return true;
	}

	/**
	 * 随机条件判断
	 * 
	 * @param hero
	 * @param effect
	 * @param context
	 * @return
	 */
	private boolean randConditionCheck(Hero hero, Effect effect, Context context) {
		// 随机因素判断
		double effectRange = effect.getParam("r", 100d);
		if (effectRange != 100) {
			int randValue = RandomUtils.nextInt(100);
			logger.debug("该效果的产生效果区间为[0-" + effectRange + "], 当前随机值为[" + randValue + "]");
			if (randValue > effectRange) {
				logger.debug("本次随机效果不生效果");
				return false;
			}
		}

		return true;
	}

	/**
	 * 速度条件判断
	 * 
	 * @param hero
	 * @param effect
	 * @param context
	 * @return
	 */
	private boolean speedConditionCheck(Hero hero, Effect effect, Context context) {
		// 速度比自己低判断
		double pl = effect.getParam("sl", 0d);
		if (pl == 1f) {
			logger.debug("本次buff生效需要目标策攻比自己低");
			Hero target = hero.getTarget();
			long seflSpeed = (long) hero.getTotalAttribute(context, AttrConstant.SPEED);
			long targetSpeed = (long) target.getTotalAttribute(context, AttrConstant.SPEED);
			if (seflSpeed <= targetSpeed) {
				logger.debug("本次buff不生效,目标武将的速度[" + targetSpeed + "]不低于当前武将的速度[" + seflSpeed + "]");
				return false;
			}
		}

		return true;
	}

	public String getAttribute() {
		return this.handle.getAttribute();
	}

	/**
	 * 策攻条件判断
	 * 
	 * @param hero
	 * @param effect
	 * @param context
	 * @return
	 */
	private boolean strategyAttackConditionCheck(Hero hero, Effect effect, Context context) {

		// 策攻比自己低判断
		double pl = effect.getParam("sal", 0d);
		if (pl == 1f) {
			logger.debug("本次buff生效需要目标策攻比自己低");
			Hero target = hero.getTarget();
			long seflStrategyAttack = (long) hero.getTotalAttribute(context, AttrConstant.STRATEGY_ATTACK);
			long targetStrategyAttack = (long) target.getTotalAttribute(context, AttrConstant.STRATEGY_DEFENSE);
			if (seflStrategyAttack <= targetStrategyAttack) {
				logger.debug("本次buff不生效,目标武将的策攻[" + targetStrategyAttack + "]不低于当前武将的策攻[" + seflStrategyAttack + "]");
				return false;
			}
		}

		return true;
	}
}
