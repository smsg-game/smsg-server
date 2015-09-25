package com.ldsg.battle.handle;

import java.util.List;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.constant.BuffAddType;
import com.ldsg.battle.constant.ParamConstant;
import com.ldsg.battle.constant.TargetSelectType;
import com.ldsg.battle.engine.AbstractAction;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.helper.TargetSelectHelper;
import com.ldsg.battle.role.Hero;

/**
 * 修改属性值的基本处理器
 * 
 * @author jacky
 * 
 */
abstract class ModifyAttributeHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(AbstractAction.class);

	/**
	 * 创建buff
	 * 
	 * @param effect
	 * @param hero
	 * @param target
	 * @param context
	 * @param critValue
	 * @return
	 */
	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {

		Buff buff = BuffHelper.Effect2Buff(effect);

		String attribute = this.getAttribute();

		double paramA = effect.getParam("a", 1d);
		int paramB = (int) effect.getParam("b", 0);

		// 旧的属性值
		double oldAttributeValue = target.getAttribute(attribute);
		// 属性变动值
		double attributeModifyValue = (oldAttributeValue * (paramA - 1)) + paramB;

		buff.setValue((long) attributeModifyValue);

		return buff;
	}

	/**
	 * 处理buff
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	public void handle(Hero hero, Buff buff, Context context) {

		Effect effect = buff.getEffect();

		int selectType = effect.getSelectType();

		if (TargetSelectType.SELF == selectType) {// 修改自己的属性的buff的效果
			this.modifySelf(hero, buff, context);
		} else {
			this.modifyOther(hero, buff, context);
		}
	}

	public void handleRemove(Hero hero, Buff buff) {

		Effect effect = buff.getEffect();
		double paramA = effect.getParam("a", 1d);

		String attribute = this.getAttribute();

		if (logger.isDebugEnabled()) {
			double oldAttributeValue = hero.getAttribute(attribute);
			if (oldAttributeValue == 0) {
				logger.warn("有更新属性的buff,但属性修改值为0.hero[" + hero.getLogName() + "], buff.uid[" + buff.getEffect().getEffectUid() + "], attribute[" + attribute + "]");
			}
		}

		if (paramA >= 1) {// 这是一个增益buff

			if (buff.getEffect().getAddType() == BuffAddType.ADD_TEMP) {// 条件buff

				attribute = attribute + "_temp_add";

			} else {

				attribute = attribute + "_add";

			}

		} else {

			if (buff.getEffect().getAddType() == BuffAddType.ADD_TEMP) {// 条件buff

				attribute = attribute + "_temp_reduce";

			} else {

				attribute = attribute + "_reduce";

			}

		}

		logger.debug("清除属性变更值hero[" + hero.getLogName() + "]buff.uid[" + buff.getEffect().getEffectUid() + "], buff[" + buff.getEffect().getRemark() + "], attribute[" + attribute + "]");

		hero.setAttribute(attribute, 0);
	}

	public void afterModify(Context context, Hero hero, double value) {
		// default do noting;
	}

	public abstract String getAttribute();

	/**
	 * 处理改他人属性的buff
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	private void modifyOther(Hero hero, Buff buff, Context context) {

		Effect effect = buff.getEffect();

		int selectType = effect.getSelectType();

		int paramD = (int) effect.getParam(ParamConstant.NEXT_BUFF_ROUND_KEY, 2d);
		int paramE = (int) effect.getParam("e", 1d);

		List<Hero> heroList = TargetSelectHelper.selectTarget(selectType, context, hero);

		for (Hero target : heroList) {
			Effect effectA = EffectConfig.getEffect(effect.getEffectId());
			IBuffHandle handle = BuffHandleFactory.getBuffHandler(effect.getFunId());
			// 目标选择,自己
			effectA.setSelectType(TargetSelectType.SELF);
			effectA.setRemoveAble(paramE);
			effectA.setShowIcons(effect.getShowIcons());
			effectA.setShowText(effect.getShowText());
			effectA.setEffectUid(effect.getEffectUid());
			effectA.setParams(effect.getParams());
			Buff buffA = BuffHelper.Effect2Buff(effectA);
			buffA.setRound(paramD);
			handle.handle(target, buffA, context);

			// if (buffA.getRound() > 0) {
			// target.addBuff(context, buffA);
			// }
		}
	}

	/**
	 * 处理改自己属性的buff
	 * 
	 * @param hero
	 * @param buff
	 * @param context
	 */
	private void modifySelf(Hero hero, Buff buff, Context context) {

		String attribute = this.getAttribute();

		Effect effect = buff.getEffect();

		double paramA = effect.getParam("a", 1d);
		int paramB = (int) effect.getParam("b", 0);

		String modifyAttribute;
		String tempModifyAttribute;

		if (paramA >= 1) {// 加成

			modifyAttribute = attribute + "_add";
			tempModifyAttribute = attribute + "_temp_add";
			logger.debug("处理加成attribute[" + attribute + "]");

		} else {// 减成

			modifyAttribute = attribute + "_reduce";
			tempModifyAttribute = attribute + "_temp_reduce";
			logger.debug("处理减成attribute[" + attribute + "]");
		}

		// 旧的属性值
		double oldAttributeValue = hero.getAttribute(attribute);
		// 属性变动值
		double attributeModifyValue = (oldAttributeValue * (paramA - 1)) + paramB;

		// 新的属性值
		double newAttributeValue = oldAttributeValue + attributeModifyValue;

		// if (buff.getRound() == Buff.ROUND_IMMEDIATELY)
		if (buff.getEffect().getAddType() == BuffAddType.ADD_BASE) {// 一次性的buff则改基本值

			hero.setAttribute(attribute, newAttributeValue);
			logger.debug("直接改基础值");

		} else if (buff.getEffect().getAddType() == BuffAddType.ADD_ONE) {// 有回合有效性的buff则改调整值

			hero.setAttribute(modifyAttribute, attributeModifyValue);
			logger.debug("更改修正值");

		} else if (buff.getEffect().getAddType() == BuffAddType.ADD_TEMP) {

			hero.setAttribute(tempModifyAttribute, attributeModifyValue);
			logger.debug("更改临时值");

		}

		logger.debug("处理属性值更改buff.attribute[" + attribute + "], modifyAttribute[" + modifyAttribute + "], oldAttributeValue[" + oldAttributeValue + "], attributeModifyValue[" + attributeModifyValue + "]");

		buff.setValue((long) attributeModifyValue);

		this.afterModify(context, hero, attributeModifyValue);
	}

	@Override
	public double getBuffValue(Context context, Buff buff, String attribute) {
		return buff.getValue();
	}

}
