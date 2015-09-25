package com.lodogame.game.dao;

import com.lodogame.model.Command;

/**
 * 命令行DAO
 * 
 * @author jacky
 * 
 */
public interface CommandDao {

	/**
	 * 添加命令
	 * 
	 * @param command
	 * @return
	 */
	public boolean add(Command command);

	/**
	 * 获取命令
	 * 
	 * @return
	 */
	public Command get(Integer... prioritys);

	/**
	 * 加载数据
	 * 
	 * @param className
	 * @return
	 */
	public boolean cacheReload(String className);
}
