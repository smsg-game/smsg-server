package com.lodogame.ldsg.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.lodogame.game.server.action.RequestAction;

public class ActionInterceptor implements MethodInterceptor {

	private static final Logger LOG = Logger.getLogger(ActionInterceptor.class);

	public Object invoke(MethodInvocation invocation) throws Throwable {

		RequestAction action = (RequestAction) invocation.getThis();

		if (action == null) {

		}

		LOG.debug("=================");

		Object object = invocation.proceed();

		return object;

	}

}
