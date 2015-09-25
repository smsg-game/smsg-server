package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemPassiveSkill;

public interface SystemPassiveSkillDao {

	/**
	 * 根据组id和技能id获取被动技能
	 * 
	 * @param skillGroupId
	 * @param skillId
	 * @return
	 */
	public SystemPassiveSkill get(int skillGroupId, int skillId);

	/**
	 * 获取某组被动技能里面的第一个技能
	 * 
	 * @param skillGroupId
	 * @return
	 */
	public SystemPassiveSkill getFirst(int skillGroupId);

	/**
	 * 获某组技能
	 * 
	 * @param skillGroupId
	 * @return
	 */
	public List<SystemPassiveSkill> getList(int skillGroupId);

}
