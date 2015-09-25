package com.lodogame.game.server.exception.handler;

import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;

public interface ExceptionHandler {
	public void handler(Throwable e, Request request, Response response);
}
