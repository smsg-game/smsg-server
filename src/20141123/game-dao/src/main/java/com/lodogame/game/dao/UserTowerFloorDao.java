package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserTowerFloor;

public interface UserTowerFloorDao {

	/**
	 * 查询用户的爬塔信息
	 * @param uid
	 * @param floor 
	 * @return
	 */
	public List<UserTowerFloor> getUserTowerFloor(String uid, int floor);

	/**
	 * 清空楼层数据
	 * @param uid
	 */
	public boolean clearTowerFloorData(String uid);

	/**
	 * 根据详细信息获得具体的位置数据
	 * @param uid
	 * @param floor
	 * @param pos
	 * @param tid
	 * @return
	 */
	public UserTowerFloor getUserTowerFloor(String uid, int floor, int pos, int tid);

	/**
	 * 保存用户楼层信息
	 * @param uid
	 * @param list
	 */
	public boolean saveUserTowerFloor(UserTowerFloor data);

	/**
	 * 更新对象状态
	 * @param uid
	 * @param floor
	 * @param pos
	 * @param isPassed
	 * @return 
	 */
	public boolean updateObjStatus(String uid, int floor, int pos, int isPassed);
}
