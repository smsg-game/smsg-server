package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemTask;

/**
 * 系统任务DAO
 * 
 * @author jacky
 * 
 */
public interface SystemTaskDao {

	/**
	 * 获取系统任务
	 * 
	 * @param systemTaskId
	 * @return
	 */
	public SystemTask get(int systemTaskId);

	/**
	 * 获取后置任务
	 * 
	 * @param systemTaskId
	 * @return
	 */
	public List<SystemTask> getPosTaskList(int systemTaskId);
	
	/**
	 * 根据 task_target 获取系统任务列表
	 */
	public List<SystemTask> getByTaskTargetType(int targetType);

}
