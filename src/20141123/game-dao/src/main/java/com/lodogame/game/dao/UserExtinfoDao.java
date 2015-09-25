package com.lodogame.game.dao;

import com.lodogame.model.UserExtinfo;

public interface UserExtinfoDao {

	/**
	 * 添加用 户扩展信息
	 * 
	 * @param userExtinfo
	 * @return
	 */
	public boolean add(UserExtinfo userExtinfo);

	/**
	 * 获取用户扩展信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserExtinfo get(String userId);

	/**
	 * 更新购买银币次数
	 * 
	 * @param userId
	 * @param times
	 * @return
	 */
	public boolean updateBuyCopperTimes(String userId, int times);

	/**
	 * 更新购买体力次数
	 * 
	 * @param userId
	 * @param times
	 * @return
	 */
	public boolean updateBuyPowerTimes(String userId, int times);

	/**
	 * 增加武将背包上限
	 * 
	 * @param userId
	 * @param heroMax
	 * @return
	 */
	public boolean updateHeroMax(String userId, int heroBag);

	/**
	 * 增加装备背包上限
	 * 
	 * @param userId
	 * @param equipMax
	 * @return
	 */
	public boolean updateEquipMax(String userId, int equipBag);

	/**
	 * 更新新手引导步骤
	 * 
	 * @param userId
	 * @param step
	 * @return
	 */
	public boolean updateGuideStep(String userId, int step);

	/**
	 * 更新战斗胜负记录
	 * 
	 * @param userId
	 * @param isWin
	 * @return
	 */
	public boolean updateFightRecode(String userId, boolean isWin);

	/**
	 * 记录用户经历过的所有新手引导步骤
	 * 
	 * @param userId
	 * @param newStep
	 * @return
	 */
	public boolean recordGuideStep(String userId, String newStep);

	/**
	 * 设置奖励的vip等级
	 * 
	 * @param userId
	 * @param vipLevel
	 * @return
	 */
	public boolean updateRewardVipLevel(String userId, int vipLevel);

}
