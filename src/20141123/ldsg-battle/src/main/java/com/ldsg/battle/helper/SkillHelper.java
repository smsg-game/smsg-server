package com.ldsg.battle.helper;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.config.SkillConfig;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.handle.BuffHandleFactory;
import com.ldsg.battle.model.SkillModel;
import com.ldsg.battle.role.Hero;

/**
 * 技能帮助类
 * 
 * @author jacky
 * 
 */
public class SkillHelper {

	private static Logger logger = Logger.getLogger(SkillHelper.class);

	// <summary>
	// 判断武将发什么技能
	// </summary>
	// <param name="hero"></param>
	// <param name="context"></param>
	// <returns></returns>
	public static Skill selectSkill(Hero hero, Context context) {
		Skill skill = hero.getSkill();

		long morale = (long) hero.getAttribute(AttrConstant.MORALE);

		if (morale < BattleConstant.SKILL_MIN_MORALE) {
			logger.debug("武将士气值不足,使用普通攻击.name[" + hero.getLogName() + "], morale[" + morale + "]");
			skill = SkillConfig.getSkill(hero.getNormalPlan());
			if (skill == null) {
				skill = SkillConfig.getNormalAttack(hero.getCareer());
			}
			return skill;
		}

		if (skill == null) {
			throw new BattleRuntimeException("武将的主动技能为空.name[" + hero.getLogName() + "]");
		}

		return skill;
	}

	public static Skill createSkill(SkillModel model) {

		if (model == null) {
			return null;
		}

		Skill skill = new Skill();

		skill.setSkillId(model.getSkillId());
		skill.setSelectType(model.getSelectType());
		skill.setSecondSelectType(model.getSecondSelectType());
		skill.setTriggerType(model.getTriggerType());
		skill.setRemark(model.getRemark());
		skill.setName(model.getName());
		skill.setCheckValue(model.getCheckValue());
		skill.setParams(model.getParams().split(","));
		skill.setAddMorale(model.getAddMorale());

		return skill;
	}

	/**
	 * 是否无视对方防守
	 * 
	 * @param hero
	 * @param target
	 * @return
	 */
	public static float isIgnoreDefense(Context context, Hero hero, Hero target) {
		float ratio = 0;

		List<Buff> buffList = hero.getBuffList();

		for (Buff buff : buffList) {
			if (buff.getEffect().getFunId().equals(BuffHandleFactory.FUNC_ID_IGNORE_DEFENSE)) {
				Effect effect = buff.getEffect();
				double pl = effect.getParam("pl", 0d);
				if (pl == 1f && hero.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK) < target.getTotalAttribute(context, AttrConstant.PHYSICS_ATTACK)) {
					continue;
				}

				ratio = (float) buff.getEffect().getParam("a", 1d);
				return ratio;
			}
		}

		return ratio;
	}
}