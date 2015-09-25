package com.lodogame.game.server.action;

import java.io.IOException;

import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;

/**
 * 请求处理器
 * 
 * @author CJ
 * 
 */
public interface RequestAction {
	/**
	 * 初始化Action
	 * 
	 * @param request
	 * @param response
	 */
	public void init(Request request, Response response);

	public Response handle() throws IOException;
}
