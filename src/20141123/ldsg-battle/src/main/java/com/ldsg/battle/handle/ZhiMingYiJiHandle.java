package com.ldsg.battle.handle;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.role.Hero;

public class ZhiMingYiJiHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ZhiMingYiJiHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {
		logger.debug("处理致命一击buff.name[" + hero.getLogName() + "]");
	}

	public void handleRemove(Hero hero, Buff buff) {
	}

	@Override
	public double getBuffAddRatio(Context context, Hero hero, Buff buff, String attribute) {

		if (!AttrConstant.HURT.equals(attribute)) {
			return 0;
		}

		if (hero.isAttack()) {
			return 0;
		}

		double paramI = buff.getEffect().getParam("i", 0d);

		double paramJ = buff.getEffect().getParam("j", 0d);

		double maxLife = hero.getTotalAttribute(context, AttrConstant.MAX_LIFE);

		double life = hero.getTotalAttribute(context, AttrConstant.LIFE);

		double reducePencet = (maxLife - life) / maxLife;

		if (reducePencet < paramI) {// 掉血到一定百分比
			return paramJ;
		}

		return 0;
	}

	@Override
	public String getAttribute() {
		return AttrConstant.HURT;
	}

}
