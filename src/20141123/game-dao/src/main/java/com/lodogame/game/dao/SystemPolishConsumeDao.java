package com.lodogame.game.dao;

import com.lodogame.model.SystemPolishConsume;

public interface SystemPolishConsumeDao {

	/**
	 * 获取装备洗练消耗的信息
	 * 
	 * @param polishType
	 * @return
	 */
	public SystemPolishConsume getSystemPolishConsume(int polishType);

}
