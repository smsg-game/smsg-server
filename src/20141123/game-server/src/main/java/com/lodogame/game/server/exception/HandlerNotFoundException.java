package com.lodogame.game.server.exception;


/**
 * 请求处理器不存在异常
 * @author CJ
 *
 */
public class HandlerNotFoundException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HandlerNotFoundException(String handlerName) {
		super("请求处理器不存在[" + handlerName + "]");
	}

}
