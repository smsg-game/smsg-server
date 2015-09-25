package com.lodogame.game.server.exception;

/**
 * GIG运行时异常
 * @author CJ
 *
 */
public class GSRuntimeExcepiton extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public GSRuntimeExcepiton(int code) {
		super();
		this.code = code;
	}

	public GSRuntimeExcepiton(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public GSRuntimeExcepiton(int code, String message) {
		super(message);
		this.code = code;
	}

	public GSRuntimeExcepiton(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}
}
