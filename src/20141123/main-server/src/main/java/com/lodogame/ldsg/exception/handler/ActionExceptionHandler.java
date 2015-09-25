package com.lodogame.ldsg.exception.handler;

import org.apache.log4j.Logger;

import com.lodogame.game.server.exception.handler.ExceptionHandler;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.exception.ServiceException;

public class ActionExceptionHandler implements ExceptionHandler {
	private final static Logger LOG = Logger.getLogger(ActionExceptionHandler.class);
	@Override
	public void handler(Throwable e, Request request, Response response){
		if(e instanceof ServiceException){
			LOG.info(e.getMessage());
			response.getMessage().setRc(((ServiceException)e).getCode());
		}
		
		throw new RuntimeException(e);
	}

}
