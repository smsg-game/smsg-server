package com.lodogame.game.dao;

import com.lodogame.model.UserMapper;

public interface UserMapperDao {
	/**
	 * 保存用户mapper
	 * 
	 * @param userMapper
	 * @return
	 */
	public boolean save(UserMapper userMapper);

	/**
	 * 根据partnerUserId获取用户mapper信息
	 * 
	 * @param partnerUserId
	 * @param serverId
	 * @return
	 */
	public UserMapper getByPartnerUserId(String partnerUserId, String partnerId, String serverId);

	/**
	 * 根据userId获取UserMapper表信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserMapper get(String userId);

	public boolean delete(String userId);
	
	/**
	 * 更新手机信息
	 * @param imei
	 * @param mac
	 * @param idfa
	 * @param userId
	 * @return
	 */
	boolean updatePhoneInfo(String userId, String imei, String mac, String idfa);
}
