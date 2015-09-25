package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemScene;

public interface SystemSceneDao {

	/**
	 * 获取系统关卡列表
	 */
	public List<SystemScene> getSystemSceneList();

	/**
	 * 获取关卡
	 * 
	 * @param sceneId
	 * @return
	 */
	public SystemScene get(int sceneId);
}
