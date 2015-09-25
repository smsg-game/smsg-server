package com.ldsg.battle.bo;

import java.util.List;

/**
 * 组合技BO
 * 
 * @author jacky
 * 
 */
public class MergeSkill {

	/**
	 * 组合的武将列表
	 */
	private List<String> posList;

	/*
	 * 技能ID
	 */
	private int skillId;

	public List<String> getPosList() {
		return posList;
	}

	public void setPosList(List<String> posList) {
		this.posList = posList;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

}
