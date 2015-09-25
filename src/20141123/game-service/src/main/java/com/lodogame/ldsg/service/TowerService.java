package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserTowerBO;
import com.lodogame.ldsg.bo.UserTowerFloorBO;
import com.lodogame.ldsg.bo.UserTowerMapDataBO;
import com.lodogame.ldsg.event.EventHandle;

public interface TowerService {
	
	/**
	 * 金币不足
	 */
	public final static int GOLD_NOT_ENOUGH = 2001;
	
	/**
	 * VIP等级不足
	 */
	public final static int VIP_LEVEL_NOT_ENOUGH = 2002;
	
	/**
	 * 火把不足
	 */
	public final static int TORCE_NOT_ENOUGH = 2103;

	/**
	 * 进入转生塔
	 * 
	 * @param uid
	 * @return
	 */
	public List<UserTowerBO> enter(String uid);

	/**
	 * 进入楼层
	 * 
	 * @param uid
	 * @param floor
	 * @return
	 */
	public List<UserTowerMapDataBO> enterFloor(String uid, int floor);

	/**
	 * 消耗体力
	 * 
	 * @param uid
	 */
	public void useTool(String uid);

	/**
	 * 获取宝箱
	 * 
	 * @param uid
	 * @param floor
	 * @param pos
	 * @param tid
	 */
	public void pickUpBox(String uid, int floor, int pos, int tid);

	/**
	 * 重置爬塔
	 * 
	 * @param uid
	 * @return
	 */
	public List<UserTowerBO> reset(String uid, boolean useMoney);

	/**
	 * 加载地图数据到缓存
	 */
	public void loadSystemTowerData();

	/**
	 * 转生战斗
	 * 
	 * @param uid
	 * @param forcesId
	 * @param pos
	 * @param eventHandle
	 */
	public void towerFight(String uid, int floor, int pos, int forcesId, EventHandle eventHandle);
}
