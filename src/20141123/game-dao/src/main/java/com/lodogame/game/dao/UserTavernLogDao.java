package com.lodogame.game.dao;

public interface UserTavernLogDao {

	/**
	 * 是否有掉四星以上武将的
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isExistLog(String userId);

	/**
	 * 添加掉4星以上武将的日志
	 * 
	 * @param userId
	 * @return
	 */
	public boolean addTavernLog(String userId);
}
