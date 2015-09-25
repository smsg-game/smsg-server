package com.ldsg.battle.handle;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.constant.TargetSelectType;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.helper.TargetSelectHelper;
import com.ldsg.battle.role.Hero;

public class SkipActionHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(SkipActionHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {
		logger.debug("处理眩晕buff.name[" + hero.getLogName() + "]");

		Effect effect = buff.getEffect();

		int selectType = effect.getSelectType();

		if (selectType == TargetSelectType.SELF) {
			this.handlerForSelf(hero, buff, context);
		} else {
			this.HandlerForOhter(hero, buff, context);
		}
	}

	public void handleRemove(Hero hero, Buff buff) {
		logger.debug("清除眩晕状态.hero.Name[" + hero.getLogName() + "], effect.Remark[" + buff.getEffect().getRemark() + "]");
		hero.setDizzy(false);
	}

	// / <summary>
	// / 自己处理的buff
	// / </summary>
	// / <param name="hero"></param>
	// / <param name="buff"></param>
	// / <param name="context"></param>
	public void handlerForSelf(Hero hero, Buff buff, Context context) {
		hero.setDizzy(true);
	}

	// / <summary>
	// / 给它人加buff的
	// / </summary>
	// / <param name="hero"></param>
	// / <param name="buff"></param>
	// / <param name="context"></param>
	private void HandlerForOhter(Hero hero, Buff buff, Context context) {

		logger.debug("处理眩晕别人的buff.name[" + hero.getLogName() + "]");

		Effect effect = buff.getEffect();
		int selectType = effect.getSelectType();

		double round = effect.getParam("round", 2d);
		double triggerType = effect.getParam("next_trigger", 2d);

		List<Hero> heroList = TargetSelectHelper.selectTarget(selectType, context, hero);

		for (Hero target : heroList) {

			Effect effectA = EffectConfig.getEffect(effect.getEffectId());
			// /目标选择,自己
			effectA.setSelectType(TargetSelectType.SELF);
			Buff buffA = BuffHelper.Effect2Buff(effectA);
			buffA.setRound((int) round);
			effectA.setTriggerType((int) triggerType);
			effectA.setShowIcons(effect.getShowIcons());
			effectA.setShowText(effect.getShowText());
			effectA.setEffectUid(effect.getEffectUid());
			target.addBuff(context, buffA);
		}
	}
}
