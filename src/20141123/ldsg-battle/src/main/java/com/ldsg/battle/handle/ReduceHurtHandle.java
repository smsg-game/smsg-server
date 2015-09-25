package com.ldsg.battle.handle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.role.Hero;

public class ReduceHurtHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ReduceHurtHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {

		Hero currentBeAttackHero = context.getCurrentBeAttackHero();
		if (currentBeAttackHero == null || !StringUtils.equalsIgnoreCase(currentBeAttackHero.getPosition(), hero.getPosition())) {
			logger.debug("不是当前被攻击者,不需要进行免伤.当前被攻击武将[" + currentBeAttackHero.getLogName() + "], 当前buff武将[" + hero.getLogName() + "]");
			buff.setReport(false);
			return;
		}

		Effect effect = buff.getEffect();

		// /当前攻击伤害
		long currentAttackHurt = context.getCurrentAttackHurt();

		double paramA = effect.getParam("a", 1d);

		long newAttackHurt = (long) ((double) currentAttackHurt * paramA);

		logger.debug("免伤前伤害[" + currentAttackHurt + ", 免伤后的伤害[" + newAttackHurt + "]");

		context.setCurrentAttackHurt(newAttackHurt);
	}

	public void handleRemove(Hero hero, Buff buff) {
		// throw new NotImplementedException();
	}

}
