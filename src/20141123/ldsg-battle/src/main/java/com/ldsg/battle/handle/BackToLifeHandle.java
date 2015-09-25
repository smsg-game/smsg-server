package com.ldsg.battle.handle;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.role.Hero;

public class BackToLifeHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(BackToLifeHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {

		Buff buff = BuffHelper.Effect2Buff(effect);

		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {

		logger.debug("处理恢复生命buff");

		Effect effect = buff.getEffect();

		double paramA = effect.getParam("a", 0.2d);

		long maxLife = (long) hero.getTotalAttribute(context, AttrConstant.MAX_LIFE);
		int value = (int) ((float) maxLife * paramA);
		logger.debug("恢复生命成功.生命恢复到[" + value + "]");
		hero.setAttribute(AttrConstant.LIFE, value);

		buff.setValue(value);
	}

	public void handleRemove(Hero hero, Buff buff) {
		throw new NotImplementedException();
	}

}
