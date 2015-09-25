/**
 * BossDao.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.Boss;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public interface BossDao {

	/**
	 * 魔物部队类型
	 */
	public static final int BOSS_FORCE_TYPE = 5;

	/**
	 * 获取游戏中所有的 Boss
	 * 
	 * @return
	 */
	public List<Boss> getBossList();

	/**
	 * 根据场景id获取boss
	 * 
	 * @param forcesId
	 * @return
	 */
	public Boss getBossByForcesId(int forcesId);
}
