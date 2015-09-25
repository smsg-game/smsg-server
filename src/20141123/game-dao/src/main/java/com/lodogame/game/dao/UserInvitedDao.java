package com.lodogame.game.dao;

import com.lodogame.model.UserInvited;

/**
 * 邀请注册 DAO
 * 
 * @author chengevo
 * 
 */
public interface UserInvitedDao {

	public boolean add(UserInvited userInvited);

	/**
	 * 更新任务完成情况
	 * 
	 * @param invitedUserId
	 * @param finishTaskIds
	 * @return
	 */
	public boolean update(String invitedUserId, String finishTaskIds);

	/**
	 * 根据被邀请用户的 Id 查询
	 * 
	 * @param invitedUserId
	 * @return
	 */
	public UserInvited getByInvitedUserId(String invitedUserId);

}
