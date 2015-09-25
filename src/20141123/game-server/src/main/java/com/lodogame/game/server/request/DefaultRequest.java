package com.lodogame.game.server.request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import com.lodogame.game.server.context.ConfigContext;
import com.lodogame.game.server.exception.RequestMessageIsEmptyException;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.ServerConnectorSessionMgr;
import com.lodogame.game.server.session.Session;
import com.lodogame.game.utils.DateUtils;

public class DefaultRequest extends AbstractRequest {

	private final static Logger logger = Logger.getLogger(DefaultRequest.class);

	public DefaultRequest(Message message, ConfigContext configContext,
			Channel channel) {
		this.configContext = configContext;
		this.channel = channel;
		init(message);
		logger.info("Req id(" + reqId + ") Req time(" + rt + ":"
				+ DateUtils.getDateStr(rt, "yyyy-MM-dd HH:mm:dd")
				+ "): handlerName=" + getRequestHandlerName() + " methodName="
				+ getMethodName());
	}

	private void init(Message message) {
		if (message == null) {
			throw new RequestMessageIsEmptyException();
		}
		String actStr = message.getAct();
		if ("connect".equals(actStr)) {
			this.requestHandlerName = "";
			this.methodName = "";
			if ("error".equals(actStr)) {
				this.requestHandlerName = "error";
				this.methodName = "";
			}
		} else {
			String[] actArr = actStr.split("\\.");
			this.requestHandlerName = actArr[0];
			this.methodName = actArr[1];
		}
		this.parameters = message.getDt();
		if (this.parameters == null) {
			this.parameters = new HashMap<String, Object>();
		}
		this.parameters.put("rc", message.getRc());
		if (StringUtils.isBlank(message.getReqId())) {
			reqId = UUID.randomUUID().toString();
		} else {
			reqId = message.getReqId();
		}
		if (message.getRt() == 0) {
			rt = System.currentTimeMillis();
		} else {
			rt = message.getRt();
		}
		bindSession(message);
	}

	private void bindSession(Message message) {
		String sid = DefaultSessionManager.getInstance().getSid(channel);
		session = getSession(sid);
	}

	private Session getSession(String sid) {
		if (sid != null && DefaultSessionManager.getInstance().hasSession(sid)) {
			return DefaultSessionManager.getInstance().getSession(sid);
		}
		if (sid != null
				&& ServerConnectorSessionMgr.getInstance().hasSession(sid)) {
			return ServerConnectorSessionMgr.getInstance().getSession(sid);
		} else {
			return DefaultSessionManager.getInstance().registerSession(channel);
		}
	}

	@Override
	public Map<String, Object> getParams() {
		return parameters;
	}

	@Override
	public void setParams(Map<String, Object> params) {
		this.parameters = params;
	}
}
