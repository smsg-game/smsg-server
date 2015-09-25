package com.lodogame.game.server.exception;


/**
 * 请求消息为空
 * @author CJ
 *
 */
public class RequestMessageIsEmptyException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public RequestMessageIsEmptyException() {
		super("请求消息为空");
	}
}
