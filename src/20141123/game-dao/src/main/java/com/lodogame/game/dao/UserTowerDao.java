package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserTower;

public interface UserTowerDao {

	/**
	 * 获取用户爬塔信息
	 * @param uid
	 * @return
	 */
	List<UserTower> getUserTower(String uid);

	/**
	 * 重置宝塔对象
	 * @param uid
	 */
	boolean resetTowerStage(String uid);
	
	/**
	 * 新建爬塔数据
	 * @param stage
	 */
	UserTower newStage(String uid, int stage, int floor);

	/**
	 * 
	 * @param uid
	 * @param curStage
	 * @param status
	 * @param times
	 * @return
	 */
	boolean updateTowerStatus(String uid, int curStage, int status, int times);

	/**
	 * 更新当前关卡楼层数
	 * @param uid
	 * @param curStage
	 * @param i
	 */
	boolean updateStageFloor(String uid, int curStage, int floor);
}
