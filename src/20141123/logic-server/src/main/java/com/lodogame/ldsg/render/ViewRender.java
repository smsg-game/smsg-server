package com.lodogame.ldsg.render;

import java.util.Map;

import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;

/**
 * 视图渲染
 * 
 * @author jacky
 * 
 */
public interface ViewRender {

	/**
	 * (手游版)字段压缩
	 * 
	 * @param message
	 * @param params
	 * @return
	 */
	public Message render(Message message, Request req, Map<String, Object> params);

	/**
	 * (手游版)字段压缩
	 * 
	 * @param message
	 * @param params
	 * @param rc
	 * @return
	 */
	public Message render(Message message, Request req, Map<String, Object> params, int rc);

	/**
	 * (手游版)字段压缩
	 * 
	 * @param rs
	 * @param params
	 * @return
	 */
	public Response render(Response rs, Request req, Map<String, Object> params);

	/**
	 * (手游版)字段压缩
	 * 
	 * @param rs
	 * @param params
	 * @param rc
	 * @return
	 */
	public Response render(Response rs, Request req, Map<String, Object> params, int rc);
}
