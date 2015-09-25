package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserPkInfo;

public interface UserPkInfoDao {

	/**
	 * 获取当前最后一名的用户争霸信息
	 * 
	 * @return
	 */
	public UserPkInfo getLastUserPkInfo();

	/**
	 * 增加用户排名信息
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public boolean add(UserPkInfo userPkInfo);

	/**
	 * 根据排名读取用户争霸信息
	 * 
	 * @param rank
	 * @return
	 */
	public UserPkInfo getByRank(int rank);

	/**
	 * 根据用户ID读取用户争霸信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserPkInfo getByUserId(String userId);

	/**
	 * 更新用户排名信息
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public boolean update(UserPkInfo userPkInfo);

	/**
	 * 根据范围获取用户排名列表
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<UserPkInfo> getByRankRange(int start, int end);

	/**
	 * 获得排名
	 * 
	 * @param rankList
	 * @return
	 */
	public List<UserPkInfo> getRanks(List<Integer> rankList);
	
	/**
	 * 获得组排名
	 * 
	 * @param rankList
	 * @param beginLevel
	 * @param endLevel
	 * @return
	 */
	public List<UserPkInfo> getGroupRanks(int beginLevel, int endLevel);
	
	/**
	 * 获得组第一排名
	 * 
	 * @param rankList
	 * @param beginLevel
	 * @param endLevel
	 * @return
	 */
	public UserPkInfo getGroupFirstRanks(int beginLevel, int endLevel);

	/**
	 * 添加积分
	 * 
	 * @param userId
	 * @param score
	 * @return
	 */
	public boolean addScore(String userId, int score);

	/**
	 * 备份排名
	 */
	public void backUserPkInfo();

	/**
	 * 更换排名
	 * 
	 * @param userId
	 * @param targetUserId
	 * @return
	 */
	public boolean changeRank(String userId, String targetUserId);

	/**
	 * 添加第一名
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public boolean addFirst(UserPkInfo userPkInfo);

	/**
	 * 购买争霸次数
	 * 
	 * @param userId
	 * @return
	 */
	public boolean buyPkTimes(String userId, int buyPkTimes);

	/**
	 * 增加增霸次数
	 * 
	 * @param userId
	 * @param pkTimes
	 * @return
	 */
	public boolean addPkTimes(String userId, int pkTimes);
	
	/**
	 * 更新连胜场数
	 * @param userId
	 */
	public void updateTimes(String userId);
	
	/**
	 * 清空连胜场数
	 * @param userId
	 */
	public void clearTimes(String userId);
	
	/**
	 * 组排名前10 
	 * @param beginLevel
	 * @param endLevel
	 * @return
	 */
	public List<UserPkInfo> getGrankTen(int beginLevel, int endLevel);
	
	/**
	 * 总排名前10
	 * @return
	 */
	public List<UserPkInfo> getTotalTen();
	
	/**
	 * 更新用户等级
	 * @param userId
	 * @param level
	 */
	public void updateUserLevel(String userId, int level);
	
}
