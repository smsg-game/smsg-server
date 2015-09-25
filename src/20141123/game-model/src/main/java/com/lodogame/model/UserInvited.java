package com.lodogame.model;

/**
 * 邀请注册
 * 
 * @author chengevo
 * 
 */
public class UserInvited {

	/**
	 * 邀请人ID
	 */
	private String userId;

	/**
	 * code
	 */
	private long code;

	/**
	 * 被邀请人ID
	 */
	private String invitedUserId;

	/**
	 * 已经完成的任务ID列表
	 */
	private String finishTaskIds;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getInvitedUserId() {
		return invitedUserId;
	}

	public void setInvitedUserId(String invitedUserId) {
		this.invitedUserId = invitedUserId;
	}

	public String getFinishTaskIds() {
		return finishTaskIds;
	}

	public void setFinishTaskIds(String finishTaskIds) {
		this.finishTaskIds = finishTaskIds;
	}

}
