package com.ldsg.battle.handle;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.TargetSelectType;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.helper.TargetSelectHelper;
import com.ldsg.battle.role.Hero;

public class AddMoraleHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(AddMoraleHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {

		if (target.isCanNotAddMorale()) {
			logger.debug("武将中了不能加士气的buff,不能增加士气[" + target.getLogName() + "]");
			return null;
		}

		Buff buff = BuffHelper.Effect2Buff(effect);

		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {

		Effect effect = buff.getEffect();

		int selectType = effect.getSelectType();

		if (selectType == TargetSelectType.SELF) {
			this.handlerForSelf(hero, buff, context);
		} else {
			this.handlerForOhter(hero, buff, context);
		}
	}

	public void handleRemove(Hero hero, Buff buff) {
		// do nothing
	}

	/**
	 * 给它人加buff的
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	private void handlerForOhter(Hero hero, Buff buff, Context context) {

		logger.debug("处理增其它人士气的buff.name[" + hero.getLogName() + "]");

		Effect effect = buff.getEffect();
		int selectType = effect.getSelectType();

		double paramA = effect.getParam("a", 25d);

		List<Hero> heroList = TargetSelectHelper.selectTarget(selectType, context, hero);

		for (Hero target : heroList) {

			IBuffHandle handle = BuffHandleFactory.getBuffHandler(effect.getFunId());
			Effect effectA = EffectConfig.getEffect(effect.getEffectId());
			// /目标选择,自己
			effectA.setSelectType(TargetSelectType.SELF);
			effectA.setParams(effect.getParams());
			Buff buffA = BuffHelper.Effect2Buff(effectA);
			buffA.setRound(-2);
			buffA.setValue((int) paramA);
			handle.handle(target, buffA, context);
		}
	}

	/**
	 * 给自己加士气的buff
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	private void handlerForSelf(Hero hero, Buff buff, Context context) {

		Effect effect = buff.getEffect();

		double paramA = effect.getParam("a", 25d);

		int moraleAdd = 0;
		if (buff.getPrevValue() > 0 || buff.isZeroVal()) {
			moraleAdd = (int) buff.getPrevValue();
		} else if (buff.getValue() > 0) {
			moraleAdd = (int) buff.getValue();
		} else {
			moraleAdd = (int) paramA;
		}

		long oldMorale = (long) hero.getTotalAttribute(context, AttrConstant.MORALE);

		if (hero.isCanNotAddMorale()) {
			logger.debug("武将中了不能加士气的buff,不能增加士气[" + hero.getLogName() + "]");
		} else {
			hero.addAttribute(context, AttrConstant.MORALE, moraleAdd);
		}
		logger.debug("处理增加士气值buff.name[" + hero.getLogName() + "], morale[" + oldMorale + "], moraleAdd[" + moraleAdd + "], newMorale[" + hero.getTotalAttribute(context, AttrConstant.MORALE) + "]");

		buff.setValue(moraleAdd);
	}

}
