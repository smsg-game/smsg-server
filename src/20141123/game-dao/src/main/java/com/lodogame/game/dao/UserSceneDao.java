package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserScene;

/**
 * 用户场景(关卡)
 * 
 * @author jacky
 * 
 */
public interface UserSceneDao {

	/**
	 * 获取用户场景列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserScene> getUserSceneList(String userId);

	/**
	 * 添另用户场景
	 * 
	 * @param userScene
	 * @return
	 */
	public int add(UserScene userScene);
	
	/**
	 * 获取最后一个场景
	 * @param userId
	 * @return
	 */
	public UserScene getLastUserScene(String userId);

	/**
	 * 更新场景为通过状态
	 * @return
	 */
	public boolean updateScenePassed(String userId, int sceneId);
}
