package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserHeroSkillBO {

	/**
	 * 表id
	 */
	@Mapper(name = "hskid")
	private int userHeroSkillId;

	/**
	 * 用户武将ID
	 */
	@Mapper(name = "uhid")
	private String userHeroId;

	/**
	 * 技能组ID
	 */
	@Mapper(name = "sgid")
	private int skillGroupId;

	/**
	 * 技能ID
	 */
	@Mapper(name = "skid")
	private int skillId;

	public int getUserHeroSkillId() {
		return userHeroSkillId;
	}

	public void setUserHeroSkillId(int userHeroSkillId) {
		this.userHeroSkillId = userHeroSkillId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getSkillGroupId() {
		return skillGroupId;
	}

	public void setSkillGroupId(int skillGroupId) {
		this.skillGroupId = skillGroupId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

}
