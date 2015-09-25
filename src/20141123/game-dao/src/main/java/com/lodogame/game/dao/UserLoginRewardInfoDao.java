package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserLoginRewardInfo;


public interface UserLoginRewardInfoDao {

	/**
	 * 添加联系登入日志
	 * 
	 * @param UserLoginRewardInfo
	 * @return
	 */
	public boolean addUserLoginRewardInfo(UserLoginRewardInfo userLoginRewardInfo);
	
	
	/**
	 * 查询用户指定某天的
	 * 
	 * @param userId day
	 * @return
	 */
	public UserLoginRewardInfo getUserLoginRewardInfoByDay(String userId, int day);

	/**
	 * 领取了某天的礼包的状态
	 * 
	 * @param user day date rewardStatus
	 * @return
	 */
	public int updateUserLoginRewardInfoByDay(String userId, int day, String date, int rewardStatus);
	

	/**
	 * 查询用户最后一次的记录
	 * 
	 * @param userId
	 * @return
	 */
	public UserLoginRewardInfo getUserLastLoginRewardInfo(String userId);
	
	/**
	 * 
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserLoginRewardInfo> getUserLoginRewardInfo(String userId);
	
}
