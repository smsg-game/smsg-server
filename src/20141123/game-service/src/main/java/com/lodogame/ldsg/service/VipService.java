package com.lodogame.ldsg.service;

public interface VipService {

	/**
	 * 获取争霸次数限制
	 * 
	 * @param userId
	 * @return
	 */
	public int getPkTimesLimit(String userId);

	/**
	 * 获取争霸次数限制
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getPkTimesLimit(int vipLevel);

	/**
	 * 重置扫荡次数限制
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getResetForcesTimesLimit(int vipLevel);

	/**
	 * 获取体力上限
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getPowerMax(int vipLevel);

	/**
	 * 获取用户经验加成值
	 * 
	 * @param vipLevel
	 * @return
	 */
	public double getExpAddRatio(int vipLevel);

	/**
	 * 获取副本银币 掉落加成值
	 * 
	 * @param vipLevel
	 * @return
	 */
	public double getCopperAddRatio(int vipLevel);

	/**
	 * 获取购买体力上限
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getBuyPowerLimit(int vipLevel);
	
	/**
	 * 重置转生塔次数限制
	 * 
	 * @param vipLevel
	 * @return
	 */
	public int getResetTowerTimesLimit(int vipLevel);
	
}
