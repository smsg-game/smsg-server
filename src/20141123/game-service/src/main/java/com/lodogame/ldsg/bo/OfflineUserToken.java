package com.lodogame.ldsg.bo;

/**
 * 离线用户的token
 * 
 * @author jacky
 * 
 */
public class OfflineUserToken {

	/**
	 * 用户登陆令牌
	 */
	private String Token;
	/**
	 * 合作厂商用户ID
	 */
	private String partnerUserId;
	/**
	 * 合作厂商ID
	 */
	private String partnerId;

	/**
	 * 服务器ID
	 */
	private String serverId;

	/**
	 * 游戏用户ID
	 */
	private String userId;

	/**
	 * 断线的时间
	 */
	private long logoutTime;

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(long logoutTime) {
		this.logoutTime = logoutTime;
	}

}
