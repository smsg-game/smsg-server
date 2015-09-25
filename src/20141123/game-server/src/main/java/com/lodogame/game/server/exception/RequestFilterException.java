package com.lodogame.game.server.exception;

import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;

/**
 * 过滤器异常，用来中断正常流程， 如验证登录失败后，抛出此异常，需要将返回信息作为异常数据传递
 * 
 * @author CJ
 * 
 */
public class RequestFilterException extends Exception {

	private static final long serialVersionUID = 1L;

	private Response response;
	private Request request;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public RequestFilterException(Request request, Response response) {
		super("用户未登录");
		this.request = request;
		this.response = response;
	}
}
