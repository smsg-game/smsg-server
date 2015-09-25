package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;
import com.lodogame.model.PkGroupAwardLog;

public interface PkGroupAwardLogDao {
	/**
	 * 发放奖励(每天晚上9点)
	 * 
	 * @param pkGroupAwardLog
	 */
	public void add(PkGroupAwardLog pkGroupAwardLog);

	/**
	 * 组前10名奖励记录
	 * 
	 * @param groupId
	 * @return
	 */
	public List<PkGroupAwardLog> getList(int groupId);

	/**
	 * 更新领奖状态
	 * 
	 * @param userId
	 * @param date
	 */
	public boolean updateGet(String userId, Date date);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public PkGroupAwardLog get(String userId);

	/**
	 * 删除历史记录
	 * 
	 * @param date
	 */
	public void deleteRecord();

	/**
	 * 设置is_hostory 为1
	 */
	public void updateHostory();

}
