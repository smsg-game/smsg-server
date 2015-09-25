package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;

import com.lodogame.model.UserTavern;

public interface UserTavernDao {

	/**
	 * 添加用户酒馆抽奖信息
	 * 
	 * @param userTavern
	 * @return
	 */
	public boolean add(UserTavern userTavern);

	/**
	 * 获取用户酒馆投资信息
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public UserTavern get(String userId, int type);

	/**
	 * 获取用户酒馆抽奖信息列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserTavern> getList(String userId);

	/**
	 * 更新抽奖信息
	 * 
	 * @param userId
	 * @param type
	 * @param updatedTime
	 * @param totalTimes
	 * @param amendTimes
	 * @return
	 */
	public boolean updateTavernInfo(String userId, int type, Date updatedTime, int totalTimes, int amendTimes,int hadUsedMoney );

}
