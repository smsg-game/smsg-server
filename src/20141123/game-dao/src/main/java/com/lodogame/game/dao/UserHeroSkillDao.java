package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserHeroSkill;

/**
 * 用户武将技能ID
 * 
 * @author jacky
 * 
 */
public interface UserHeroSkillDao {

	/**
	 * 获取用户武将技能列表
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public List<UserHeroSkill> getList(String userId, String userHeroId);

	/**
	 * 获取用户武将技能列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserHeroSkill> getList(String userId);

	/**
	 * 更新用户武将技能
	 * 
	 * @param userId
	 * @param userHeroSkillId
	 * @param skillGroupId
	 * @param skillId
	 * @return
	 */
	public boolean update(String userId, int userHeroSkillId, int skillGroupId, int skillId);

	/**
	 * 更新用户武将技能
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param skillGroupId
	 * @param skillId
	 * @return
	 */
	public boolean udpate(String userId, String userHeroId, int skillGroupId, int skillId);

	/**
	 * 用户武将技能
	 * 
	 * @param userHeroSkill
	 * @return
	 */
	public boolean add(UserHeroSkill userHeroSkill);

	/**
	 * 删除用户天赋技能
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param userHeroSkillId
	 * @return
	 */
	public boolean delete(String userId, String userHeroId, int userHeroSkillId);

	/**
	 * 获取用户武将技能
	 * 
	 * @param userId
	 * @param userHeroSkillId
	 * @return
	 */
	public UserHeroSkill get(String userId, int userHeroSkillId);

}
