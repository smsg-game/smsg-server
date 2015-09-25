package com.lodogame.game.dao;

public interface ActiveCodeDao {

	/**
	 * 用户是否已经激活
	 * 
	 * @param uuid
	 * @param partnerId 
	 * @return
	 */
	public boolean isActive(String uuid, String partnerId);

	/**
	 * 激活用户
	 * 
	 * @param uuid
	 * @param code
	 * @return
	 */
	public boolean active(String uuid, String code, String partnerId);

	/**
	 * imei是否在白名单中
	 * @param imei
	 * @return
	 */
	public boolean isBlackImei(String imei, String partnerId);
}
