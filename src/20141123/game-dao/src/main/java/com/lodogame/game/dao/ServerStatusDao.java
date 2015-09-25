package com.lodogame.game.dao;

import com.lodogame.model.ServerStatus;

public interface ServerStatusDao {

	/**
	 * 获取服务器状态
	 * 
	 * @return
	 */
	public ServerStatus getServerStatus(String partnerId);

	/**
	 * 设置服务器状态
	 * 
	 * @param status
	 * @param whiteList
	 */
	public void setServerStatus(int id, int status, String whiteList);

	/**
	 * 是否充许的IP
	 * 
	 * @param ip
	 * @return
	 */
	public boolean isWhiteIp(String partnerId, String ip);
}
