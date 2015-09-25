package com.lodogame.game.server.request;

import java.util.Map;

import com.lodogame.game.server.session.Session;

/**
 * 请求信息
 * 
 * @author CJ
 * 
 */
public interface Request {
	public final static String REQUEST_HANDLER_NAME = "handler";
	/**
	 * 获取session
	 * @return
	 */
	public Session getSession();

	/**
	 * 获取参数
	 * @param name
	 * @return
	 */
	public Object getParameter(String name);

	/**
	 * 获取处理器名称
	 * @return
	 */
	public String getRequestHandlerName();
	
	/**
	 * 获取回调名称
	 * @return
	 */
	public String getMethodName();
	
	/**
	 * 设置调用方法名
	 * @param methodName
	 */
	public void setMethodName(String methodName);

	/**
	 * 获取当前服务器的IP
	 * @return
	 */
	public String getServerIp();
	
	/**
	 * 获取当前服务器端口
	 * @return
	 */
	public int getServerPort();
	
	/**
	 * 获取参数的Map
	 * @return
	 */
	public Map<String, Object> getParams();
	
	/**
	 * 整个参数Map替换
	 * @param params
	 * @return
	 */
	public void setParams(Map<String, Object> params);

	/**
	 * 获取请求ID
	 */
	public String getReqId();
	
	/**
	 * 设置请求ID
	 * @param reqId
	 * @return
	 */
	public void setReqId(String reqId);
	
	/**
	 * 获取请求时间
	 * @return
	 */
	public long getRt();
	
	/**
	 * 设置请求时间
	 */
	public void setRt(long rt);
}
