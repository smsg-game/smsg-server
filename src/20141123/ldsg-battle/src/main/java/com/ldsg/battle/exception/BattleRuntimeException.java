package com.ldsg.battle.exception;

public class BattleRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BattleRuntimeException(String message) {
		super(message);
	}

	public BattleRuntimeException(String message, Throwable t) {
		super(message, t);
	}
}
