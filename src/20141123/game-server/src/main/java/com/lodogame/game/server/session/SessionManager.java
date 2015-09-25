package com.lodogame.game.server.session;

import java.util.Collection;
import java.util.Iterator;

import org.jboss.netty.channel.Channel;

/**
 * Session管理器，主要负责Session的注册、注销以及获取等管理
 * @author CJ
 *
 */
public interface SessionManager {
	/**
	 * 注册session
	 * @param session
	 * @return
	 */
	public Session registerSession(Channel channel);
	
	/**
	 * 注册session
	 * @param session
	 * @return
	 */
	public Session registerSession(String sid, Session session);
	
	/**
	 * 获得session
	 * @param sid
	 * @return
	 */
	public Session getSession(String sid);
	
	/**
	 * 注销session
	 * @param sid
	 * @return
	 */
	public void closeSession(String sid);
	
	/**
	 * 获取Sid
	 * @param channel
	 * @return
	 */
	public String getSid(Channel channel);
	
	/**
	 * Session是否存在
	 * @param sid
	 * @return
	 */
	public boolean hasSession(String sid);
	
	/**
	 * 注销session
	 * @param session
	 * @return
	 */
	public void closeSession(Session session);
	
	/**
	 * 获取所有session
	 * @return
	 */
	public Collection<Session> getAllSession();

	public Iterator<Session> getAllSessionKey();
	
	/**
	 * 将session移除出缓存，但并不断开，同时返回Session
	 * @param sid
	 * @return Session
	 */
	public Session removeSession(String sid);

	/**
	 * 设置关闭session处理器
	 * @param handler
	 */
	public void setSessionCloseHandler(SessionCloseHandler handler);

//	/**
//	 * 清除已经断开连接的会话
//	 */
//	public void clearDisconnectedSession();
}
