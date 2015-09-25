package com.lodogame.game.dao;

import java.util.Date;

import com.lodogame.model.UserTavernRebateLog;

public interface UserTavernRebateLogDao {

	/**
	 * 给定时间的招募次数
	 * 
	 * @return
	 */
	public int getTodayNum(String userId, int type, String now);

	/**
	 * 活动时间总招募次数
	 * 
	 * @param startDate
	 *            活动开始时间
	 * @param endDate
	 *            活动结束时间
	 * @return
	 */
	public int getTotalNum(String userId, int type, Date startDate, Date endDate);

	/**
	 * 设置次数
	 * 
	 * @return
	 */
	public boolean setNum(String userId, String now, int times, int type);

	/**
	 * 是否有每日领取记录
	 * 
	 * @param userId
	 * @param date
	 * @param type
	 * @param times
	 * @return
	 */
	public boolean isAwardRecord(String userId, String date, int type, int times);

	/**
	 * 是否有累计领取记录
	 * 
	 * @param userId
	 * @param date
	 * @param type
	 * @param times
	 * @return
	 */
	public boolean isTotalAwardRecord(String userId, Date startTime, Date endTime, int type, int times);

	/**
	 * 添加领取记录
	 * 
	 * @param userId
	 * @param date
	 * @param type
	 * @param times
	 * @return
	 */
	public boolean addAwardRecord(String userId, Date date, int type, int times);

}
