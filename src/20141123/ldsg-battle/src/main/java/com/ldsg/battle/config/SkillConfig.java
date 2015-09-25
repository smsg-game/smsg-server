package com.ldsg.battle.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.constant.CareerConstant;
import com.ldsg.battle.dao.SkillDao;
import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.factory.BeanFactory;
import com.ldsg.battle.helper.SkillHelper;
import com.ldsg.battle.model.SkillEffectModel;
import com.ldsg.battle.model.SkillModel;
import com.ldsg.battle.util.Convert;

public class SkillConfig {

	private static Logger logger = Logger.getLogger(SkillConfig.class);

	/**
	 * 表示一个空的skill(眩晕时使用)
	 */
	private static Skill emptySkill = null;

	/**
	 * 后置效果映射
	 */
	private static Map<Integer, Effect> nextEffectMap = null;

	/**
	 * 普通攻击技能ID(物攻)
	 */
	private static int NORMAL_PHYSICS_ATTACK_SKILL_ID = 10000;

	/**
	 * 医者的普通攻击技能
	 */
	private static int NORMAL_PHYSICIAN_ATTACK_SKILL_ID = 10002;

	/**
	 * 神羽普通攻击技能
	 */
	private static int NORMAL_JOKYBOBO_ATTACK_SKILL_ID = 10003;

	/**
	 * 策士的普通攻击技能
	 */
	private static int NORMAL_TACTICIAN_ATTACK_SKILL_ID = 10004;

	private static Map<Integer, Skill> skillMap = null;

	public static Skill getEmptySkill() {
		if (emptySkill == null) {
			synchronized (SkillConfig.class) {
				emptySkill = new Skill();
				emptySkill.setSkillId(0);
			}
		}
		return emptySkill;
	}

	/**
	 * 获取反击的技能
	 * 
	 * @return
	 */
	public static Skill getCounterSkill() {
		return getSkill(NORMAL_PHYSICS_ATTACK_SKILL_ID);
	}

	/**
	 * 获取普通攻击技能(谁都有的)
	 * 
	 * @param career
	 * @return
	 */
	public static Skill getNormalAttack(int career) {

		int skillID;

		if (career == CareerConstant.CAREER_CATEGORY_STRATEGY_ATTACK) {
			skillID = NORMAL_PHYSICIAN_ATTACK_SKILL_ID;
		} else if (career == CareerConstant.CAREER_CATEGORY_JOKYBOBO_ATTACK) {// 神羽
			skillID = NORMAL_JOKYBOBO_ATTACK_SKILL_ID;
		} else if (career == CareerConstant.CAREER_CATEGORY_TACTICIAN_ATTACK) {// 策士
			skillID = NORMAL_TACTICIAN_ATTACK_SKILL_ID;
		} else {
			skillID = NORMAL_PHYSICS_ATTACK_SKILL_ID;
		}

		Skill skill = getSkill(skillID);
		if (skill == null) {
			logger.error("获取不到普通攻击技能ID");
			throw new BattleRuntimeException("获取不到普通攻击技能数据.ID[" + skillID + "]");
		}

		return skill;
	}

	/**
	 * 根据技能ID获取技能
	 * 
	 * @param id
	 * @return
	 */
	public static Skill getSkill(int id) {

		init();

		if (skillMap.containsKey(id)) {
			return skillMap.get(id);
		} else {
			throw new BattleRuntimeException("技能不存在.skillID[" + id + "]");
		}

	}

	/**
	 * 根据技能ID获取技能效果列表
	 * 
	 * @param id
	 * @return
	 */
	public static List<Effect> getSkillEffectList(int id) {
		init();

		Skill skill = getSkill(id);
		if (skill != null) {
			return skill.getEffectList();
		}

		return null;
	}

	/**
	 * 判断技能是不是普通技能
	 * 
	 * @param skill
	 * @return
	 */
	public static boolean isNoramlAttack(Skill skill) {

		if (skill == null) {
			return false;
		}

		int skillId = skill.getSkillId();

		// 大于10000的是普通攻击
		return skillId >= 10000;

	}

	/**
	 * 是否加士气的技能
	 * 
	 * @param skill
	 * @return
	 */
	public static boolean isAddMoraleSkill(Skill skill) {
		if (skill == null) {
			return false;
		}

		return skill.getAddMorale() == 1;
	}

	/**
	 * 随机获取一个主动技能
	 * 
	 * @return
	 */
	public static int randGetSkill() {
		init();
		Object[] keys = skillMap.keySet().toArray();
		while (true) {
			int r = RandomUtils.nextInt(keys.length);
			int skillId = Convert.toInt32(keys[r].toString());
			Skill skill = getSkill(skillId);
			if (skill.getTriggerType() == 3) {
				return skill.getSkillId();
			}
		}
	}

	/**
	 * 重新加载数据
	 */
	public static void reload() {
		synchronized (SkillConfig.class) {
			if (skillMap != null) {
				skillMap.clear();
				skillMap = null;
			}
		}
	}

	/**
	 * 初始化
	 */
	public static void init() {
		synchronized (SkillConfig.class) {
			if (skillMap != null) {
				return;
			}

			logger.debug("开始初始化技能配置信息");
			try {
				initFromDb();
			} catch (Exception ae) {
				ae.printStackTrace();
				logger.error("初始化技能配置数据失败:" + ae.getMessage());
				throw new BattleRuntimeException("初始化技能配置数据失败.", ae);
			}

		}
	}

