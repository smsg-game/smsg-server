package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserHeroSkillTrain;

/**
 * 用户武将技能训练Dao
 * 
 * @author jacky
 * 
 */
public interface UserHeroSkillTrainDao {

	/**
	 * 获取用户武将技能训练列表
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public List<UserHeroSkillTrain> getList(String userId, String userHeroId);

	/**
	 * 删除用户武将训练表
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public boolean delete(String userId, String userHeroId);

	/**
	 * 删除某个技能训练项
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param skillGroupId
	 * @return
	 */
	public boolean delete(String userId, String userHeroId, int skillGroupId);

	/**
	 * 保存用户技能训练结果
	 * 
	 * @param userHeroSkillTrainList
	 * @return
	 */
	public boolean add(List<UserHeroSkillTrain> userHeroSkillTrainList);
}
