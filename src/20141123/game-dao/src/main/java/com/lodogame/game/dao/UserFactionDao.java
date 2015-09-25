package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserFaction;


/**
 * 帮派成员
 * 
 * @author zyz
 * 
 */
public interface UserFactionDao {


	/**
	 * 获取指定帮派的成员
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserFaction> getFactionMemberByFid(int fid);
	
	/**
	 * 加入帮派
	 * 
	 * @param userId
	 * @return
	 */
	public boolean addUserFaction(UserFaction userFaction);
	
	/**
	 * 退出帮派
	 * 
	 * @param userId
	 * @return
	 */
	public boolean delUserFaction(String userId, int fid);
	
	/**
	 * 解散帮派
	 * @param fid
	 * @return
	 */
	public boolean delAllUserFactionByFactionId(int fid);
	
}