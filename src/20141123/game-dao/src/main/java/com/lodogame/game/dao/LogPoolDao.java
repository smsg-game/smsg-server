package com.lodogame.game.dao;

import com.lodogame.model.ILog;

public interface LogPoolDao {

	/**
	 * 拿到一条日志
	 * 
	 * @return
	 */
	public ILog get() throws InterruptedException;

	/**
	 * 添加一条日志
	 * 
	 * @param log
	 */
	public void add(ILog log);

}
