package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserHero;

/**
 * 用户武将DAO
 * 
 * @author jacky
 * 
 */

public interface UserHeroDao {

	/**
	 * 获取用户武将列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserHero> getUserHeroList(String userId);

	/**
	 * 获取用用户某一武将列表
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @return
	 */
	public List<UserHero> getUserHeroList(String userId, int systemHeroId);

	/**
	 * 设置用户英雄列表
	 * 
	 * @param userId
	 * @param userHeroList
	 */
	public void setUserHeroList(String userId, List<UserHero> userHeroList);

	/**
	 * 获取用户单个武将信息
	 * 
	 * @param userHeroId
	 * @return
	 */
	public UserHero get(String userId, String userHeroId);

	/**
	 * 根据上场位置获取武将
	 * 
	 * @param userId
	 * @param pos
	 * @return
	 */
	public UserHero getUserHeroByPos(String userId, int pos);

	/**
	 * 添加用户武将
	 * 
	 * @param userHero
	 * @return
	 */
	public boolean addUserHero(UserHero userHero);

	/**
	 * 指添加用 户武将
	 * 
	 * @param userHeroList
	 * @return
	 */
	public boolean addUserHero(List<UserHero> userHeroList);

	/**
	 * 修改武将占位
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param pos
	 * @return
	 */
	public boolean changePos(String userId, String userHeroId, int pos);

	/**
	 * 更新用户武将系统武将ID(进阶)
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param systemHeroId
	 * @return
	 */
	public boolean changeSystemHeroId(String userId, String userHeroId, int systemHeroId);

	/**
	 * 删除用户武 将
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public boolean delete(String userId, String userHeroId);

	/**
	 * 删除武将(批量)
	 * 
	 * @param userId
	 * @param userHeroIdList
	 * @return
	 */
	public int delete(String userId, List<String> userHeroIdList);

	/**
	 * 更新武将经验，等级
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param exp
	 * @param level
	 * @return
	 */
	public boolean updateExpLevel(String userId, String userHeroId, int exp, int level);

	/**
	 * 获取上阵武将数量
	 * 
	 * @param userId
	 * @return
	 */
	public int getBattleHeroCount(String userId);

	/**
	 * 获取用户武将数量
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserHeroCount(String userId);

	/**
	 * 根据武将等级倒序获取列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<UserHero> listByHeroLevelDesc(int offset, int size);

	/**
	 * 将用户中具有相同 system_hero_id 的武将按 hero_level 正序排序
	 */
	public List<UserHero> listUserHeroByLevelAsc(String userId, int systemHeroId);

	/**
	 * 更新用户武将,转生用
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param systemHeroId
	 * @param level
	 * @param exp
	 * @return
	 */
	public boolean update(String userId, String userHeroId, int systemHeroId, int level, int exp);
	
	/**
	 * 增加血祭等级
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param pos
	 * @return
	 */
	public boolean upgradeStage(String userId, String userHeroId);
	
	
	/**
	 * 锁定武将
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param pos
	 * @return
	 */
	public boolean lockHero(String userId, String userHeroId);

	
	/**
	 * 武将解除锁定
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param pos
	 * @return
	 */
	public boolean unlockHero(String userId, String userHeroId);

	/**
	 * 更改用户化神等级
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param deifyNodeLevel
	 * @return
	 */
	public boolean upgradeDeifyNodeLevel(String userId, String userHeroId,int deifyNodeLevel);
	
	/**
	 * 获取用户神将列表
	 * @param userId
	 * @return
	 */
	public List<UserHero> getDeifyPeopleList(String userId);
	

}
