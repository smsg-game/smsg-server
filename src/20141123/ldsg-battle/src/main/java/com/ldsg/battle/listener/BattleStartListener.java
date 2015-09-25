package com.ldsg.battle.listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.bo.MergeSkill;
import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.config.SkillConfig;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.BattleConstant;
import com.ldsg.battle.constant.BattleStatus;
import com.ldsg.battle.constant.ReportConstant;
import com.ldsg.battle.event.BattleStartEvent;
import com.ldsg.battle.event.IEvent;
import com.ldsg.battle.handle.BuffHandleFactory;
import com.ldsg.battle.handle.IBuffHandle;
import com.ldsg.battle.helper.ReportHelper;
import com.ldsg.battle.helper.TargetSelectHelper;
import com.ldsg.battle.role.Hero;
import com.ldsg.battle.vo.MergeSkillBO;

/**
 * 战斗开始事件监听器
 * 
 * @author jacky
 * 
 */
public class BattleStartListener extends BaseListener implements IEventListener {

	private static Logger logger = Logger.getLogger(BattleStartListener.class);

	public void listen(IEvent e) {

		if (e.getClass() != BattleStartEvent.class) {
			return;
		}

		logger.debug("[事件]处理战斗开始前事件");

		Context context = e.getContext();
		List<Hero> heroList = e.getHeroList();

		// 所有武将的天赋技能发放
		logger.debug("开始发放所有武将的天赋技能");
		String mergeSkillReport = this.ReleaseAllNaturalSkill(heroList, context);

		// 处理战斗开始时触发的buff
		this.handle(context, heroList, BattleStatus.BATTLE_START);

		// 战报封装

		// context.appendReport(ReportConstant.REPORT_LIST_START_TAG);

		this.appendHeroInfoToReport("ahl", context, context.getAttackHeroDict());
		// context.appendReport(ReportConstant.REPORT_COMMA_SPLIT_TAG);
		this.appendHeroInfoToReport("dhl", context, context.getDefenseHeroDict());
		// context.appendReport(ReportConstant.REPORT_COMMA_SPLIT_TAG);

		context.appendReport("msl", mergeSkillReport);

	}

	/**
	 * 将武将信息添加到战报
	 * 
	 * @param context
	 * @param heroInfo
	 */
	private void appendHeroInfoToReport(String block, Context context, Map<String, Hero> heroInfo) {

		context.appendReport(block, ReportConstant.REPORT_LIST_START_TAG);

		int i = 0;
		for (Entry<String, Hero> entry : heroInfo.entrySet()) {

			String position = entry.getKey();
			Hero hero = entry.getValue();

			Skill skill = hero.getSkill();
			int initiativeSkillId = 0;
			if (skill != null) {
				initiativeSkillId = skill.getSkillId();
			}

			int soliderToken = hero.getSoliderToken();

			int maxLife = (int) hero.getAttribute(AttrConstant.MAX_LIFE);
			int life = (int) hero.getAttribute(AttrConstant.LIFE);
			int morale = (int) hero.getAttribute(AttrConstant.MORALE);

			String s = MessageFormat.format(ReportConstant.REPORT_HERO_FORMAT, hero.getHeroId(), hero.getName(), hero.getLevel(), hero.getImgId(), soliderToken, maxLife, life, morale, initiativeSkillId, position,
					hero.getAttackType(), hero.getImgType());

			context.appendReport(block, s);

			if (i != heroInfo.size() - 1) {
				context.appendReport(block, ReportConstant.REPORT_COMMA_SPLIT_TAG);
			}
			i++;
		}

		context.appendReport(block, ReportConstant.REPORT_LIST_END_TAG);

	}

	// <summary>
	// 发放所有武将的天赋技能
	// </summary>
	// <param name="heroList"></param>
	private String ReleaseAllNaturalSkill(List<Hero> heroList, Context context) {

		StringBuffer sb = new StringBuffer();

		for (Hero hero : heroList) {

			logger.debug("------------------------------" + hero.getLogName() + "----------------------------------");
			this.releaseNaturalSkill(hero, context);

		}

		List<MergeSkillBO> mergeSkillBOList = new ArrayList<MergeSkillBO>();
		mergeSkillBOList.addAll(context.getAttackMergeSkillSet());
		mergeSkillBOList.addAll(context.getDefensesMergeSkillSet());

		boolean first = true;

		// [
		sb.append(ReportConstant.REPORT_LIST_START_TAG);

		for (MergeSkillBO mergeSkillBO : mergeSkillBOList) {

			if (!first) {
				sb.append(ReportConstant.REPORT_COMMA_SPLIT_TAG);
			} else {
				first = false;
			}

			List<String> posList = new ArrayList<String>();
			posList.addAll(mergeSkillBO.getPosList());

			sb.append(ReportHelper.buildMergeSkillReport(mergeSkillBO.getPos(), mergeSkillBO.getSkillId(), posList, mergeSkillBO.getBuffVOList()));
		}

		// ]
		sb.append(ReportConstant.REPORT_LIST_END_TAG);

		return sb.toString();

	}

