package com.lodogame.game.server.request;

import java.util.Map;

import org.jboss.netty.channel.Channel;

import com.lodogame.game.server.context.ConfigContext;
import com.lodogame.game.server.context.Context;
import com.lodogame.game.server.session.Session;

public abstract class AbstractRequest implements Request {
	
	protected String requestHandlerName;
	protected String methodName;
	protected Session session;
	protected ConfigContext configContext;
	protected Map<String, Object> parameters;
	protected Channel channel;
	protected Context context;
	protected String reqId;
	protected long rt;

	@Override
	public long getRt() {
		return rt;
	}

	@Override
	public void setRt(long rt) {
		this.rt = rt;
	}

	@Override
	public Session getSession(){
		return session;
	}

	@Override
	public Object getParameter(String name) {
		return parameters.get(name);
	}
	
	@Override
	public String getRequestHandlerName(){
		return requestHandlerName;
	}
	@Override
	public String getServerIp(){
		return configContext.getIp();
	}

	@Override
	public int getServerPort(){
		return configContext.getPort();
	}
	
	@Override
	public String getMethodName() {
		return methodName;
	}

	@Override
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String getReqId(){
		return reqId;
	}
	
	@Override
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	
}
