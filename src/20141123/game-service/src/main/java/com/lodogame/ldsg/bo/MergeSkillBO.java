package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class MergeSkillBO {

	/**
	 * 技能ID
	 */
	@Mapper(name = "skid")
	private int skillId;

	/**
	 * 组合武将列表
	 */
	@Mapper(name = "plist")
	private List<Integer> posList;

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public List<Integer> getPosList() {
		return posList;
	}

	public void setPosList(List<Integer> posList) {
		this.posList = posList;
	}

}
