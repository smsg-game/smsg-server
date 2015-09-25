package com.ldsg.battle.helper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Skill;
import com.ldsg.battle.constant.AttrConstant;
import com.ldsg.battle.constant.ReportConstant;
import com.ldsg.battle.role.Hero;
import com.ldsg.battle.vo.AttackVO;
import com.ldsg.battle.vo.BuffVO;

public class ReportHelper {

	/**
	 * 战报里关于攻击的封装
	 * 
	 * @param hero
	 * @param skill
	 * @param attackVOList
	 * @return
	 */
	public static String BuildAttackReport(Context context, Hero hero, Skill skill, List<AttackVO> attackVOList) {

		StringBuilder sb = new StringBuilder();

		sb.append(ReportConstant.REPORT_LIST_START_TAG);
		sb.append(MessageFormat.format("\"{0}\",{1},{2}", hero.getPosition(), String.valueOf((int) hero.getTotalAttribute(context, AttrConstant.LIFE)), String.valueOf(skill.getSkillId())));
		sb.append(ReportConstant.REPORT_LIST_END_TAG);

		sb.append(ReportConstant.REPORT_COMMA_SPLIT_TAG);

		List<String> attackReportList = new ArrayList<String>();
		for (AttackVO vo : attackVOList) {
			String s = MessageFormat.format("[\"{0}\",{1},{2},{3}]", vo.getPosition(), vo.getHit(), vo.getCritValue(), String.valueOf(vo.getCounter()));
			attackReportList.add(s);
		}
		sb.append(ReportConstant.REPORT_LIST_START_TAG);
		sb.append(StringUtils.join(attackReportList, ReportConstant.REPORT_COMMA_SPLIT_TAG));
		sb.append(ReportConstant.REPORT_LIST_END_TAG);

		// ","号
		sb.append(ReportConstant.REPORT_COMMA_SPLIT_TAG);

		return sb.toString();
	}

	/**
	 * 
	 * @param pos
	 * @param skillId
	 * @param posList
	 * @param buffVOList
	 * @return
	 */
	public static String buildMergeSkillReport(String pos, int skillId, List<String> posList, List<BuffVO> buffVOList) {

		StringBuilder sb = new StringBuilder();

		// [
		sb.append(ReportConstant.REPORT_LIST_START_TAG);

		sb.append(MessageFormat.format("[\"{0}\", {1}]", pos, skillId));

		// ,
		sb.append(ReportConstant.REPORT_COMMA_SPLIT_TAG);

		List<String> posStrList = new ArrayList<String>();
		for (String s : posList) {
			posStrList.add("\"" + s + "\"");
		}

		sb.append(MessageFormat.format("[{0}]", StringUtils.join(posStrList, ReportConstant.REPORT_COMMA_SPLIT_TAG)));

		// ,
		sb.append(ReportConstant.REPORT_COMMA_SPLIT_TAG);

		sb.append(BuildBuffVOReport(buffVOList));

		// ]
		sb.append(ReportConstant.REPORT_LIST_END_TAG);

		return sb.toString();
	}

	/**
	 * 战报里关于buff的封装
	 * 
	 * @param buffVOList
	 * @return
	 */
	public static String BuildBuffVOReport(List<BuffVO> buffVOList) {
		StringBuilder sb = new StringBuilder();

		// [
		sb.append(ReportConstant.REPORT_LIST_START_TAG);

		List<String> list = new ArrayList<String>();
		for (BuffVO vo : buffVOList) {
			int effectUID = vo.getEffectUID();
			String position = vo.getPosition();
			// long life = vo.Life;

			String s = MessageFormat.format(ReportConstant.REPORT_BUFF_FORMAT, position, vo.getValue(), effectUID, vo.getRound(), vo.getLife());
			list.add(s);
		}

		sb.append(StringUtils.join(list, ReportConstant.REPORT_COMMA_SPLIT_TAG));

		// ]
		sb.append(ReportConstant.REPORT_LIST_END_TAG);

		return sb.toString();
	}

	/**
	 * 战报里关于武将怒气值的封装
	 * 
	 * @param heroList
	 * @return
	 */
	public static String BuildMoraleReport(Context context, List<Hero> heroList) {
		StringBuilder sb = new StringBuilder();

		List<String> values = new ArrayList<String>();
		for (Hero hero : heroList) {
			values.add(MessageFormat.format("\"{0}\":{1,number,#}", hero.getPosition(), (int) hero.getTotalAttribute(context, AttrConstant.MORALE)));
		}

		// [
		sb.append(ReportConstant.REPORT_BLOCK_START_TAG);

		sb.append(StringUtils.join(values, ReportConstant.REPORT_COMMA_SPLIT_TAG));

		// ]
		sb.append(ReportConstant.REPORT_BLOCK_END_TAG);

		return sb.toString();
	}
}
