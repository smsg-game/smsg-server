package com.lodogame.game.dao;

import com.lodogame.model.SystemDeifyAttr;

public interface SystemDeifyAttrDao {
	
	public SystemDeifyAttr get(int deifyId, int deifyLevel);
	
	public int maxLevel(int deifyId);
}