	/**
	 * 发放某个武将的天赋技能
	 * 
	 * @param hero
	 * @param context
	 */
	private void releaseNaturalSkill(Hero hero, Context context) {

		context.setCurrentHero(hero);

		List<Skill> skillList = hero.getPassiveSkillList();
		for (Skill skill : skillList) {
			this.releaseOneNaturalSkill(hero, skill, context);
		}

		// 组合技能
		List<MergeSkill> mergeSkillList = hero.getMergeSkillList();
		for (MergeSkill mergeSkill : mergeSkillList) {

			context.cleanMergeSkillBuffVOList();

			Skill skill = SkillConfig.getSkill(mergeSkill.getSkillId());

			if (skill == null) {
				logger.debug("组合技能不存在.hero[" + hero.getLogName() + "], skillId[" + mergeSkill.getSkillId() + "]");
				continue;
			}

			this.releaseOneNaturalSkill(hero, skill, context);

			// sb.append(ReportHelper.buildMergeSkillReport(hero.getPosition(),
			// mergeSkill.getSkillId(), mergeSkill.getPosList(),
			// context.getMergeSkillBuffVOList()));

			Set<MergeSkillBO> set = null;

			if (hero.isAttackHero()) {
				set = context.getAttackMergeSkillSet();
			} else {
				set = context.getDefensesMergeSkillSet();
			}

			int skillId = mergeSkill.getSkillId();

			MergeSkillBO mergeSkillBO = getExistMergeSkill(set, skillId, hero.getPosition(), mergeSkill.getPosList());

			if (mergeSkillBO == null) {
				mergeSkillBO = new MergeSkillBO();
				mergeSkillBO.setSkillId(skillId);
				mergeSkillBO.setPos(hero.getPosition());
			}

			mergeSkillBO.getPosList().addAll(mergeSkill.getPosList());
			mergeSkillBO.getBuffVOList().addAll(context.getMergeSkillBuffVOList());

			set.add(mergeSkillBO);

		}

		context.cleanCurrentHero();

	}

	/**
	 * 
	 * @param set
	 * @param skillId
	 * @param pos
	 * @param posList
	 * @return
	 */
	private MergeSkillBO getExistMergeSkill(Set<MergeSkillBO> set, int skillId, String pos, List<String> posList) {

		for (MergeSkillBO bo : set) {

			if (skillId != bo.getSkillId()) {
				continue;
			}

			Set<String> newSet = new HashSet<String>();
			newSet.add(bo.getPos());
			newSet.addAll(bo.getPosList());

			if (newSet.size() != posList.size() + 1) {
				continue;
			}

			if (!newSet.contains(pos)) {
				continue;
			}

			for (String p : posList) {
				if (!newSet.contains(p)) {
					break;
				}
			}

			return bo;
		}

		return null;
	}

	/**
	 * 发放一个技能
	 * 
	 * @param hero
	 * @param skill
	 * @param context
	 */
	private void releaseOneNaturalSkill(Hero hero, Skill skill, Context context) {

		logger.debug("天赋技能发放.hero[" + hero.getLogName() + "], skill[" + skill.getSkillId() + "], skillName[" + skill.getName() + "]");

		if (skill.getTriggerType() != BattleStatus.BATTLE_START) {
			return;
		}

		// 不同目标选择的效果映射
		Map<Integer, List<Effect>> selectEffectMap = new HashMap<Integer, List<Effect>>();
		List<Effect> allEffectList = skill.getEffectList();
		for (Effect effect : allEffectList) {

			int targetSelect = effect.getTargetSelect() == 0 ? skill.getSelectType() : effect.getTargetSelect();

			List<Effect> list = null;
			if (selectEffectMap.containsKey(targetSelect)) {
				list = selectEffectMap.get(targetSelect);
			} else {
				list = new ArrayList<Effect>();
			}
			list.add(effect);

			selectEffectMap.put(targetSelect, list);
		}

		int selectType = skill.getSelectType();
		List<Hero> targetList = TargetSelectHelper.selectTarget(selectType, context, hero);

		for (Hero target : targetList) {

			List<Effect> effectList = null;
			if (selectEffectMap.containsKey(selectType)) {
				effectList = selectEffectMap.get(selectType);
			} else {
				effectList = new ArrayList<Effect>();
			}

			for (Effect effect : effectList) {
				logger.debug("开始添加buff[" + effect.getEffectId() + "],name[" + effect.getRemark() + "]");

				String funID = effect.getFunId();
				// 获取buff处理器
				IBuffHandle handle = BuffHandleFactory.getBuffHandler(funID);
				Buff buff = handle.create(effect, hero, target, context, BattleConstant.ATTACK_RESULT_NORMAL);
				if (buff == null) {
					logger.debug("本次天赋技能的附带buff不命中skill.Remark[" + skill.getRemark() + "]");
					continue;
				}

				if (buff.getEffect().getImmediately() == 1) {// 立即触发的类型

					handle.handle(target, buff, context);

				} else {

					target.addBuff(context, buff);

				}
			}
		}

		// 目标和技能目标选择不一样的武将
		for (Entry<Integer, List<Effect>> entry : selectEffectMap.entrySet()) {

			int targetSelect = entry.getKey();
			if (targetSelect == selectType) {
				continue;
			}
			List<Hero> otherTargetHeroList = TargetSelectHelper.selectTarget(targetSelect, context, hero);
			for (Hero targetHero : otherTargetHeroList) {

				for (Effect effect : entry.getValue()) {

					String funID = effect.getFunId();

					IBuffHandle handle = BuffHandleFactory.getBuffHandler(funID);
					Buff buff = handle.create(effect, hero, targetHero, context, BattleConstant.ATTACK_RESULT_NORMAL);
					if (buff == null) {
						logger.debug("本次天赋技能的附带buff不命中skill.Remark[" + skill.getRemark() + "]");
						continue;
					}

					if (buff.getEffect().getImmediately() == 1) {// 立即触发的类型
						handle.handle(targetHero, buff, context);
					} else {
						targetHero.addBuff(context, buff);
					}

				}

			}
		}
	}
}
