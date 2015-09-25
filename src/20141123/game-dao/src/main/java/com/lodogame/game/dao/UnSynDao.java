package com.lodogame.game.dao;

import java.util.Date;

/**
 * 异步执行sql 存储数据库
 * 
 * @author foxwang
 * 
 */
public interface UnSynDao {
	/**
	 * 游戏库内执行sql
	 */
	public void executSql(String sql);

	/**
	 * 日志库内执行sql
	 * 
	 * @param sql
	 */
	public void executeLogSql(String sql);

}
