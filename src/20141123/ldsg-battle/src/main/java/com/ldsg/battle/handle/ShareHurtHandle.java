package com.ldsg.battle.handle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.helper.TargetSelectHelper;
import com.ldsg.battle.role.Hero;

public class ShareHurtHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ShareHurtHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {
		Hero currentBeAttackHero = context.getCurrentBeAttackHero();
		if (currentBeAttackHero == null || !StringUtils.equalsIgnoreCase(currentBeAttackHero.getPosition(), hero.getPosition())) {
			logger.debug("不是当前被攻击者,不需要进行伤害分担.当前被攻击武将[" + currentBeAttackHero.getLogName() + "], 当前buff武将[" + hero.getLogName() + "]");
			buff.setReport(false);
			return;
		}

		if (context.isCurrentIsCounter()) {
			logger.debug("反击不需要进行伤害分担");
			buff.setReport(false);
			return;
		}

		Effect effect = buff.getEffect();

		List<Hero> heroList = TargetSelectHelper.selectTarget(effect.getSelectType(), context, hero);

		// /选把当前buff所有者去掉
		List<Hero> list = new ArrayList<Hero>();
		for (Hero battleHero : heroList) {
			if (battleHero.getPosition().equals(hero.getPosition())) {
				continue;
			}
			list.add(battleHero);
		}

		if (list.size() == 0) {
			logger.debug("可以帮你分担伤害的武将个数为0,死翘翘了!");
			buff.setReport(false);
			return;
		}

		// /从上下文中读取当前行动的伤害值
		long currentAttackHurt = context.getCurrentAttackHurt();
		int count = list.size();
		int avgShare = (int) ((double) currentAttackHurt / (count + 1));

		// /获取掉血伤害的效果
		Effect effectA = EffectConfig.getReduceLifeEffect();
		String funID = effectA.getFunId();

		IBuffHandle handler = BuffHandleFactory.getBuffHandler(funID);

		// /每个分担的武将身上加一个立即触发的掉血buff
		for (Hero battleHero : heroList) {
			Buff buffA = BuffHelper.Effect2Buff(effectA);
			buffA.setValue(avgShare);
			buffA.setRound(Buff.ROUND_IMMEDIATELY);
			handler.handle(battleHero, buffA, context);

			currentAttackHurt -= avgShare;
		}

		context.setCurrentAttackHurt(currentAttackHurt);
	}

	public void handleRemove(Hero hero, Buff buff) {
		throw new NotImplementedException();
	}

}
