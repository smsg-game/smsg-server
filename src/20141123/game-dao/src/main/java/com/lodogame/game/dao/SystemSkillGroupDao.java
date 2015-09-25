package com.lodogame.game.dao;

import com.lodogame.model.SystemSkillGroup;

/**
 * 系统技能分组dao
 * 
 * @author jacky
 * 
 */
public interface SystemSkillGroupDao {

	/**
	 * 根据技能书获取相应的技能分组
	 * 
	 * @param toolId
	 * @return
	 */
	public SystemSkillGroup getByToolId(int toolId);

	/**
	 * 根据技能分组ID获取对应的技能分组
	 * 
	 * @param skillGroupId
	 * @return
	 */
	public SystemSkillGroup get(int skillGroupId);

}
