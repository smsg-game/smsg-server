package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemGoldSet;

public interface SystemGoldSetDao {

	/**
	 * 获取列表
	 * 
	 * @param type
	 * @return
	 */
	public List<SystemGoldSet> getList(int type);

	/**
	 * 获取单个
	 * 
	 * @param amount
	 * @return
	 */
	public SystemGoldSet getByPayAmount(int type, double amount);

}
