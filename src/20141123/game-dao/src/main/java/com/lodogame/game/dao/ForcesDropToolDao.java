package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.ForcesDropTool;

/**
 * 怪物部队掉落dao
 * 
 * @author jacky
 * 
 */
public interface ForcesDropToolDao {

	public List<ForcesDropTool> getForcesDropToolList(int forcesId);

}
