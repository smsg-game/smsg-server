package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserApplyFaction;

/**
 * 帮派dao
 * 
 * @author zyz
 * 
 */
public interface UserApplyFactionDao {


	/**
	 * 获取指定帮派有哪些申请的人
	 * 
	 * @param fid
	 * @return
	 */
	public List<UserApplyFaction> getApplyFactionByFid(int fid);
	
	/**
	 * 申请加入帮派
	 * 
	 * @param userApplyFaction
	 * @return
	 */
	public boolean addUserApplyFaction(UserApplyFaction userApplyFaction);

	/**
	 * 删除申请记录
	 * 
	 * @param userApplyFaction
	 * @return
	 */
	public boolean delUserApplyFaction(String userId, int factionId);
	
	/**
	 * 删除所有申请记录
	 * 
	 * @param userApplyFaction
	 * @return
	 */
	public boolean delUserApplyFaction(String userId);
	
	/**
	 * 删除所有申请记录
	 * 
	 * @param userApplyFaction
	 * @return
	 */
	public List<UserApplyFaction> getUserApplyFactionByUserId(String userId);
	
}