package com.ldsg.battle.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.dao.SkillDao;
import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.factory.BeanFactory;
import com.ldsg.battle.helper.BattleHeroHelper;
import com.ldsg.battle.helper.EffectHelper;
import com.ldsg.battle.model.EffectModel;

/**
 * 效果配置类
 * 
 * @author jacky
 * 
 */
public class EffectConfig {

	private static Logger logger = Logger.getLogger(BattleHeroHelper.class);

	/**
	 * 为其它人加物攻时往其它人身上加的buff效果ID
	 */
	public final static int ADD_PHYSICS_ATTACK_EFFECT_ID = 1;

	/**
	 * 为其它人加物攻时往其它人身上加的buff效果UID(关联表ID)
	 */
	public final static int ADD_PHYSICS_ATTACK_EFFECT_UID = 1;

	/**
	 * 为其它人加物防时往其它人身上加的buff效果ID
	 */
	public final static int ADD_PHYSICS_DEFENSE_EFFECT_ID = 2;

	/**
	 * 为其它人加物防时往其它人身上加的buff效果UID(关联表ID)
	 */
	public final static int ADD_PHYSICS_DEFENSE_EFFECT_UID = 2;

	/**
	 * 掉血的效果ID
	 */
	public final static int REDUCE_LIFE_EFFECT_ID = 10000;

	/**
	 * 加血的效果ID1
	 */
	public final static int ADD_LIFE_EFFECT_ID_1 = 10002;

	/**
	 * 加血的效果ID2
	 */
	public final static int ADD_LIFE_EFFECT_ID_2 = 4;

	/**
	 * 效果配置映射
	 */
	private static Map<Integer, Effect> effectMap = null;

	/**
	 * 根据效果ID获取效果
	 * 
	 * @param effectID
	 * @return
	 */
	public static Effect getEffect(int effectID) {

		init();

		if (effectMap.containsKey(effectID)) {
			Effect effect = effectMap.get(effectID);
			// 返回一个浅拷贝对象
			return (Effect) effect.clone();
		}

		return null;
	}

	/**
	 * 获取普通攻击掉血的效果
	 * 
	 * @return
	 */
	public static Effect getReduceLifeEffect() {

		Effect effect = getEffect(EffectConfig.REDUCE_LIFE_EFFECT_ID);
		effect.setShowText(1); // 飘字
		effect.setEffectUid(149); // 唯一ID
		return effect;
	}

	/**
	 * 重新加载数据
	 */
	public static void reload() {

		synchronized (EffectConfig.class) {
			if (effectMap != null) {
				effectMap.clear();
				effectMap = null;
			}
		}
	}

	/**
	 * 初始化技能效果配置信息
	 */
	public static void init() {

		if (effectMap != null) {
			return;
		}

		synchronized (EffectConfig.class) {
			logger.debug("开始初始化技能效果配置信息");
			try {
				initFromDb();
			} catch (Exception ae) {
				logger.error("初始化技能效果配置数据失败:" + ae.getMessage());
				throw new BattleRuntimeException("初始化技能效果配置数据失败.", ae);
			}

		}
	}

	/**
	 * 从数据库初始化技能配置信息
	 */
	private static void initFromDb() {
		if (effectMap != null && !effectMap.isEmpty()) {
			return;
		}

		SkillDao skillDao = BeanFactory.getInstance().getBean(SkillDao.class);

		effectMap = new HashMap<Integer, Effect>();

		List<EffectModel> list = skillDao.getEffectList();
		for (EffectModel model : list) {

			Effect effect = EffectHelper.createEffect(model);

			if (StringUtils.isEmpty(effect.getFunId())) {
				logger.warn("效果的计算公式ID为空[" + effect.getEffectId() + "]");
			}

			effectMap.put(effect.getEffectId(), effect);
		}

	}
}