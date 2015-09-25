package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemFloorObj;

public interface SystemFloorObjDao {

	/**
	 * 获取对象列表
	 * @param objType
	 * @return
	 */
	List<SystemFloorObj> getAll();

}
