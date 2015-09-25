package com.ldsg.battle.helper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.ASObject;
import com.ldsg.battle.Context;
import com.ldsg.battle.bo.MergeSkill;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.ScienceConstant;
import com.ldsg.battle.exception.BattleRuntimeException;
import com.ldsg.battle.role.Hero;
import com.ldsg.battle.util.Convert;

/**
 * 武将帮助类
 * 
 * @author jacky
 * 
 */
public class BattleHeroHelper {

	private static Logger logger = Logger.getLogger(BattleHeroHelper.class);

	/**
	 * 计算加成值
	 * 
	 * @param hero
	 * @param addRatio
	 */
	public static void calculateAddRation(Hero hero, double addRatio) {

		hero.setAttribute(AttrConstant.CRIT, (hero.getAttribute(AttrConstant.CRIT) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.HIT, (hero.getAttribute(AttrConstant.HIT) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.MAX_LIFE, (hero.getAttribute(AttrConstant.MAX_LIFE) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.LIFE, (hero.getAttribute(AttrConstant.LIFE) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.PARRY, (hero.getAttribute(AttrConstant.PARRY) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.PHYSICS_ATTACK, (hero.getAttribute(AttrConstant.PHYSICS_ATTACK) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.PHYSICS_DEFENSE, (hero.getAttribute(AttrConstant.PHYSICS_DEFENSE) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.STRATEGY_ATTACK, (hero.getAttribute(AttrConstant.STRATEGY_ATTACK) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.STRATEGY_DEFENSE, (hero.getAttribute(AttrConstant.STRATEGY_DEFENSE) * (1 + addRatio)));
		hero.setAttribute(AttrConstant.SPEED, (hero.getAttribute(AttrConstant.SPEED) * (1 + addRatio)));

	}

	/**
	 * 计算科技加成
	 * 
	 * @param hero
	 * @param scienceType
	 * @param addValue
	 */
	public static void calculateScienceAdd(Context context, Hero hero, int scienceType, int addValue) {
		if (scienceType == 0) {
			return;
		}

		switch (scienceType) {
		case ScienceConstant.SCIENCE_TYPE_PHYSICS_ATTACK:
			hero.addAttribute(context, AttrConstant.PHYSICS_ATTACK, addValue);
			break;
		case ScienceConstant.SCIENCE_TYPE_PHYSICS_DEFENSE:
			hero.addAttribute(context, AttrConstant.PHYSICS_DEFENSE, addValue);
			break;
		case ScienceConstant.SCIENCE_TYPE_STRATEGY_ATTACK:
			hero.addAttribute(context, AttrConstant.STRATEGY_ATTACK, addValue);
			break;
		case ScienceConstant.SCIENCE_TYPE_STRATEGY_DEFENSE:
			hero.addAttribute(context, AttrConstant.STRATEGY_DEFENSE, addValue);
			break;
		case ScienceConstant.SCIENCE_TYPE_LIFE:
			hero.addAttribute(context, AttrConstant.MAX_LIFE, addValue);
			hero.addAttribute(context, AttrConstant.LIFE, addValue);
			break;
		case ScienceConstant.SCIENCE_TYPE_SPEED:
			hero.addAttribute(context, AttrConstant.SPEED, addValue);
			break;
		default:
			throw new BattleRuntimeException("未知的科技类型[" + scienceType + "]");
		}
	}

	public static Hero getHeroByIndex(Map<String, Hero> attackHeroDict, Map<String, Hero> defenseHeroDict, String index) {
		if (index == null) {
			logger.debug("获取武将出错,空的站位信息值!");
			throw new BattleRuntimeException("获取武将出错,空的站位信息值!");
		}

		if (index.startsWith(BattleConstant.ATTACK_PREFIX)) {
			return attackHeroDict.get(index);
		} else if (index.startsWith(BattleConstant.DEFENSE_PREFIX)) {
			return defenseHeroDict.get(index);
		} else {
			logger.warn("获取武将出错,非法的站位信息值[" + index + "]");
			throw new BattleRuntimeException("获取武将出错,非法的站位信息值[" + index + "]");
		}
	}

	/**
	 * 将武将字符debug用的字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String getHeroDebugInfo(ASObject obj) {

		StringBuilder sb = new StringBuilder();

		sb.append(MessageFormat.format("名称[{0}],", obj.get(BattleConstant.HERO_NAME_KEY)));
		sb.append(MessageFormat.format("ID[{0}],", obj.get(BattleConstant.HERO_ID_KEY)));
		sb.append(MessageFormat.format("等级[{0}],", obj.get(BattleConstant.HERO_LEVEL_KEY)));
		sb.append(MessageFormat.format("等级[{0}],", obj.get(BattleConstant.HERO_CAREER_KEY)));
		sb.append(MessageFormat.format("速度[{0}],", obj.get(BattleConstant.HERO_SPEED_KEY)));
		sb.append(MessageFormat.format("命中[{0}],", obj.get(BattleConstant.HERO_HIT_KEY)));
		sb.append(MessageFormat.format("闪避[{0}],", obj.get(BattleConstant.HERO_DODGE_KEY)));
		sb.append(MessageFormat.format("爆击[{0}],", obj.get(BattleConstant.HERO_CRIT_KEY)));
		sb.append(MessageFormat.format("格挡[{0}],", obj.get(BattleConstant.HERO_PARRY_KEY)));
		sb.append(MessageFormat.format("位置[{0}],", obj.get(BattleConstant.HERO_INDEX_KEY)));
		sb.append(MessageFormat.format("生命[{0}],", obj.get(BattleConstant.HERO_LIFE_KEY)));
		sb.append(MessageFormat.format("物理攻击[{0}],", obj.get(BattleConstant.HERO_PHYSICS_ATTACK_KEY)));
		sb.append(MessageFormat.format("物理防守[{0}],", obj.get(BattleConstant.HERO_PHYSICS_DEFENSE_KEY)));
		sb.append(MessageFormat.format("策略攻击[{0}],", obj.get(BattleConstant.HERO_STRATEGY_ATTACK_KEY)));
		sb.append(MessageFormat.format("策略防守[{0}],", obj.get(BattleConstant.HERO_STRATEGY_DEFENSE_KEY)));
		sb.append(MessageFormat.format("主动技能[{0}],", obj.get(BattleConstant.HERO_PLAN_KEY)));
		sb.append(MessageFormat.format("被动技能1[{0}],", obj.get(BattleConstant.HERO_SKILL_1_KEY)));
		sb.append(MessageFormat.format("被动技能2[{0}],", obj.get(BattleConstant.HERO_SKILL_2_KEY)));
		sb.append(MessageFormat.format("被动技能3[{0}],", obj.get(BattleConstant.HERO_SKILL_3_KEY)));
		sb.append(MessageFormat.format("被动技能4[{0}],", obj.get(BattleConstant.HERO_SKILL_4_KEY)));
		sb.append(MessageFormat.format("头像ID[{0}],", obj.get(BattleConstant.HERO_PORTRAIT_KEY)));
		sb.append(MessageFormat.format("兵符ID[{0}]", obj.get(BattleConstant.HERO_COMMAND_KEY)));

		return sb.toString();
	}

	/**
	 * 对武将出场进行排序,速度->交替->位置
	 * 
	 * @param heroList
	 * @param preOrderPrefix
	 * @return
	 */
	public static List<String> getHeroStartOrder(List<Hero> heroList, String preOrderPrefix) {

		for (int i = 0; i < heroList.size(); i++) {
			for (int j = i + 1; j < heroList.size(); j++) {
				Hero heroA = heroList.get(i);
				Hero heroB = heroList.get(j);
				if (heroStartOrderCompare(heroA, heroB, preOrderPrefix) < 0) {
					Hero hero = heroB;
					heroList.set(j, heroA);
					heroList.set(i, hero);
				}
			}

			// 标记上一个武将是哪一方的
			preOrderPrefix = heroList.get(i).getPosition().substring(0, 1);

		}

		List<String> orderList = new ArrayList<String>();
		for (Hero hero : heroList) {
			orderList.add(hero.getPosition());
		}

		return orderList;
	}

	/**
	 * 判断两个武将是否同一方的
	 * 
	 * @param heroA
	 * @param heroB
	 * @return
	 */
	public static boolean isSameTeamHero(Hero heroA, Hero heroB) {
		String prefixA = heroA.getPosition().substring(0, 1);
		String prefixB = heroB.getPosition().substring(0, 1);
		return StringUtils.equalsIgnoreCase(prefixA, prefixB);
	}

	/**
	 * 从一个ASObject List 对象转成一个 Hero List对象
	 * 
	 * @param asObjectList
	 * @param indexPrefix
	 * @return
	 */
	public static Map<String, Hero> parseHeroList(List<ASObject> asObjectList, String indexPrefix) {
		Map<String, Hero> battleHeroMap = new HashMap<String, Hero>();
		for (ASObject o : asObjectList) {
			Hero hero = parseHero(o, indexPrefix);
			battleHeroMap.put(hero.getPosition(), hero);
		}
		return battleHeroMap;
	}

	private static int heroStartOrderCompare(Hero heroA, Hero heroB, String preOrderPrefix) {

		// A的阵营
		String partyA = heroA.getPosition().substring(0, 1);
		// A的索引位置
		int positionA = Convert.toInt32(heroA.getPosition().substring(1));

		// B的阵营
		String partyB = heroB.getPosition().substring(0, 1);
		// B的索引位置
		int positionB = Convert.toInt32(heroB.getPosition().substring(1));

		if (StringUtils.equals(partyA, partyB))// 如果是同一阵营,则比较位置
		{
			if (positionA < positionB) {
				return 1;
			} else {
				return -1;
			}
		} else// 如果是不同方,则看一下上一轮出场的是哪一方
		{
			if (StringUtils.equals(partyA, preOrderPrefix)) {
				return -1;
			} else {
				return 1;
			}

		}

	}

	/**
	 * Map 转成 Hero
	 * 
	 * @param asObject
	 * @param indexPrefix
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Hero parseHero(ASObject asObject, String indexPrefix) {

		Hero hero = new Hero();
		// ID
		hero.setHeroId(asObject.getInt(BattleConstant.HERO_ID_KEY));
		// 爆击
		hero.setAttribute(AttrConstant.CRIT, asObject.getInt(BattleConstant.HERO_CRIT_KEY));
		// 职业
		hero.setCareer(asObject.getInt(BattleConstant.HERO_CAREER_KEY));
		// 闪避
		hero.setAttribute(AttrConstant.DODGE, asObject.getInt(BattleConstant.HERO_DODGE_KEY));
		// 命中
		hero.setAttribute(AttrConstant.HIT, asObject.getInt(BattleConstant.HERO_HIT_KEY));

		hero.setPosition(indexPrefix + asObject.getString(BattleConstant.HERO_INDEX_KEY));

		hero.setAttribute(AttrConstant.LIFE, asObject.getInt(BattleConstant.HERO_LIFE_KEY));

		if (asObject.containsKey(BattleConstant.HERO_MAX_LIFE_KEY)) {
			hero.setAttribute(AttrConstant.MAX_LIFE, asObject.getInt(BattleConstant.HERO_MAX_LIFE_KEY));
		}

		if (hero.getAttribute(AttrConstant.MAX_LIFE) == 0) {
			hero.setAttribute(AttrConstant.MAX_LIFE, hero.getAttribute(AttrConstant.LIFE));
		}

		hero.setLevel(asObject.getInt(BattleConstant.HERO_LEVEL_KEY));
		hero.setAttribute(AttrConstant.MORALE, 0);

		hero.setPortrait(asObject.getInt(BattleConstant.HERO_PORTRAIT_KEY));

		hero.setAttribute(AttrConstant.PARRY, asObject.getInt(BattleConstant.HERO_PARRY_KEY));
		hero.setAttribute(AttrConstant.PHYSICS_ATTACK, asObject.getInt(BattleConstant.HERO_PHYSICS_ATTACK_KEY));
		hero.setAttribute(AttrConstant.PHYSICS_DEFENSE, asObject.getInt(BattleConstant.HERO_PHYSICS_DEFENSE_KEY));
		hero.setAttribute(AttrConstant.STRATEGY_ATTACK, asObject.getInt(BattleConstant.HERO_STRATEGY_ATTACK_KEY));
		hero.setAttribute(AttrConstant.STRATEGY_DEFENSE, asObject.getInt(BattleConstant.HERO_STRATEGY_DEFENSE_KEY));
		hero.setAttribute(AttrConstant.SPEED, asObject.getInt(BattleConstant.HERO_SPEED_KEY));

		hero.setName(asObject.getString(BattleConstant.HERO_NAME_KEY));
		hero.setSoliderToken(asObject.getInt(BattleConstant.HERO_COMMAND_KEY));
		hero.setAttackType(asObject.getInt(BattleConstant.HERO_ATTACK_TYPE_KEY));

		int imageType = asObject.getInt(BattleConstant.HEOR_IMAGE_RATE_KEY);

		if (imageType == 0) {
			imageType = 1;
		}
		hero.setImgType(imageType);

		// 组合技
		List<Map> mList = (List<Map>) asObject.get(BattleConstant.HERO_SKILLS_KEY);
		if (mList != null) {
			for (Map<String, Object> m : mList) {
				int skillId = Convert.toInt32(m.get("skid"));
				List<Integer> pList = (List<Integer>) m.get("plist");

				List<String> posList = new ArrayList<String>();
				for (Integer pos : pList) {
					posList.add(indexPrefix + pos);
				}

				MergeSkill mergeSkill = new MergeSkill();
				mergeSkill.setSkillId(skillId);
				mergeSkill.setPosList(posList);

				// 添加组合技
				hero.addMergeSkill(mergeSkill);
			}

		}

		List<Integer> skList = (List<Integer>) asObject.get(BattleConstant.HERO_SKILL_LIST_KEY);
		if (skList != null) {

			for (Integer skillId : skList) {
				if (skillId > 0) {
					hero.addSkill(skillId);
				}
			}
		}

		int normalSkill = asObject.getInt(BattleConstant.HERO_NORMAL_PLAN_KEY);
		hero.setNormalPlan(normalSkill);

		hero.setPlan(asObject.getInt(BattleConstant.HERO_PLAN_KEY));// 主动技能

		int skill = asObject.getInt(BattleConstant.HERO_SKILL_1_KEY);
		if (skill > 0) {
			hero.addSkill(skill);
		}
		skill = asObject.getInt(BattleConstant.HERO_SKILL_2_KEY);
		if (skill > 0) {
			hero.addSkill(skill);
		}
		skill = asObject.getInt(BattleConstant.HERO_SKILL_3_KEY);
		if (skill > 0) {
			hero.addSkill(skill);
		}
		skill = asObject.getInt(BattleConstant.HERO_SKILL_4_KEY);
		if (skill > 0) {
			hero.addSkill(skill);
		}

		return hero;
	}
}
