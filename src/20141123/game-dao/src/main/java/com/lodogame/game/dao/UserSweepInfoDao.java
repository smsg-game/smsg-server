/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2013
 */

package com.lodogame.game.dao;

import com.lodogame.model.UserSweepInfo;


public interface UserSweepInfoDao{
	/**
	 * 增加扫荡信息，状态为0
	 * @param sweepInfo
	 * @return
	 */
	public boolean add(UserSweepInfo sweepInfo);
	
	/**
	 * 根据获取当前的sweep
	 * @param userId
	 * @return
	 */
	public UserSweepInfo getCurrentSweep(String userId);
	
	/**
	 * 更新扫荡为完成状态，状态为1
	 * @param userId
	 * @return
	 */
	public boolean updateSweepComplete(String userId);
	
	/**
	 * 停止扫荡，状态为-1
	 * @param userId
	 * @return
	 */
	public boolean stopSweep(String userId);

	/**
	 * 更新扫荡信息为领取状态，状态为2
	 * @param userId
	 * @return
	 */
	public boolean updateSweepReceived(String userId);
}
