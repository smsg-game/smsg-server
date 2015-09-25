package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemToolDrop;

/**
 * 道具掉落dao
 * 
 * @author jacky
 * 
 */
public interface SystemToolDropDao {

	public List<SystemToolDrop> getSystemToolDropList(int toolId);

}
