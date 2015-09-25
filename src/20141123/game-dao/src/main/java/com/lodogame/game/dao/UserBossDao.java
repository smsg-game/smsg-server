/**
 * UserBossDao.java
 *
 * Copyright 2013 Easou Inc. All Rights Reserved.
 *
 */

package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserBoss;

/**
 * @author <a href="mailto:clact_jia@staff.easou.com">Clact</a>
 * @since v1.0.0.2013-9-25
 */
public interface UserBossDao {

	public boolean resetCooldown(String userId, int forcesId);

	public UserBoss getBoss(String userId, int forcesId);

	public List<UserBoss> getBossList(String userId);

	public boolean clean();

	public void addToList(String userId, List<UserBoss> userBossList);

	/**
	 * 在用户要攻打某张地图的 Boss 时，添加用户和地图的对应关系
	 * 
	 * @param mid
	 *            地图编号
	 */
	public void addUserMap(String userId, int mid);

	/**
	 * 获取用户所在的地图编号
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserMap(String userId);
	
	/**
	 * 读取一张地图中的所有用户
	 * @param forceId
	 * @return
	 */
	public List<String> getUsers(int forceId);
}
