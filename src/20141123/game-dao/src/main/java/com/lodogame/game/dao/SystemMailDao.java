package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;

import com.lodogame.model.SystemMail;

public interface SystemMailDao {

	/**
	 * 根据时间获取系统邮件
	 * 
	 * @param date
	 * @param target
	 * @return
	 */
	public List<SystemMail> getSystemMailByTime(Date date);

	/**
	 * 获取所有邮件 *
	 * 
	 * @return
	 */
	public List<SystemMail> getSystemList();

	/**
	 * 获取系统邮件
	 * 
	 * @param systemMailId
	 * @return
	 */
	public SystemMail get(String systemMailId);

	/**
	 * 添加系统邮件
	 * 
	 * @param systemMail
	 * @return
	 */
	public boolean add(SystemMail systemMail);
	
	/**
	 * 拿最大的源ID
	 * @return
	 */
	public int getMaxSourceId();

}
