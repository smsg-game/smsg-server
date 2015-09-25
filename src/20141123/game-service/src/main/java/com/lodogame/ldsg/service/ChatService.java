package com.lodogame.ldsg.service;



public interface ChatService {

	/**
	 *  消息过长
	 */
	public static final int CHAT_MESSAGE_TOO_LONG = 2001;
	
	/**
	 *  发言人目前在禁言期间
	 */
	public static final int CHAT_USER_IS_BANNED = 2002;
	
	/**
	 *  发言人发送消息过快
	 */
	public static final int CHAT_USER_SEND_TOO_OFTEN = 2003;
	
	/**
	 *  发送的消息不能为空
	 */
	public static final int CHAT_MESSAGE_IS_NULL = 2004;
	
	/**
	 *  发送给的人不存在
	 */
	public static final int CHAT_TO_USER_NOT_EXIST = 2005;
	
	/**
	 *  私聊对象不在线
	 */
	public static final int CHAT_TO_USER_NOT_ONLINE = 2006;
	
	/**
	 *  聊天内容包含不合法的内容
	 */
	public static final int CHART_HAS_CONTAIN_ILLEGAL_WORDS = 2007;
	
	
	/**
	 * 发送消息
	 * 
	 * @param type
	 * @param toUserId
	 * @param content
	 */
	public String sendMessage(String userId,int type, String toUserId, String content);
	
	/**
	 * 禁言
	 * @param userId 
	 * 
	 */
	public void bannedToPost(String userId);

}