	public static Effect getNextEffect(Integer effectUid) {
		return nextEffectMap.get(effectUid);
	}

	/**
	 * 从数据库初始化技能信息
	 */
	private static void initFromDb() {

		if (skillMap != null) {
			return;
		}

		nextEffectMap = new HashMap<Integer, Effect>();
		skillMap = new HashMap<Integer, Skill>();

		SkillDao skillDao = BeanFactory.getInstance().getBean(SkillDao.class);

		List<SkillModel> skillList = skillDao.getSkillList();

		List<SkillEffectModel> list = skillDao.getSkillEffectList();

		Map<Integer, Integer> posEffectIdMap = new HashMap<Integer, Integer>();
		for (SkillEffectModel model : list) {
			if (model.getNextEffectId() > 0) {
				posEffectIdMap.put(model.getNextEffectId(), model.getSkillEffectId());
			}
		}

		Map<Integer, List<SkillEffectModel>> skillEffectMap = new HashMap<Integer, List<SkillEffectModel>>();
		for (SkillEffectModel model : list) {

			List<SkillEffectModel> l = null;
			if (skillEffectMap.containsKey(model.getSkillId())) {
				l = skillEffectMap.get(model.getSkillId());
			} else {
				l = new ArrayList<SkillEffectModel>();
			}

			l.add(model);

			skillEffectMap.put(model.getSkillId(), l);
		}

		for (SkillModel o : skillList) {
			Skill skill = SkillHelper.createSkill(o);

			skillMap.put(skill.getSkillId(), skill);

			List<Effect> effectList = new ArrayList<Effect>();

			List<SkillEffectModel> l = null;
			if (!skillEffectMap.containsKey(skill.getSkillId())) {
				logger.warn("技能没有任何效果.skillId[" + skill.getSkillId() + "]");
				l = new ArrayList<SkillEffectModel>();
			} else {
				l = skillEffectMap.get(skill.getSkillId());
			}

			for (SkillEffectModel skillEffectModel : l) {

				int effectUID = skillEffectModel.getSkillEffectId();
				int skillId = skillEffectModel.getSkillId();
				int effectID = skillEffectModel.getEffectId();
				int nextEffectId = skillEffectModel.getNextEffectId();
				int nextSelectType = skillEffectModel.getNextSelectType();
				int round = skillEffectModel.getRound();
				int showText = skillEffectModel.getShowText();
				int showIcons = skillEffectModel.getShowIcons();
				int selectType = skillEffectModel.getSelectType();
				int triggerType = skillEffectModel.getTriggerType();
				int immediately = skillEffectModel.getImmediately();
				int removeAble = skillEffectModel.getRemoveAble();
				int addType = skillEffectModel.getAddType();
				int targetSelect = skillEffectModel.getTargetSelect();
				String param = skillEffectModel.getParams();
				int removeType = skillEffectModel.getRemoveType();

				Effect effect = EffectConfig.getEffect(effectID);
				if (effect == null) {
					logger.warn("技能对应的效果不存在.EffectID[" + effectID + "]");
					continue;
				}

				effect.setEffectUid(effectUID);
				effect.setRound(round);
				effect.setSelectType(selectType);
				effect.setTriggerType(triggerType);
				effect.setShowText(showText);
				effect.setShowIcons(showIcons);
				effect.setImmediately(immediately);
				effect.setRemoveAble(removeAble);
				effect.setSkillId(skillId);
				effect.setNextEffectId(nextEffectId);
				effect.setNextSelectType(nextSelectType);
				effect.setAddType(addType);
				effect.setTargetSelect(targetSelect);
				effect.setRemoveType(removeType);

				Map<String, Double> paramDict = new HashMap<String, Double>();

				if (!StringUtils.isEmpty(param)) {
					String[] paramList = param.split(",");

					for (String p : paramList) {
						String[] datas = p.split(":");
						if (datas.length != 2) {
							throw new BattleRuntimeException("效果参数配置错误[" + param + "]");
						}
						paramDict.put(datas[0], Convert.toDouble(datas[1]));
					}
				}

				if (logger.isDebugEnabled()) {
					if (effect.getRemoveAble() == 1 && effect.getRound() <= 0) {
						throw new BattleRuntimeException("配置数据存在不合理的数值,可以移除的buff必须持续回合数大于0.effectUID[" + effectUID + "]");
					}
				}
				effect.setParams(paramDict);

				if (posEffectIdMap.containsKey(effect.getEffectUid())) {
					nextEffectMap.put(posEffectIdMap.get(effect.getEffectUid()), effect);
				} else {
					effectList.add(effect);
				}

				skill.setEffectList(effectList);

				skillMap.put(skill.getSkillId(), skill);
			}
		}
	}

	/**
	 * 是否加血的buff
	 * 
	 * @return
	 */
	public static boolean isAddLifeSkill(Skill skill) {

		List<Effect> effectList = skill.getEffectList();
		if (effectList.size() > 0) {
			int firstEffectId = effectList.get(0).getEffectId();
			return firstEffectId == EffectConfig.ADD_LIFE_EFFECT_ID_1 || firstEffectId == EffectConfig.ADD_LIFE_EFFECT_ID_2;
		}

		return false;
	}
}
