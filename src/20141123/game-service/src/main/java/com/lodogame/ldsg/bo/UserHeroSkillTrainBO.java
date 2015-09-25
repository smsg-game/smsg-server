package com.lodogame.ldsg.bo;

import java.util.Date;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 用户武将技能训练
 * 
 * @author jacky
 * 
 */
@Compress
public class UserHeroSkillTrainBO {

	/**
	 * 技能分组ID
	 */
	@Mapper(name = "sgid")
	private int skillGroupId;

	/**
	 * 技能ID
	 */
	@Mapper(name = "skid")
	private int skillId;

	/**
	 * 锁定
	 */
	@Mapper(name = "lk")
	private int lock;

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

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

}
