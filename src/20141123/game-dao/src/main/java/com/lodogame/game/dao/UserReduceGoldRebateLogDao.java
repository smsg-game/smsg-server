package com.lodogame.game.dao;

import java.util.Date;

public interface UserReduceGoldRebateLogDao {
	
	public int getTodayReduceGold(String userId, String now);
	
	public int getTotalReduceGold(String userId, Date startDate, Date endDate);
	
	/**
	 * 是否有每日领取记录
	 * @param userId
	 * @param date
	 * @param times
	 * @return
	 */
	public boolean isAwardRecord(String userId, String date, int amount, int type);
	
	/**
	 * 是否有累计领取记录
	 * @param userId
	 * @param date
	 * @param times
	 * @return
	 */
	public boolean isTotalAwardRecord(String userId, Date startTime, Date endTime, int amount,int type);
	
	/**
	 * 添加领取记录
	 * @param userId
	 * @param date
	 * @param type
	 * @param times
	 * @return
	 */
	public boolean addAwardRecord(String userId, Date date, int type, int amount);
	
}
