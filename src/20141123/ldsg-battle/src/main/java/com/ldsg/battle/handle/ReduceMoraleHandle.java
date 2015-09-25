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

public class ReduceMoraleHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ReduceMoraleHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {

		Buff buff = BuffHelper.Effect2Buff(effect);

		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {
		Effect effect = buff.getEffect();

		int selectType = effect.getSelectType();

		if (selectType == TargetSelectType.SELF) {
			this.handlerForSelf(hero, buff, context);
		} else {
			this.HandlerForOhter(hero, buff, context);
		}
	}

	public void handleRemove(Hero hero, Buff buff) {
		// throw new NotImplementedException();
		// do nothing
	}

	// / <summary>
	// / 自己处理的buff
	// / </summary>
	// / <param name="hero"></param>
	// / <param name="buff"></param>
	// / <param name="context"></param>
	public void handlerForSelf(Hero hero, Buff buff, Context context) {
		Effect effect = buff.getEffect();

		int moraleReduce = (int) buff.getValue();
		if (moraleReduce <= 0) {
			double paramA = effect.getParam("a", 25d);
			moraleReduce = (int) paramA;
		}

		int oldMorale = (int) hero.getAttribute(AttrConstant.MORALE);

		if (oldMorale < moraleReduce) {
			moraleReduce = oldMorale;
		}

		hero.reduceAttribute(AttrConstant.MORALE, moraleReduce);

		logger.debug("处理减少士气值buff.name[" + hero.getLogName() + "], morale[" + oldMorale + "], moraleReduce[" + moraleReduce + "], newMorale[" + hero.getAttribute(AttrConstant.MORALE) + "]");

		if (moraleReduce == 0) {
			buff.setZeroVal(true);
		}

		buff.setValue(moraleReduce);
	}

	// / <summary>
	// / 给它人加buff的
	// / </summary>
	// / <param name="hero"></param>
	// / <param name="buff"></param>
	// / <param name="context"></param>
	private void HandlerForOhter(Hero hero, Buff buff, Context context) {

		logger.debug("处理减少其它人士气的buff.name[" + hero.getLogName() + "]");

		Effect effect = buff.getEffect();
		int selectType = effect.getSelectType();

		double paramA = effect.getParam("a", 25d);

		List<Hero> heroList = TargetSelectHelper.selectTarget(selectType, context, hero);

		for (Hero target : heroList) {
			IBuffHandle handler = BuffHandleFactory.getBuffHandler(effect.getFunId());
			Effect effectA = EffectConfig.getEffect(effect.getEffectId());
			// /目标选择,自己
			effectA.setSelectType(TargetSelectType.SELF);
			Buff buffA = BuffHelper.Effect2Buff(effectA);
			buffA.setRound(-2);
			effectA.setShowIcons(effect.getShowIcons());
			effectA.setShowText(effect.getShowText());
			effectA.setEffectUid(effect.getEffectUid());
			buffA.setValue((int) paramA);
			handler.handle(target, buffA, context);
		}
	}

}
