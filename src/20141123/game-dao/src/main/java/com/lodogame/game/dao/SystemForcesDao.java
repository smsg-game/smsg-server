package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemForces;

public interface SystemForcesDao {

	/**
	 * 获取关卡部队列表
	 * 
	 * @param sceneId
	 * @return
	 */
	public List<SystemForces> getSceneForcesList(int sceneId);

	/**
	 * 获取单个关卡部队
	 * 
	 * @param forcesId
	 * @return
	 */
	public SystemForces get(int forcesId);

	/**
	 * 根据前置怪获取怪物部队
	 * 
	 * @param preForcesId
	 * @return
	 */
	public List<SystemForces> getSystemForcesByPreForcesId(int preForcesId);
	
	
	/**
	 * 根据类型获得怪物部队
	 * 
	 * @param type
	 * @return
	 */
	public List<SystemForces> getSystemForcesByType(int type);
	
	/**
	 * 更新关卡次数
	 * @param forcesId
	 * @param times
	 * @return
	 */
	public int updateTimes(int forcesId, int times);

}
