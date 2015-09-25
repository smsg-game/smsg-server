package com.lodogame.game.server.filter;

import com.lodogame.game.server.exception.RequestFilterException;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;

/**
 * 过滤器
 * 
 * @author CJ
 * 
 */
public interface RequestFilter {
	/**
	 * 初始化
	 */
	public void init();

	/**
	 * 销毁
	 */
	public void destroy();

	/**
	 * 过滤方法
	 * @param message
	 * @throws RequestFilterException 
	 */
	public void filter(Request request, Response response) throws RequestFilterException;
}
