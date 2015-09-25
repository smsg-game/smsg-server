package com.lodogame.game.server.response;

import com.lodogame.game.server.request.Message;

/**
 * 响应处理
 * @author CJ
 *
 */
public interface Response {
	/**
	 * 响应消息
	 * @param message
	 */
	public void setMessage(Message message);
	
	/**
	 * 获取消息
	 * @return
	 */
	public Message getMessage();

}
