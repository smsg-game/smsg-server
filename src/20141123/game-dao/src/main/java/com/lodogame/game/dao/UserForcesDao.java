package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserForces;
import com.lodogame.model.UserForcesCount;

public interface UserForcesDao {

	/**
	 * 获取用户场景怪物军队
	 * 
	 * @param userId
	 * @param sceneId
	 * @return
	 */
	public List<UserForces> getUserForcesList(String userId, int sceneId);

	/**
	 * 增加用户场景部队
	 * 
	 * @param userForces
	 * @return
	 */
	public boolean add(UserForces userForces);

	/**
	 * 获取用户当前可以攻打的怪物部队
	 * 
	 * @param userId
	 */
	public UserForces getUserCurrentForces(String userId, int forcesType);

	/**
	 * 获取用户怪物部队
	 * 
	 * @param userId
	 * @param forcesId
	 * @return
	 */
	public UserForces get(String userId, int forcesId);

	/**
	 * 更新怪物部队状态
	 * 
	 * @param suerId
	 * @param forcesId
	 * @param status
	 * @param times
	 *            增加的攻打次数
	 * @return
	 */
	public boolean updateStatus(String userId, int forcesId, int status, int times);

	/**
	 * 增加攻打次数
	 * 
	 * @param userId
	 * @param forcesId
	 * @param times
	 * @return
	 */
	public boolean updateTimes(String userId, int forcesId, int times);

	/**
	 * 根据用户痛快计数倒序获得列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size);

	/**
	 * 重置副本攻打次数
	 * 
	 * @param userId
	 * @param sceneIdList
	 * @return
	 */
	public boolean resetForcesTimes(String userId, List<Integer> sceneIdList);

	/**
	 * 获取修改阵法的请求时间
	 * 
	 * @param userId
	 * @param timestamp
	 * @return
	 */
	public long getAmendEmbattleTime(String userId);

	/**
	 * 设置请求最后时间
	 * 
	 * @param userId
	 * @param timestamp
	 */
	public void setAmendEmbattleTime(String userId, long timestamp);

	/**
	 * 批量改变部队攻打次数
	 * 
	 * @param uid
	 * @param times
	 * @param forcesIds
	 * @return
	 */
	public boolean updateTimes(String uid, int times, List<Integer> forcesIds);

}
