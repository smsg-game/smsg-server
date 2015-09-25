package com.ldsg.battle.handle;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.role.Hero;

public class ImmuneDebuffHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ImmuneDebuffHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {
		logger.debug("处理免疫debuff的buff.name[" + hero.getLogName() + "]");
		hero.setImmuneDebuff(true);
	}

	public void handleRemove(Hero hero, Buff buff) {
		logger.debug("清除debuff免疫状态.hero.Name[" + hero.getLogName() + "], effect.Remark[" + buff.getEffect().getRemark() + "]");
		hero.setImmuneDebuff(false);
	}

}
