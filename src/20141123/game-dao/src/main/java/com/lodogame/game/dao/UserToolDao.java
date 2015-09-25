package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserTool;

public interface UserToolDao {

	/**
	 * 获取用户道具
	 * 
	 * @param userId
	 * @param toolId
	 * @return
	 */
	public UserTool get(String userId, int toolId);

	/**
	 * 获取用户 道具数量
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserToolNum(String userId, int toolId);

	/**
	 * 花费道具
	 * 
	 * @param userId
	 * @param toolId
	 * @param num
	 * @return
	 */
	public boolean reduceUserTool(String userId, int toolId, int num);

	/**
	 * 增加用户道具(数量)
	 * 
	 * @param userId
	 * @param toolId
	 * @param num
	 * @return
	 */
	public boolean addUserTool(String userId, int toolId, int num);

	/**
	 * 添加用户道具
	 * 
	 * @param userTool
	 * @return
	 */
	public boolean add(UserTool userTool);

	/**
	 * 获取用户道具列表
	 * 
	 * @return
	 */
	public List<UserTool> getList(String userId);

	/**
	 * 删除数量为0的道具(退出时调用)
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteZeroNumTools(String userId);
	
	public List<UserTool> getUserToolListByToolType(String userId, int toolType);
}
