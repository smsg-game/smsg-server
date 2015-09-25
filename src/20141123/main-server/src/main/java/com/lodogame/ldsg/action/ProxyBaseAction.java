package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.action.BaseRequestAction;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.manager.UserSessionManager;

public class ProxyBaseAction extends BaseRequestAction {

	private final static Logger LOG = Logger.getLogger(ProxyBaseAction.class);

	protected String getUserId() {
		return (String) request.getParameter("uid");
	}

	protected Message getProxyMessage() {
		Message msg = new Message();
		msg.setReqId(request.getReqId());
		msg.setRt(request.getRt());
		return msg;
	}

	protected Message getProxyReqMessage() {
		Message msg = new Message();
		msg.setReqId(request.getReqId());
		msg.setRt(request.getRt());
		msg.setDt(request.getParams());
		return msg;
	}

	@Override
	public Response handle() throws IOException {

		if (request.getMethodName().endsWith("Rq") || request.getMethodName().startsWith("push")) {
			this.render();
			return null;
		}

		return this.dispatchRequest(this.request.getRequestHandlerName() + "." + this.request.getMethodName());
	}

	/**
	 * 转发请求
	 * 
	 * @param action
	 * @throws IOException
	 */
	protected Response dispatchRequest(String action) throws IOException {

		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");
		if (session == null) {
			LOG.error("逻辑服务器未准备好");
			return null;
		}

		Message msg = getProxyMessage();
		msg.setAct(action);
		msg.setAttribate("uid", userToken.getUserId());
		Map<String, Object> params = this.request.getParams();
		for (Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			msg.setAttribate(key, value);
		}
		session.send(msg);
		return null;
	}

	public void Test() {

	}

	protected void render() throws IOException {
		String uid = getUserId();
		String userSessionId = UserSessionManager.getInstance().getUserSessionId(uid);
		Session session = DefaultSessionManager.getInstance().getSession(userSessionId);
		Message msg = response.getMessage();
		Map<String, Object> dt = request.getParams();
		msg.setRc((Integer) dt.get("rc"));
		dt.remove("uid");
		dt.remove("rc");
		msg.setDt(dt);
		if (session != null) {
			long rt = request.getRt();
			long resTime = System.currentTimeMillis() - rt;
			LOG.info("Main Server response reqId(" + request.getReqId() + "[" + resTime + "ms]): [act=" + request.getRequestHandlerName() + "." + request.getMethodName() + "]");
			logLongTimeRes(resTime);
			msg.setReqId(request.getReqId());
			// msg.setReqId("");
			msg.setRt(0);
			session.send(msg);
		} else {
			LOG.debug("推送消息失败，用户不在线.uid[" + uid + "],act[" + msg.getAct() + "]");
		}
	}

	/**
	 * 记录响应超过1秒的请求
	 */
	private void logLongTimeRes(long processMillSeconds) {
		if (processMillSeconds > 1000) {
			LOG.info("long time response：" + request.getReqId() + "[" + processMillSeconds + "ms]): [act=" + request.getRequestHandlerName() + "." + request.getMethodName());
		}
	}

}
