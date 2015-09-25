package com.lodogame.ldsg.service;

import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.event.EventHandle;

public interface BattleService {

	/**
	 * 发起战斗
	 */
	public void fight(BattleBO attack, BattleBO defense, int type, EventHandle handle);

	/**
	 * 保存战报
	 */
	public void saveReport(String userId, String targetId, int type, int flag, String report, boolean async);
	
	public BattleBO createBattleBO(String userId);
}
