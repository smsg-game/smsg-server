package com.lodogame.ldsg.handler;

import java.util.Map;

public interface WarPushHandler {
	/**
	 * 推送所有城池
	 */
	public void pushAllCity();

	/**
	 * 推送国战结束
	 */
	public void pushWarEnd();

	/**
	 * 推送防守次数
	 * 
	 * @param userId
	 * @param defenseNum
	 */
	public void pushDefenseNum(String userId, Map<String, String> params);

	/**
	 * 推送防守战报
	 * 
	 * @param attackUserId
	 * @param defenseUserId
	 * @param attackName
	 * @param defenseName
	 * @param rf
	 * @param report
	 */
	public void pushBattle(String userId, Map<String, String> params);

}
