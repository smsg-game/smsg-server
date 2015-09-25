package com.lodogame.game.dao;

import com.lodogame.model.SystemBloodSacrifice;

public interface SystemBloodSacrificeDao {
	
	/**
	 * 获取血祭材料 
	 * 
	 * @param heroId
	 * @param stage
	 * @return
	 */
	public  SystemBloodSacrifice get(int heroId, int stage);
	
	
}
