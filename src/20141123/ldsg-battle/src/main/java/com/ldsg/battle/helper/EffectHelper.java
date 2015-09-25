package com.ldsg.battle.helper;

import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.model.EffectModel;

public class EffectHelper {

	public static Effect createEffect(EffectModel model) {

		if (model == null) {
			return null;
		}

		Effect effect = new Effect();

		effect.setEffectId(model.getEffectId());
		effect.setFunId(model.getFunId());
		effect.setName(model.getName());
		effect.setRemark(model.getName());
		effect.setType(model.getType());
		return effect;
	}

	// <summary>
	// 判断是不是一个条件效果(需要根据目标来判断是否触发的buff效果)
	// </summary>
	// <param name="effect"></param>
	// <returns></returns>
	public static boolean isConditionEffect(Effect effect) {
		//物攻条件
		if (effect.getParam("pl", 0) > 0) {
			return true;
		}

		//策攻条件
		if (effect.getParam("sal", 0) > 0) {
			return true;
		}

		//速度条件
		if (effect.getParam("sl", 0) > 0) {
			return true;
		}

		return false;
	}

}
