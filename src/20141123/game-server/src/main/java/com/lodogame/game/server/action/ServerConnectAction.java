package com.lodogame.game.server.action;

import org.apache.log4j.Logger;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.response.Response;

/**
 * 用于处理服务端连接请求， 当接收到源服务器请求时，将连接保存
 * 
 * @author CJ
 * 
 */
public class ServerConnectAction extends BaseRequestAction {

	protected static final Logger LOG = Logger.getLogger(ServerConnectAction.class);

	/**
	 * 连接器ID，
	 */
	private String serverType;
	private String connectorId;

	public Response connect() {
		LOG.debug(serverType + "服务器请求连接成功，sessionId：" + request.getSession().getSid());
		ServerConnectorMgr.getInstance().registerServer(serverType, connectorId, request.getSession().getSid());
		// Message msg = new Message();
		// msg.setRc(CodeConstants.SUCCESS);
		// response.setMessage(msg);
		return null;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}

	@Override
	public Response handle() {
		return null;
	}

}
