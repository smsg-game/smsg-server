/**
 * BossEvent.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.ldsg.event;

import com.lodogame.model.BossTeam;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-26
 */
public class BossEvent extends BaseEvent {
	public static final String KEY_OF_BOSS_TEAM = "bossTeam";
	/**
	 * 
	 * @param team
	 * @param requesterId
	 *          请求Service方法的用户编号
	 */
	public BossEvent(BossTeam team, String requesterId) {
		this.userId = requesterId;
		this.data.put(KEY_OF_BOSS_TEAM, team);
	}
}
