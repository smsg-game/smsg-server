package com.lodogame.game.server.exception;

/**
 * 参数异常
 * @author CJ
 *
 */
public class IllegalArgumentException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public IllegalArgumentException(String message) {
		super(message);
	}

	public IllegalArgumentException(Throwable cause) {
		super(cause);
	}
}
