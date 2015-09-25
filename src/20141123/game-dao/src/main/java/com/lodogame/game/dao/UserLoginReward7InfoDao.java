package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserLoginReward7Info;

public interface UserLoginReward7InfoDao {
	
	/**
	 * 添加用户七天登陆奖励信息
	 */
	public boolean addUserLoginReward7Info(UserLoginReward7Info loginInfo);

	
	/**
	 * 查询用户最后一天登录的信息
	 * @param userId
	 * @return
	 */
	public UserLoginReward7Info getLastLogin(String userId);

	/**
	 * 获取某一天之前的领取信息
	 * @param userId
	 * @param day
	 */
	public List<UserLoginReward7Info> getLoginInfoList(String userId, int day);
}
