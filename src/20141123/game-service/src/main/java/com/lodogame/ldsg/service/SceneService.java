package com.lodogame.ldsg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lodogame.ldsg.bo.SweepInfoBO;
import com.lodogame.ldsg.bo.UserSceneBO;
import com.lodogame.ldsg.event.EventHandle;

/**
 * 场景service
 * 
 * @author jacky
 * 
 */
public interface SceneService {

	@SuppressWarnings("serial")
	public final static Map<Integer, String> section = new HashMap<Integer, String>() {
		{
			put(23012, "第三章");
			put(24012, "第四章");
			put(25012, "第五章");
			put(26012, "第六章");
			put(27012, "第七章");
		}
	};
	/**
	 * 体力不足
	 */
	public final static int ATTACK_FORCES_POWER_NOT_ENOUGH = 2003;
	/**
	 * 部队没有通过，无法扫荡
	 */
	public final static int ADD_SWEEP_RETURN_FORCES_NOT_PASS = 2001;

	/**
	 * 正在扫荡中
	 */
	public final static int ADD_SWEEP_RETURN_ING = 2002;

	/**
	 * 体力不足
	 */
	public final static int ADD_SWEEP_RETURN_POWER_NOT_ENOUGH = 2003;

	/**
	 * 超出攻打限制
	 */
	public final static int ADD_SWEEP_RETURN_LIMIT_EXCEED = 2004;

	/**
	 * 停止扫荡，没有进行中的扫荡
	 */
	public final static int STOP_SWEEP_NOT_ING = 2001;

	/**
	 * 加速扫荡，没有进行中的扫荡
	 */
	public final static int SPEED_UP_SWEEP_NOT_ING = 2001;

	/**
	 * 金币不足
	 */
	public final static int SPEED_UP_SWEEP_GOLD_NOT_ENOUGH = 2002;

	/**
	 * 武将背包超出限制
	 */
	public final static int USER_HERO_BAG_LIMIT_EXCEED = 2005;

	/**
	 * 装备背包超出限制
	 */
	public final static int USER_EQUIP_BAG_LIMIT_EXCEED = 2006;

	/**
	 * 突击检测部队失败(没有部队可突击)
	 */
	public final static int ASSAULT_SCENE_FORCE_CHECK_FAILED = 2001;

	/**
	 * 突击令不足
	 */
	public final static int ASSAULT_TOKEN_NOT_ENOUGH = 2002;

	/**
	 * 获取用户场景列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserSceneBO> getUserSceneList(String userId);

	/**
	 * 攻打怪物
	 * 
	 * @param userId
	 * @param forcesId
	 * @return
	 */
	public boolean attack(String userId, int forcesId, EventHandle handle);

	/**
	 * 获取当前场景
	 * 
	 * @param uid
	 * @return userSceneBO
	 */
	public UserSceneBO getCurrentScene(String uid);

	/**
	 * 处理武将开启
	 * 
	 * @param userId
	 * @param forcesId
	 */
	public Set<Integer> handleForcesPass(String userId, int forcesId);

	/**
	 * 开启后面的部队
	 * 
	 * @param userId
	 * @param forcesId
	 */
	public Set<Integer> openAfterForces(String userId, int forcesId);

	/**
	 * 开始扫荡
	 * 
	 * @param userId
	 * @param forcesId
	 * @param times
	 */
	public void sweep(String userId, int forcesId, int times, EventHandle handle);

	/**
	 * 停止当前进行中的扫荡
	 * 
	 * @param userId
	 * @param forcesId
	 */
	public void stopSweep(String userId, EventHandle handle);

	/**
	 * 加速扫荡
	 * 
	 * @param userId
	 */
	public void speedUpSweep(String userId);

	/**
	 * 领取扫荡奖励
	 * 
	 * @param userId
	 * @return
	 */
	public void receiveSweep(String userId, EventHandle handle);

	/**
	 * 更新用户扫荡信息
	 * 
	 * @param userId
	 * @return
	 */
	public boolean updateSweepComplete(String userId);

	/**
	 * 获得用户当前的扫荡信息
	 * 
	 * @param userId
	 * @return
	 */
	SweepInfoBO getUserSweepInfo(String userId);

	/**
	 * 突击
	 * 
	 * @param uid
	 * @param sid
	 * @param eventHandle
	 */
	public void assault(String uid, Integer sid, EventHandle eventHandle);

}
