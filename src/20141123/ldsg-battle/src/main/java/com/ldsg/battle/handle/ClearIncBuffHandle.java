package com.ldsg.battle.handle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.role.Hero;

public class ClearIncBuffHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ClearIncBuffHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);

		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {
		logger.debug("清除增buff.name[" + hero.getLogName() + "]");
		List<Buff> removeList = new ArrayList<Buff>();
		int removeCount = 0;
		for (Buff buffA : hero.getBuffList()) {
			Effect effect = buffA.getEffect();
			if (buff.isIncBuff() && effect.getRemoveAble() == 1) {
				removeList.add(buffA);
				removeCount++;
			}
		}

		for (Buff buffA : removeList) {
			hero.removeBuff(context, buffA);
		}

		logger.debug("清除增益buff完成.name[" + hero.getLogName() + "], removeCount[" + removeCount + "]");
	}

	public void handleRemove(Hero hero, Buff buff) {
		throw new NotImplementedException();
	}

}
