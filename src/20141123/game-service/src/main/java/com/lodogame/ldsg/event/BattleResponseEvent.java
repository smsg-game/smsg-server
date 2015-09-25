package com.lodogame.ldsg.event;

import java.util.Map;

/**
 * 战斗结束事件
 * 
 * @author jacky
 * 
 */
public class BattleResponseEvent extends BaseEvent {

	public BattleResponseEvent(String report, int flag, Map<String, Integer> lifeMap) {
		this.data.put("report", report);
		this.data.put("flag", flag);
		this.data.put("life", lifeMap);
	}
}
