package com.ldsg.battle.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ldsg.battle.util.Convert;

public class Skill {

	private String name;

	/**
	 * 触发时机
	 */
	private int triggerType;

	/**
	 * 目标选择
	 */
	private int selectType;

	/**
	 * 第二目标选择
	 */
	private int secondSelectType;

	/**
	 * 效果列表
	 */
	private List<Effect> effectList;

	/**
	 * 说明
	 */
	private String remark;

	/**
	 * 技能ID
	 */
	private int skillId;

	/**
	 * 判断类型
	 */
	private int checkValue;

	/**
	 * 是否增加士气
	 */
	private int addMorale;

	/**
	 * 参数
	 */
	private String[] params;

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(int triggerType) {
		this.triggerType = triggerType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSelectType() {
		return selectType;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

	public int getSecondSelectType() {
		return secondSelectType;
	}

	public void setSecondSelectType(int secondSelectType) {
		this.secondSelectType = secondSelectType;
	}

	public int getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(int checkValue) {
		this.checkValue = checkValue;
	}

	public List<Effect> getEffectList() {
		if (effectList == null) {
			return new ArrayList<Effect>();
		}
		return effectList;
	}

	public void setEffectList(List<Effect> effectList) {
		this.effectList = effectList;
	}

	public double getParam(int ind) {
		if (this.params.length <= ind) {
			return 0d;
		}
		return Convert.toDouble(this.params[ind]);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public int getAddMorale() {
		return addMorale;
	}

	public void setAddMorale(int addMorale) {
		this.addMorale = addMorale;
	}

}
