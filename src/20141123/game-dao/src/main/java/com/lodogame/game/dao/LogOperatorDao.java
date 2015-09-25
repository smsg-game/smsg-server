package com.lodogame.game.dao;


/**
 * 运营日志操作dao
 * @author foxwang
 *
 */
public interface LogOperatorDao {

	/**
	 * 添加命令
	 * 
	 * @param command
	 * @return
	 */
	public boolean add(String sql);

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public String get();
	
}
