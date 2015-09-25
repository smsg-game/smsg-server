package com.lodogame.ldsg.handler;

import org.apache.log4j.Logger;

import com.lodogame.game.server.exception.handler.ExceptionHandler;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;

public class ActionExceptionHandler implements ExceptionHandler {

	private static final Logger LOG = Logger.getLogger(ActionExceptionHandler.class);

	public void handler(Throwable e, Request request, Response response) {

		if (e instanceof ServiceException) {
			LOG.debug(e.getMessage(), e);
			ServiceException se = (ServiceException) e;
			String uid = (String) request.getParameter("uid");
			response.getMessage().setRc(se.getCode());
			response.getMessage().setAttribate("uid", uid);
		} else {

			LOG.error(e.getMessage(), e);

			String uid = (String) request.getParameter("uid");
			response.getMessage().setRc(ServiceReturnCode.FAILD);
			response.getMessage().setAttribate("uid", uid);
		}
	}
}
