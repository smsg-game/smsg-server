package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemCheckInReward;

/**
 * 用户签到奖励dao
 * 
 * @author jacky
 * 
 */
public interface SystemCheckInRewardDao {

	/**
	 * 获取某一天的奖励
	 * 
	 * @param groupId
	 * @param day
	 * @return
	 */
	public SystemCheckInReward getSystemCheckInReward(int groupId, int day);

	/**
	 * 获取某一组的奖励
	 * 
	 * @param groupId
	 * @return
	 */
	public List<SystemCheckInReward> getSystemCheckInReward(int groupId);

}
