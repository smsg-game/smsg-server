package com.lodogame.game.server.session;

import java.io.IOException;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.lodogame.game.server.request.Message;

/**
 * 一个Session代表一个连接会话
 * 连接中断时，session被关闭以及移除
 * @author CJ
 *
 */
public interface Session {
	/**
	 * 初始化session
	 */
	public void init();
	
	/**
	 * 资源释放
	 */
	public void destroy();
	
	/**
	 * 关闭会话，将直接关闭socket
	 */
	public void close();
	
	/**
	 * 输出信息
	 */
	public void out(String message);
	
	/**
	 * 发送信息到其他session
	 */
	public void send(String sid, Message message) throws IOException ;
	
	/**
	 * 发送信息
	 */
	public ChannelFuture send(Message message) throws IOException ;
	
	/**
	 * 向所有session发送信息
	 * @param message
	 * @throws IOException 
	 */
	public void sendAll(Message message) throws IOException;
	
	/**
	 * 存储会话数据
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value);
	
	/**
	 * 根据name获取会话数据
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name);
	
	/**
	 * 移除数据
	 * @param name
	 * @return
	 */
	public void removeAttribute(String name);
	
	/**
	 * 获取SID
	 */
	public String getSid();
	
	/**
	 * 获取Channel
	 * @return
	 */
	public Channel getChannel();
}
