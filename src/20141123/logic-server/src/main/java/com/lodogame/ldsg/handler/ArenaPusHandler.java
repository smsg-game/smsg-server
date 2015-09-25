package com.lodogame.ldsg.handler;

import java.util.Map;

public interface ArenaPusHandler {

	/**
	 * 推送排行榜
	 * 
	 * @param userId
	 */
	public void pushRank(String userId);

	/**
	 * 推送用户信息
	 * 
	 * @param userId
	 */
	public void pushUserInfo(String userId);

	/**
	 * 推送战报
	 * 
	 * @param userId
	 * @param params
	 */
	public void pushBattle(String userId, Map<String, String> params);

	/**
	 * 推送战斗记录
	 * 
	 * @param userId
	 * @param params
	 */
	public void pushBattleRecord(String userId, Map<String, String> params);

	/**
	 * 推送活动奖励信息
	 * 
	 * @param userId
	 * @param params
	 */
	public void pushReward(String userId);
}
