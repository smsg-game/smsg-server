package com.lodogame.game.server.exception;

public class DuplicateSessionIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateSessionIdException(){
		super();
	}
	
	public DuplicateSessionIdException(String sid){
		super("重复session id[" + sid + "]");
	}

}
