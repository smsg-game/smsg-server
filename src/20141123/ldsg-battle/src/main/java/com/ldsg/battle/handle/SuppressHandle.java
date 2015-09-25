package com.ldsg.battle.handle;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.role.Hero;

/**
 * 压制
 * 
 * @author jacky
 * 
 */
public class SuppressHandle extends BaseHandle {

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {
		// DO NOTING
	}

	public void handleRemove(Hero hero, Buff buff) {

	}

	@Override
	public double getBuffAddRatio(Context context, Hero hero, Buff buff, String attrbute) {

		if (!AttrConstant.PHYSICS_ATTACK.equals(attrbute)) {// 只加物攻
			return 0;
		}

		Effect effect = buff.getEffect();
		int selfTeamHeroCount = 0;
		int targetTeamHeroCount = 0;
		if (hero.isAttackHero()) {
			selfTeamHeroCount = context.getAttackHeroDict().size();
			targetTeamHeroCount = context.getDefenseHeroDict().size();
		} else {
			targetTeamHeroCount = context.getAttackHeroDict().size();
			selfTeamHeroCount = context.getDefenseHeroDict().size();
		}

		double addRatio = effect.getParam("a", 0.1);

		int diff = selfTeamHeroCount - targetTeamHeroCount;
		if (diff > 0) {
			return addRatio * diff;
		}

		return 0;
	}
}
