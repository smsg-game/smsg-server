package com.lodogame.game.dao;

import java.util.Date;

import com.lodogame.model.SystemCheckInInfo;

public interface SystemCheckInInfoDao {

	/**
	 * 获取当前签到信息
	 * 
	 * @return
	 */
	public SystemCheckInInfo getSystemCheckInInfo();

	/**
	 * 更新系统签到信息
	 * 
	 * @param groupId
	 * @param finishTime
	 * @return
	 */
	public boolean setSystemCheckInInfo(int groupId, Date finishTime);

}
