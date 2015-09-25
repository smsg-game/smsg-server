package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemActivity;

public interface SystemActivityDao {

	/**
	 * 获取系统活动
	 * 
	 * @param activityType
	 * @return
	 */
	public List<SystemActivity> getList(int activityType);

	/**
	 * 根据id获取活动
	 * 
	 * @param activityId
	 * @return
	 */
	public SystemActivity get(int activityId);

	/**
	 * 获取在活动面板显示的活动列表
	 * 
	 * @return
	 */
	public List<SystemActivity> getDisplayActivityLiset();

	/**
	 * 添加活动
	 * 
	 * @param systemActivity
	 */
	public boolean addActivity(SystemActivity systemActivity);

	/**
	 * 修改活动
	 * 
	 * @param systemActivity
	 */
	public boolean modifyActivity(SystemActivity systemActivity);

	/**
	 * 执行修改活动定义的sql
	 * 
	 * @param sql
	 * @return
	 */
	public boolean execute(String sql);

}
