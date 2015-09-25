package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.ForcesBo;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.model.UserForcesCount;

/**
 * 场景部队列表
 * 
 * @author jacky
 * 
 */
public interface ForcesService {

	/**
	 * 重置副本失败，次数不足
	 */
	public final static int RESET_FORCES_TIMES_TIMES_LIMIT = 2001;

	/**
	 * 重置副本失败，材料不足
	 */
	public final static int RESET_FORCES_MONEY_NOT_ENOUGH = 2002;

	/**
	 * 
	 * @param userId
	 * @param sceneId
	 * @return
	 */
	public List<UserForcesBO> getUserForcesList(String userId, int sceneId);

	/**
	 * 获取一支军队的怪物列表(战斗对象)
	 * 
	 * @param forcesId
	 * @return
	 */
	public List<BattleHeroBO> getForcesHeroBOList(int forcesId);

	/**
	 * 获取用户当前可以攻打的怪物部队
	 * 
	 * @param userId
	 * @param forcesType
	 * @return
	 */
	public UserForcesBO getUserCurrentForces(String userId, int forcesType);

	/**
	 * 根据玩家通关的数量倒序获得列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<UserForcesCount> listOrderByForceCntDesc(int offset, int size);

	/**
	 * 重置次数
	 * 
	 * @param userId
	 * @param sceneList
	 * @return
	 */
	public boolean resetForcesTimes(String userId, List<Integer> sceneList);

	/**
	 * 跳过战斗
	 * 
	 * @param userId
	 * @return
	 */
	public boolean passForcesBattle(String userId);
	
	/**
	 * 活动FB次数
	 * @param type
	 * @return
	 */
	public Map<String,String> getForcesTimes(int type);
	
	/**
	 * 更新FB次数
	 * @param forcesId
	 * @param times
	 * @return
	 */
	public int updateForcesTime(int forcesId, int times );
}
