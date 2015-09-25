package com.lodogame.game.server.exception;

/**
 * 服务器运行时错误
 * @author CJ
 *
 */
public class ServerRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerRuntimeException() {
		super();
	}

	public ServerRuntimeException(String message) {
		super(message);
	}

	public ServerRuntimeException(Throwable t) {
		super(t);
	}
}
