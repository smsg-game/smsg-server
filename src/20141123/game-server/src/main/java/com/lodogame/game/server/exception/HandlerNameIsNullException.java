package com.lodogame.game.server.exception;

public class HandlerNameIsNullException extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HandlerNameIsNullException() {
		super("请求处理器名称为空");
	}
}
