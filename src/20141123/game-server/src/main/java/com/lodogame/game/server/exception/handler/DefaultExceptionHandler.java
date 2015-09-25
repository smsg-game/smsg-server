package com.lodogame.game.server.exception.handler;

import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;


public class DefaultExceptionHandler implements ExceptionHandler {

	@Override
	public void handler(Throwable e, Request request, Response response) {
		throw new RuntimeException(e);
	}

}
