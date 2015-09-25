package com.lodogame.game.dao;

import com.lodogame.model.UserCheckinLog;

/**
 * 用户签到日志dao
 * 
 * @author jacky
 * 
 */
public interface UserCheckInLogDao {

	/**
	 * 添加签到日志
	 * 
	 * @param userCheckInLog
	 * @return
	 */
	public boolean addUserCheckInLog(UserCheckinLog userCheckInLog);

	/**
	 * 获取最后签到日志
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public UserCheckinLog getLastUserCheckInLog(String userId, int groupId);

	/**
	 * 清空用户签到日志
	 * 
	 * @return
	 */
	public boolean cleanUserCheckInLog();

}
